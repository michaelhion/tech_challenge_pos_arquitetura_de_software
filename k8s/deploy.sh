#!/usr/bin/env bash

set -Eeuo pipefail

NAMESPACE="oficina360"

SCRIPT_DIR="$(
  cd "$(dirname "${BASH_SOURCE[0]}")"
  pwd
)"

KUBECTL="${KUBECTL:-kubectl}"

MANIFESTS=(
  "namespace.yaml"
  "configmap.yaml"
  "metrics-server.yaml"
  "postgres-pvc.yaml"
  "postgres-service.yaml"
  "postgres-deployment.yaml"
  "api-deployment.yaml"
  "api-service.yaml"
  "hpa.yaml"
)

show_header() {
  echo "======================================"
  echo " Oficina360 - Deploy Kubernetes"
  echo "======================================"
  echo ""
}

show_diagnostics() {
  echo ""
  echo "======================================"
  echo " Diagnóstico do ambiente"
  echo "======================================"

  echo ""
  echo "=== Nodes ==="
  $KUBECTL get nodes -o wide || true

  echo ""
  echo "=== Pods ==="
  $KUBECTL get pods \
    --namespace "$NAMESPACE" \
    -o wide || true

  echo ""
  echo "=== Deployments ==="
  $KUBECTL get deployments \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== Services ==="
  $KUBECTL get services \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== Endpoints ==="
  $KUBECTL get endpoints \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== PVCs ==="
  $KUBECTL get pvc \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== HPA ==="
  $KUBECTL get hpa \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== Eventos recentes ==="
  $KUBECTL get events \
    --namespace "$NAMESPACE" \
    --sort-by='.lastTimestamp' || true

  echo ""
  echo "=== Logs da API ==="
  $KUBECTL logs \
    deployment/oficina360-api \
    --namespace "$NAMESPACE" \
    --tail=100 || true

  echo ""
  echo "=== Logs do PostgreSQL ==="
  $KUBECTL logs \
    deployment/postgres \
    --namespace "$NAMESPACE" \
    --tail=100 || true
}

handle_error() {
  local exit_code=$?

  echo ""
  echo "ERRO: o deploy falhou com código $exit_code."

  show_diagnostics

  exit "$exit_code"
}

trap handle_error ERR

validate_files() {
  echo "[1/12] Verificando arquivos necessários..."

  for manifest in "${MANIFESTS[@]}"; do
    if [[ ! -f "$SCRIPT_DIR/$manifest" ]]; then
      echo "ERRO: manifesto não encontrado:"
      echo "$SCRIPT_DIR/$manifest"
      exit 1
    fi
  done

  echo "Todos os manifests necessários foram encontrados."
}

validate_manifests() {
  echo ""
  echo "[2/12] Validando manifests Kubernetes..."

  for manifest in "${MANIFESTS[@]}"; do
    echo "Validando: $manifest"

    $KUBECTL apply \
      --dry-run=client \
      --filename "$SCRIPT_DIR/$manifest" \
      >/dev/null
  done

  if [[ -x "$SCRIPT_DIR/validate.sh" ]]; then
    echo ""
    echo "Executando validate.sh..."

    "$SCRIPT_DIR/validate.sh"
  fi

  echo "Validação concluída."
}

check_cluster() {
  echo ""
  echo "[3/12] Verificando conexão com Kubernetes..."

  $KUBECTL cluster-info
  $KUBECTL get nodes -o wide
}

apply_namespace() {
  echo ""
  echo "[4/12] Criando namespace..."

  $KUBECTL apply \
    --filename "$SCRIPT_DIR/namespace.yaml"
}

configure_secret() {
  echo ""
  echo "[5/12] Configurando Secret..."

  if [[ -f "$SCRIPT_DIR/secret.yaml" ]]; then
    echo "Aplicando Secret local: secret.yaml"

    $KUBECTL apply \
      --filename "$SCRIPT_DIR/secret.yaml"

    return
  fi

  if $KUBECTL get secret oficina360-secret \
    --namespace "$NAMESPACE" \
    >/dev/null 2>&1; then

    echo "Secret oficina360-secret já existe no cluster."
    return
  fi

  echo "ERRO: o arquivo secret.yaml não existe e o Secret"
  echo "oficina360-secret também não foi encontrado no cluster."
  echo ""
  echo "Para desenvolvimento local:"
  echo "  cp \"$SCRIPT_DIR/secret-example.yaml\" \"$SCRIPT_DIR/secret.yaml\""
  echo ""
  echo "Depois, substitua os valores de exemplo."
  echo ""
  echo "Na pipeline, o Secret deve ser criado dinamicamente"
  echo "a partir dos GitHub Actions Secrets."

  exit 1
}

apply_configmap() {
  echo ""
  echo "[6/12] Aplicando ConfigMap..."

  $KUBECTL apply \
    --filename "$SCRIPT_DIR/configmap.yaml"
}

deploy_metrics_server() {
  echo ""
  echo "[7/12] Instalando Metrics Server..."

  $KUBECTL apply \
    --filename "$SCRIPT_DIR/metrics-server.yaml"

  echo "Aguardando Metrics Server..."

  $KUBECTL rollout status \
    deployment/metrics-server \
    --namespace kube-system \
    --timeout=300s

  echo ""
  echo "Verificando API de métricas..."

  for attempt in $(seq 1 18); do
    if $KUBECTL top nodes >/dev/null 2>&1; then
      echo "Metrics Server está fornecendo métricas."
      $KUBECTL top nodes
      return
    fi

    echo "Tentativa ${attempt}/18: aguardando métricas..."
    sleep 10
  done

  echo "AVISO: o Metrics Server foi implantado, mas kubectl top"
  echo "ainda não retornou métricas."
  echo "O HPA poderá mostrar métricas como <unknown> inicialmente."
}

deploy_postgres() {
  echo ""
  echo "[8/12] Implantando PostgreSQL..."

  echo "Aplicando PVC..."
  $KUBECTL apply \
    --filename "$SCRIPT_DIR/postgres-pvc.yaml"

  echo "Aplicando Service..."
  $KUBECTL apply \
    --filename "$SCRIPT_DIR/postgres-service.yaml"

  echo "Aplicando Deployment..."
  $KUBECTL apply \
    --filename "$SCRIPT_DIR/postgres-deployment.yaml"

  echo ""
  echo "Aguardando o PVC ficar vinculado..."

  for attempt in $(seq 1 30); do
    pvc_status="$(
      $KUBECTL get pvc postgres-pvc \
        --namespace "$NAMESPACE" \
        --output=jsonpath='{.status.phase}' \
        2>/dev/null || true
    )"

    if [[ "$pvc_status" == "Bound" ]]; then
      echo "PVC postgres-pvc está Bound."
      break
    fi

    if [[ "$attempt" -eq 30 ]]; then
      echo "ERRO: o PVC não ficou Bound dentro do prazo."

      echo ""
      echo "=== PVC ==="

      $KUBECTL describe pvc postgres-pvc \
        --namespace "$NAMESPACE" || true

      echo ""
      echo "=== Pod PostgreSQL ==="

      $KUBECTL describe pod \
        --namespace "$NAMESPACE" \
        --selector app=postgres || true

      echo ""
      echo "=== Eventos ==="

      $KUBECTL get events \
        --namespace "$NAMESPACE" \
        --sort-by='.lastTimestamp' || true

      exit 1
    fi

    echo "Tentativa ${attempt}/30: PVC em estado '${pvc_status:-desconhecido}'."
    sleep 5
  done

  echo ""
  echo "Aguardando PostgreSQL ficar disponível..."

  $KUBECTL rollout status \
    deployment/postgres \
    --namespace "$NAMESPACE" \
    --timeout=300s
}

deploy_api() {
  echo ""
  echo "[9/12] Implantando API..."

  $KUBECTL apply \
    --filename "$SCRIPT_DIR/api-deployment.yaml"

  echo ""
  echo "Aguardando API ficar disponível..."

  $KUBECTL rollout status \
    deployment/oficina360-api \
    --namespace "$NAMESPACE" \
    --timeout=600s
}

deploy_api_service() {
  echo ""
  echo "[10/12] Criando Service da API..."

  $KUBECTL apply \
    --filename "$SCRIPT_DIR/api-service.yaml"
}

deploy_hpa() {
  echo ""
  echo "[11/12] Criando HPA..."

  $KUBECTL apply \
    --filename "$SCRIPT_DIR/hpa.yaml"
}

show_result() {
  echo ""
  echo "[12/12] Validando recursos implantados..."

  echo ""
  echo "=== Pods ==="
  $KUBECTL get pods \
    --namespace "$NAMESPACE" \
    -o wide

  echo ""
  echo "=== Deployments ==="
  $KUBECTL get deployments \
    --namespace "$NAMESPACE"

  echo ""
  echo "=== Services ==="
  $KUBECTL get services \
    --namespace "$NAMESPACE"

  echo ""
  echo "=== Endpoints ==="
  $KUBECTL get endpoints \
    --namespace "$NAMESPACE"

  echo ""
  echo "=== PVCs ==="
  $KUBECTL get pvc \
    --namespace "$NAMESPACE"

  echo ""
  echo "=== HPA ==="
  $KUBECTL get hpa \
    --namespace "$NAMESPACE"

  echo ""
  echo "=== Imagem da API ==="

  $KUBECTL get deployment oficina360-api \
    --namespace "$NAMESPACE" \
    --output=jsonpath='{.spec.template.spec.containers[0].image}'

  echo ""
  echo ""
  echo "=== Acesso externo ==="

  node_port="$(
    $KUBECTL get service oficina360-api \
      --namespace "$NAMESPACE" \
      --output=jsonpath='{.spec.ports[0].nodePort}' \
      2>/dev/null || true
  )"

  if [[ -n "$node_port" ]]; then
    echo "NodePort da API: $node_port"
    echo "Health: http://IP_PUBLICO:${node_port}/actuator/health"
    echo "Swagger: http://IP_PUBLICO:${node_port}/swagger-ui/index.html"
  else
    echo "O Service não possui NodePort."
    echo "Consulte o endereço com:"
    echo "kubectl get service oficina360-api -n $NAMESPACE"
  fi
}

show_header
validate_files
validate_manifests
check_cluster
apply_namespace
configure_secret
apply_configmap
deploy_metrics_server
deploy_postgres
deploy_api
deploy_api_service
deploy_hpa
show_result

trap - ERR

echo ""
echo "======================================"
echo " Deploy concluído com sucesso!"
echo "======================================"
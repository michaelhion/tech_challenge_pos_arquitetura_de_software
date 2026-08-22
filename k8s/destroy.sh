#!/usr/bin

set -Eeuo pipefail

NAMESPACE="oficina360"
KUBECTL="${KUBECTL:-kubectl}"
AUTO_APPROVE="${AUTO_APPROVE:-false}"

show_header() {
  echo "======================================"
  echo " Oficina360 - Destroy Kubernetes"
  echo "======================================"
  echo ""
}

check_cluster() {
  echo "Verificando conexão com Kubernetes..."

  $KUBECTL cluster-info
  $KUBECTL get nodes -o wide
}

show_current_resources() {
  echo ""
  echo "Recursos atuais do namespace '$NAMESPACE':"
  echo ""

  if ! $KUBECTL get namespace "$NAMESPACE" >/dev/null 2>&1; then
    echo "O namespace '$NAMESPACE' não existe."
    return
  fi

  echo "=== Recursos principais ==="
  $KUBECTL get all \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== PVCs ==="
  $KUBECTL get pvc \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== ConfigMaps ==="
  $KUBECTL get configmaps \
    --namespace "$NAMESPACE" || true

  echo ""
  echo "=== Secrets ==="
  $KUBECTL get secrets \
    --namespace "$NAMESPACE" || true
}

confirm_destroy() {
  if [[ "$AUTO_APPROVE" == "true" ]]; then
    echo ""
    echo "AUTO_APPROVE=true. Confirmação manual ignorada."
    return
  fi

  echo ""
  echo "ATENÇÃO:"
  echo "Todos os recursos do namespace '$NAMESPACE' serão removidos."
  echo ""
  echo "Isso inclui:"
  echo "  - Deployments"
  echo "  - Pods"
  echo "  - Services"
  echo "  - ConfigMaps"
  echo "  - Secrets"
  echo "  - HPA"
  echo "  - PVC do PostgreSQL"
  echo ""
  echo "A exclusão do PVC poderá eliminar os dados do PostgreSQL."
  echo ""
  echo "O Metrics Server não será removido."
  echo "A EC2 e a infraestrutura AWS não serão removidas."
  echo ""

  read -r -p "Deseja continuar? [y/N] " confirmation

  if [[ ! "$confirmation" =~ ^[Yy]$ ]]; then
    echo "Operação cancelada."
    exit 0
  fi
}

delete_namespace() {
  echo ""
  echo "Removendo namespace '$NAMESPACE'..."

  $KUBECTL delete namespace "$NAMESPACE" \
    --ignore-not-found

  if ! $KUBECTL get namespace "$NAMESPACE" >/dev/null 2>&1; then
    echo "Namespace removido."
    return
  fi

  echo ""
  echo "Aguardando remoção do namespace..."

  if ! $KUBECTL wait \
    --for=delete \
    "namespace/$NAMESPACE" \
    --timeout=180s; then

    echo ""
    echo "ERRO: o namespace não foi removido dentro do prazo."

    echo ""
    echo "Estado atual do namespace:"

    $KUBECTL get namespace "$NAMESPACE" \
      --output=yaml || true

    exit 1
  fi
}

show_remaining_resources() {
  echo ""
  echo "Namespaces restantes:"

  $KUBECTL get namespaces

  echo ""
  echo "Metrics Server preservado:"

  $KUBECTL get deployment metrics-server \
    --namespace kube-system || true
}

show_header
check_cluster
show_current_resources

if ! $KUBECTL get namespace "$NAMESPACE" >/dev/null 2>&1; then
  echo ""
  echo "Nada para remover."
  exit 0
fi

confirm_destroy
delete_namespace
show_remaining_resources

echo ""
echo "======================================"
echo " Ambiente Kubernetes removido!"
echo "======================================"
echo ""
echo "A infraestrutura AWS continua ativa."
echo "Para remover a EC2, execute separadamente:"
echo ""
echo "  terraform -chdir=infra destroy"
echo ""
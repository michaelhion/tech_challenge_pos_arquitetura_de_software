#!/usr/bin/env bash

set -Eeuo pipefail

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

echo "======================================"
echo " Oficina360 - Validate Kubernetes"
echo "======================================"

for manifest in "${MANIFESTS[@]}"; do
  path="$SCRIPT_DIR/$manifest"

  if [[ ! -f "$path" ]]; then
    echo "ERRO: arquivo não encontrado: $path"
    exit 1
  fi

  echo "Validando: $manifest"

  $KUBECTL apply \
    --dry-run=client \
    --filename "$path" \
    >/dev/null
done
#
#if command -v yamllint >/dev/null 2>&1; then
#  echo ""
#  echo "Executando yamllint..."
#
#  yamllint "$SCRIPT_DIR"
#else
#  echo ""
#  echo "AVISO: yamllint não está instalado."
#fi

if command -v kubeconform >/dev/null 2>&1; then
  echo ""
  echo "Executando kubeconform..."

  for manifest in "${MANIFESTS[@]}"; do
    kubeconform \
      -strict \
      -summary \
      "$SCRIPT_DIR/$manifest"
  done
else
  echo ""
  echo "AVISO: kubeconform não está instalado."
fi

echo ""
echo "======================================"
echo " Validação concluída com sucesso!"
echo "======================================"
#!/usr/bin/env bash

set -e

NAMESPACE="oficina360"

echo "======================================"
echo " Oficina360 - Destroy Kubernetes"
echo "======================================"

echo ""
echo "ATENÇÃO!"
echo "Todos os recursos do namespace '$NAMESPACE' serão removidos."
echo ""

read -r -p "Deseja continuar? [y/N] " CONFIRM

if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
    echo "Operação cancelada."
    exit 0
fi

echo ""
echo "Verificando conexão com Kubernetes..."

kubectl cluster-info

echo ""
echo "Recursos atuais:"
kubectl get all -n "$NAMESPACE" 2>/dev/null || true

echo ""
echo "Removendo namespace '$NAMESPACE'..."

kubectl delete namespace "$NAMESPACE" --ignore-not-found

echo ""
echo "Aguardando remoção do namespace..."

kubectl wait \
    --for=delete namespace/"$NAMESPACE" \
    --timeout=120s 2>/dev/null || true

echo ""
echo "======================================"
echo " Ambiente removido!"
echo "======================================"

echo ""
echo "Namespaces restantes:"
kubectl get namespaces
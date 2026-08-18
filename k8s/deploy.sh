#!/usr/bin/env bash

set -e

NAMESPACE="oficina360"
K8S_DIR="k8s"
cd ..
echo "======================================"
echo " Oficina360 - Deploy Kubernetes"
echo "======================================"

echo ""
echo "[1/9] Verificando conexão com Kubernetes..."
kubectl cluster-info

echo ""
echo "[2/9] Criando namespace..."
kubectl apply -f "$K8S_DIR/namespace.yaml"

echo "[3/9] Instalando Metrics Server..."
kubectl apply -f "$K8S_DIR/metrics-server.yaml"

echo ""
echo "[4/9] Aplicando Secret..."
kubectl apply -f "$K8S_DIR/secret.yaml"

echo ""
echo "[5/9] Aplicando ConfigMap..."
kubectl apply -f "$K8S_DIR/configmap.yaml"

echo ""
echo "[6/9] Criando PostgreSQL..."
kubectl apply -f "$K8S_DIR/postgres-deployment.yaml"

echo ""
echo "[7/9] Criando Service do PostgreSQL..."
kubectl apply -f "$K8S_DIR/postgres-service.yaml"

echo ""
echo "Aguardando PostgreSQL ficar disponível..."
kubectl rollout status deployment/postgres \
    -n "$NAMESPACE" \
    --timeout=180s

echo ""
echo "[7/9] Criando API..."
kubectl apply -f "$K8S_DIR/api-deployment.yaml"

echo ""
echo "Criando Service da API..."
kubectl apply -f "$K8S_DIR/api-service.yaml"

echo ""
echo "Aguardando API ficar disponível..."
kubectl rollout status deployment/oficina360-api \
    -n "$NAMESPACE" \
    --timeout=180s

echo ""
echo "[8/8] Criando HPA..."
kubectl apply -f "$K8S_DIR/hpa.yaml"

echo ""
echo "======================================"
echo " Deploy concluído!"
echo "======================================"

echo ""
echo "Pods:"
kubectl get pods -n "$NAMESPACE"

echo ""
echo "Services:"
kubectl get services -n "$NAMESPACE"

echo ""
echo "HPA:"
kubectl get hpa -n "$NAMESPACE"

echo ""
echo "Deployments:"
kubectl get deployments -n "$NAMESPACE"
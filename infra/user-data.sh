#!/bin/bash

set -e

exec > >(tee /var/log/user-data.log | logger -t user-data -s 2>/dev/console) 2>&1

echo "=== Iniciando configuração da máquina ==="

# Atualização do sistema
dnf update -y

# Pacotes básicos
dnf install -y \
  curl \
  wget \
  git \
  unzip \
  ca-certificates

echo "=== Configurando Swap ==="

# Kubernetes + PostgreSQL + JVM em instâncias pequenas

if [ ! -f /swapfile ]; then
    fallocate -l 2G /swapfile
    chmod 600 /swapfile
    mkswap /swapfile
    swapon /swapfile

    echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi

echo "=== Instalando K3s ==="

curl -sfL https://get.k3s.io | sh -

echo "=== Aguardando K3s ==="

systemctl enable k3s
systemctl start k3s

echo "=== Verificando Kubernetes ==="

sleep 20

/usr/local/bin/kubectl get nodes

echo "=== K3s instalado com sucesso ==="

echo "=== Configurando kubeconfig ==="

mkdir -p /home/ec2-user/.kube

cp /etc/rancher/k3s/k3s.yaml /home/ec2-user/.kube/config

chown -R ec2-user:ec2-user /home/ec2-user/.kube

chmod 600 /home/ec2-user/.kube/config

echo "export KUBECONFIG=/home/ec2-user/.kube/config" >> /home/ec2-user/.bashrc

echo "=== Configuração concluída ==="
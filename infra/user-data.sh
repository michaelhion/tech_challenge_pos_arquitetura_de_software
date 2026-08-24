#!/usr/bin/env bash

set -Eeuo pipefail

exec > >(tee /var/log/user-data.log | logger -t user-data -s 2>/dev/console) 2>&1

echo "======================================"
echo " Oficina360 - Bootstrap EC2"
echo "======================================"

echo ""
echo "[1/7] Verificando sistema operacional..."

source /etc/os-release

echo "Sistema: ${PRETTY_NAME:-desconhecido}"

if [[ "${ID:-}" != "amzn" ]]; then
  echo "ERRO: este script foi preparado para Amazon Linux."
  exit 1
fi

echo ""
echo "[2/7] Atualizando metadados dos repositórios..."

dnf makecache -y

echo ""
echo "[3/7] Instalando pacotes básicos..."

dnf install -y \
  wget \
  git \
  unzip \
  ca-certificates

echo ""
echo "Verificando curl já fornecido pela AMI..."

if ! command -v curl >/dev/null 2>&1; then
  echo "ERRO: o comando curl não está disponível."
  exit 1
fi

curl --version

echo ""
echo "[4/7] Configurando swap..."

if [[ ! -f /swapfile ]]; then
  fallocate -l 2G /swapfile
  chmod 600 /swapfile
  mkswap /swapfile
fi

if ! swapon --show=NAME --noheadings | grep -qx '/swapfile'; then
  swapon /swapfile
fi

if ! grep -q '^/swapfile ' /etc/fstab; then
  echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi

echo ""
echo "Swap configurada:"

swapon --show
free -h

echo ""
echo "[5/7] Instalando K3s..."

if ! command -v k3s >/dev/null 2>&1; then
  curl -sfL https://get.k3s.io \
    | INSTALL_K3S_VERSION="v1.36.1+k3s1" sh -
else
  echo "K3s já está instalado."
fi

echo ""
echo "[6/7] Aguardando K3s..."

systemctl enable k3s
systemctl start k3s

for attempt in $(seq 1 60); do
  if /usr/local/bin/k3s kubectl get nodes >/dev/null 2>&1; then
    echo "K3s disponível."
    break
  fi

  if [[ "$attempt" -eq 60 ]]; then
    echo "ERRO: o K3s não ficou disponível dentro do prazo."

    systemctl status k3s --no-pager || true
    journalctl -u k3s -n 200 --no-pager || true

    exit 1
  fi

  echo "Tentativa ${attempt}/60: aguardando K3s..."
  sleep 10
done

echo ""
echo "[7/7] Configurando kubeconfig para ec2-user..."

install \
  -d \
  -m 700 \
  -o ec2-user \
  -g ec2-user \
  /home/ec2-user/.kube

install \
  -m 600 \
  -o ec2-user \
  -g ec2-user \
  /etc/rancher/k3s/k3s.yaml \
  /home/ec2-user/.kube/config

echo ""
echo "=== Nó Kubernetes ==="

/usr/local/bin/k3s kubectl get nodes -o wide

echo ""
echo "=== Serviço K3s ==="

systemctl status k3s --no-pager

echo ""
echo "======================================"
echo " Bootstrap concluído com sucesso!"
echo "======================================"

echo "Obtendo IP público da EC2..."

TOKEN="$(
  curl -sS -X PUT \
    -H "X-aws-ec2-metadata-token-ttl-seconds: 21600" \
    http://169.254.169.254/latest/api/token
)"

PUBLIC_IP="$(
  curl -sS \
    -H "X-aws-ec2-metadata-token: $TOKEN" \
    http://169.254.169.254/latest/meta-data/public-ipv4
)"

if [[ -z "$PUBLIC_IP" ]]; then
  echo "ERRO: não foi possível identificar o IP público."
  exit 1
fi

echo "IP público identificado."

echo "Instalando K3s com TLS SAN..."

curl -sfL https://get.k3s.io \
  | INSTALL_K3S_EXEC="server --tls-san ${PUBLIC_IP}" sh -
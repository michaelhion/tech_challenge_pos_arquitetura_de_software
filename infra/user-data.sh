#!/usr/bin/env bash

set -Eeuo pipefail

exec > >(tee /var/log/user-data.log | logger -t user-data -s 2>/dev/console) 2>&1

readonly BOOTSTRAP_MARKER="/var/lib/oficina360-bootstrap-complete"

on_error() {
  exit_code=$?

  echo ""
  echo "======================================"
  echo " Bootstrap falhou"
  echo " Código: ${exit_code}"
  echo "======================================"

  echo ""
  echo "=== K3s ==="

  systemctl status k3s \
    --no-pager \
    --full || true

  echo ""
  echo "=== SSM Agent ==="

  systemctl status amazon-ssm-agent \
    --no-pager \
    --full || true

  echo ""
  echo "=== Logs K3s ==="

  journalctl \
    --unit k3s \
    --lines 200 \
    --no-pager || true

  exit "$exit_code"
}

trap on_error ERR

echo "======================================"
echo " Oficina360 - Bootstrap EC2"
echo "======================================"

echo ""
echo "[1/8] Verificando sistema operacional..."

source /etc/os-release

echo "Sistema: ${PRETTY_NAME:-desconhecido}"

if [[ "${ID:-}" != "amzn" ]]; then
  echo "ERRO: este script foi preparado para Amazon Linux."
  exit 1
fi

echo ""
echo "[2/8] Atualizando metadados dos repositórios..."

dnf makecache -y

echo ""
echo "[3/8] Instalando pacotes básicos..."

dnf install -y \
  wget \
  git \
  unzip \
  ca-certificates

if ! command -v curl >/dev/null 2>&1; then
  echo "ERRO: o comando curl não está disponível."
  exit 1
fi

curl --version

echo ""
echo "[4/8] Configurando SSM Agent..."

if ! systemctl list-unit-files \
  | grep -q '^amazon-ssm-agent.service'; then

  echo "Instalando amazon-ssm-agent..."

  dnf install -y amazon-ssm-agent
fi

systemctl enable amazon-ssm-agent
systemctl restart amazon-ssm-agent

if ! systemctl is-active \
  --quiet \
  amazon-ssm-agent; then

  echo "ERRO: amazon-ssm-agent não está ativo."

  systemctl status amazon-ssm-agent \
    --no-pager \
    --full || true

  exit 1
fi

echo "SSM Agent ativo."

echo ""
echo "[5/8] Configurando swap..."

if [[ ! -f /swapfile ]]; then
  fallocate -l 2G /swapfile
  chmod 600 /swapfile
  mkswap /swapfile
fi

if ! swapon --show=NAME --noheadings \
  | grep -qx '/swapfile'; then

  swapon /swapfile
fi

if ! grep -q '^/swapfile ' /etc/fstab; then
  echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi

echo "Swap configurada:"

swapon --show
free -h

echo ""
echo "[6/8] Instalando K3s..."

if ! command -v k3s >/dev/null 2>&1; then
  curl -sfL https://get.k3s.io | sh -
else
  echo "K3s já está instalado."
fi

systemctl enable k3s
systemctl restart k3s

echo ""
echo "[7/8] Aguardando API do K3s..."

for attempt in $(seq 1 60); do
  if timeout 15 \
    /usr/local/bin/k3s kubectl get nodes \
    >/dev/null 2>&1; then

    echo "K3s disponível na tentativa ${attempt}."

    /usr/local/bin/k3s kubectl get nodes -o wide

    break
  fi

  if [[ "$attempt" -eq 60 ]]; then
    echo "ERRO: K3s não ficou disponível dentro do prazo."

    systemctl status k3s \
      --no-pager \
      --full || true

    journalctl \
      --unit k3s \
      --lines 200 \
      --no-pager || true

    exit 1
  fi

  echo "Tentativa ${attempt}/60: aguardando K3s..."
  sleep 10
done

echo ""
echo "[8/8] Configurando kubeconfig para ec2-user..."

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

mkdir -p "$(dirname "$BOOTSTRAP_MARKER")"

touch "$BOOTSTRAP_MARKER"

echo ""
echo "=== Validação final ==="

systemctl is-active amazon-ssm-agent
systemctl is-active k3s

/usr/local/bin/k3s kubectl get nodes -o wide

echo ""
echo "======================================"
echo " Bootstrap concluído com sucesso"
echo "======================================"

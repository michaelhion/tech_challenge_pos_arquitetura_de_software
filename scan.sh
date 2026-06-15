#!/bin/bash

echo "Rodando testes e Dependency-Check..."
./mvnw clean verify

echo "Rodando Trivy filesystem..."
trivy fs --scanners vuln,secret,misconfig --severity HIGH,CRITICAL --format table --output trivy-fs-report.txt .

echo "Build da imagem Docker..."
docker build -t oficina360-api:local .

echo "Rodando Trivy image..."
trivy image --severity HIGH,CRITICAL --format table --output trivy-image-report.txt oficina360-api:local

echo "Scans finalizados."
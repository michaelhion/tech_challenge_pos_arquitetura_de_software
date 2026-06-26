#!/bin/bash

echo "Validando pré-requisitos do Oficina360..."
echo ""

if ! command -v java >/dev/null 2>&1; then
    echo "❌ Java não encontrado"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')

echo "Versão do Java encontrada: $JAVA_VERSION"

if ! echo "$JAVA_VERSION" | grep -q "^21"; then
    echo "❌ Java 21 é obrigatório"
    exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
    echo "❌ Docker não encontrado"
    exit 1
fi

if ! docker info >/dev/null 2>&1; then
    echo "❌ Docker instalado, mas não está em execução"
    exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
    echo "❌ Docker Compose não encontrado"
    exit 1
fi

echo "✅ Java 21 encontrado"
echo "✅ Docker encontrado"
echo "✅ Docker Compose encontrado"
echo ""
echo "✅ Ambiente validado com sucesso"
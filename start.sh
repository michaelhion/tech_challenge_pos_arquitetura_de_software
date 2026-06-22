#!/bin/bash

echo "Building application..."

./mvnw clean package -DskipTests

echo "Starting containers..."

docker compose up -d; docker compose logs -f
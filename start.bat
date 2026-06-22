@echo off

echo Building application...

call mvnw.cmd clean package -DskipTests

echo Starting containers...

docker compose up --build
#!/bin/bash
set -e
if [ $# -eq 0 ]
then
  exit 1
fi
APP=$1
COMMANDS="clean package"

docker-compose -f docker-compose.yml down
docker-compose -f docker-compose.yml up -d rabbitmq accounts payments tokens transactions
sleep 30
echo "Running mvn clean package -pl $APP"
mvn clean package -pl $APP
docker-compose -f docker-compose.yml down

echo "Starting docker build..."
docker-compose -f docker-compose.yml build $APP
echo "Docker build done"





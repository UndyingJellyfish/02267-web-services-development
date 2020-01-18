#!/bin/bash
set -e
if [ $# -eq 0 ]
then
  exit 1
fi
APP=$1
COMMANDS="clean package"

docker-compose -f docker-compose.yml up -d --scale ${APP}=0
echo "Running mvn -pl $APP clean package"
mvn -pl $APP clean package
docker-compose -f docker-compose yml down

echo "Starting docker build..."
docker-compose -f docker-compose.yml build $APP
echo "Docker build done"





#!/bin/bash
set -e
if [ $# -eq 0; ] then
  exit 1
fi

APP=$1
mvn clean package -pl $APP
docker-compose -f docker-compose.yml build $APP




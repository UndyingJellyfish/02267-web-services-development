#!/bin/bash
set -e
if [ $# -eq 0 ]
then
  exit 1
fi
APP=$1
COMMANDS="clean package"
if [ "$APP" == "library" ]
then
  COMMANDS+=" install"
fi
echo "Running mvn -pl $APP $COMMANDS"
mvn -pl $APP $COMMANDS
if [ "$APP" != "library" ]
then
  echo "Starting docker build..."
  docker-compose -f docker-compose.yml build $APP
  echo "Docker build done"
fi





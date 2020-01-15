#!/bin/bash
set -e
docker-compose --file docker-compose.yml build
docker-compose --file docker-compose.yml up -d

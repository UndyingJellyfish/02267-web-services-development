#!/bin/bash
set -e
docker-compose build --file docker-compose.yml
docker-compose up -d --build --file docker-compose.yml

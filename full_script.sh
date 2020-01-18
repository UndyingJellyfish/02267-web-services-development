#!/bin/bash
set -e

mvn clean

bash build_and_test.sh library

bash build_and_test.sh payments
bash build_and_test.sh tokens
bash build_and_test.sh accounts
bash build_and_test.sh transactions

if [ $1 ]
then
  bash systemTests.sh application
  docker-compose -f docker-compose.yml up -d
else
  docker-compose -f docker-compose.yml up -d --scale application=0 --scale accounts=0
fi






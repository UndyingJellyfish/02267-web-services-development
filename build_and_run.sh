#!/bin/bash
echo "Starting build"

mvn package
java -jar target/fastmoney-06-1.0-SNAPSHOT.jar

echo "Build done"

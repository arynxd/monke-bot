#!/bin/bash

git pull
gradle clean shadowJar
buildStatus=$?

if [[ buildStatus  -eq 0 ]]
then
  clear
  java -jar build/libs/Monke-0.0.1-all.jar
fi

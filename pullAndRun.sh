#!/bin/bash

git pull
gradle clean shadowJar
buildStatus=$?

if [[ buildStatus  -eq 0 ]]
then
  clear
  java -jar build/libs/IGSQBot-0.0.1-all.jar
fi

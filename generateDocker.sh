#!/usr/bin/env bash

git pull
./gradlew clean shadowJar
docker build .
tagName=$(docker images | awk '{print $3}' | awk 'NR==2')
docker tag $tagName arynxd/monkebot:dev-latest
docker login
docker push arynxd/monkebot:dev-latest
echo "Done!"

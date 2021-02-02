FROM openjdk:15-alpine
WORKDIR /home/monkebot

RUN mkdir /tmp/monkebot/ && apk update && apk upgrade
COPY ./* /tmp/monkebot/
RUN /tmp/monkebot/gradlew clean shadowJar && cp /tmp/monkebot/build/libs/Monke-0.0.1-all.jar Monke.jar && rm -r /tmp/monkebot
ENTRYPOINT java -jar Monke.jar

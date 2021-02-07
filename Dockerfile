FROM openjdk:15
WORKDIR /home/monkebot/
COPY build/libs/Monke-all.jar Monke.jar
RUN apk update && apk upgrade
ENTRYPOINT java -jar Monke.jar

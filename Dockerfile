FROM openjdk:15
WORKDIR /home/monkebot

RUN ./* /tmp/monkebot/ && ./gradlew clean shadowJar
COPY /tmp/monkebot/build/libs/Monke-0.0.1-all.jar Monke.jar

RUN rm -r /tmp/monkebot
ENTRYPOINT ["java"]
CMD ["-jar", "Monke.jar"]

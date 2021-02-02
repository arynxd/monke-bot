FROM openjdk:15

WORKDIR /home/monkebot

COPY ./build/libs/Monke-0.0.1-all.jar Monke.jar

ENTRYPOINT ["java"]
CMD ["-jar", "Monke.jar"]

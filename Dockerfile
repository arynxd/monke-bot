FROM gradle:6.8.1-jdk15
WORKDIR /home/monkebot
COPY ./* /home/gradle/

RUN gradle clean shadowJar && cp /home/gradle/build/libs/Monke-*-all.jar Monke.jar && rm -rf /home/gradle/
ENTRYPOINT java -jar build/libs/Monke-0.0.1-all.jar

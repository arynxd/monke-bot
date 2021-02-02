FROM gradle:6.8.1-jdk15
WORKDIR /home/monkebot
COPY ./* /home/gradle/
ENTRYPOINT gradle runShadow

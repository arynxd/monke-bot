FROM gradle:6.8.1-jdk15
WORKDIR /home/monkebot
COPY . /home/gradle/

RUN ls -lah
RUN cd /home/gradle/ &&\
		cp /home/gradle/gradle.properties.example /home/gradle/gradle.properties &&\
		gradle clean shadowJar &&\
	 	cp /home/gradle/build/libs/Monke-*-all.jar /home/monkebot/Monke.jar &&\
	  rm -rf /home/gradle/* &&\
		cd /home/monkebot

ENTRYPOINT java -jar Monke.jar

FROM java:latest
MAINTAINER Rainer Burgstaller <rburgst@gmail.com>
LABEL Description="Java 8 image including openjfx dependencies"

RUN apt-get update && apt-get install -y --no-install-recommends openjfx && rm -rf /var/lib/apt/lists/*


EXPOSE 4567

VOLUME /data 

COPY *.jar /service.jar

ENTRYPOINT ["java", "-jar", "/service.jar"]

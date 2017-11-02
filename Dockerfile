FROM openjdk:8-jdk

EXPOSE 4567

VOLUME /home/amjad/hp_data

COPY build/*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
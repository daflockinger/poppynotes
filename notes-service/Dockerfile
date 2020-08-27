FROM amazoncorretto:11.0.3
VOLUME /tmp
COPY target/notes-service-0.0.1.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM amazoncorretto:11.0.3
VOLUME /tmp
COPY notes-service/target/notes-service-0.0.1.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM openjdk:8-jre-alpine
LABEL maintainer="liyachao"
VOLUME /tmp
ADD target/spring-boot-admin-server-ui-2.1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
FROM amazoncorretto:17.0.7-alpine3.17

LABEL maintainer="Tiwari, Ashish"

WORKDIR /app

COPY target/tyd-users-module.jar .

ENTRYPOINT ["java","-Dspring.profiles.active=aws", "-jar", "tyd-users-module.jar"]
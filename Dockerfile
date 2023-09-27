FROM amazoncorretto:17

LABEL maintainer="Tiwari, Ashish"

WORKDIR /app

COPY target/tyd-users-module.jar /app/tyd-users-module.jar

ENTRYPOINT ["java","-Dspring.profiles.active=local", "-jar", "tyd-users-module.jar"]
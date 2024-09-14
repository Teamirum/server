FROM openjdk:17
WORKDIR /app

COPY /build/libs/backend-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
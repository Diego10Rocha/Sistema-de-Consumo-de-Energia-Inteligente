# Use an official OpenJDK runtime as a parent image
FROM openjdk:11

RUN mkdir /monitor_usuario
WORKDIR /monitor_usuario
COPY /target/monitor-usuario-1.0-SNAPSHOT-jar-with-dependencies.jar /monitor_usuario
#RUN mvn clean package

#CMD ["java", "-jar", "target/medidor-1.0-SNAPSHOT-jar-with-dependencies.jar"]
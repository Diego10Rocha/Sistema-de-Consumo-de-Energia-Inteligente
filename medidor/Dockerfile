# Use an official OpenJDK runtime as a parent image
FROM openjdk:11

RUN mkdir /medidor
WORKDIR /medidor
COPY /target/medidor-1.0-SNAPSHOT-jar-with-dependencies.jar /medidor
#RUN mvn clean package

#CMD ["java", "-jar", "target/medidor-1.0-SNAPSHOT-jar-with-dependencies.jar"]
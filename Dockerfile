FROM openjdk:19
ARG JAR_FILE=build/libs/Backend-Order-0.0.1-SNAPSHOT.jar
ARG MONGODB_URI=mongodb://localhost:27017
ARG KAFKA_URL=localhost:9092
ARG APPLICATION_PORT=8085
ENV MONGODB_URI=$MONGODB_URI
ENV APPLICATION_PORT=$APPLICATION_PORT
ENV KAFKA_URL=$KAFKA_URL
EXPOSE 8085
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

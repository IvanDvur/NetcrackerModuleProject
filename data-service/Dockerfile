FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
WORKDIR /app
RUN apt-get update && apt-get install -y wkhtmltoimage
ADD ./data-service/target/data-service.jar data-service.jar
ENTRYPOINT ["java","-jar","data-service.jar"]
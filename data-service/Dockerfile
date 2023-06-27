FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
WORKDIR /app
RUN apt update
RUN apt -y install wget
RUN apt-get update && apt-get install -y libjpeg62-turbo
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.buster_amd64.deb
RUN apt install ./wkhtmltox_0.12.6-1.buster_amd64.deb
ADD ./data-service/target/data-service.jar data-service.jar
ENTRYPOINT ["java","-jar","data-service.jar"]
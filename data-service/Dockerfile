FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
WORKDIR /app
RUN sudo apt -y install wget
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.buster_amd64.deb
RUN sudo apt install ./wkhtmltox_0.12.6-1.buster_amd64.deb
ADD ./data-service/target/data-service.jar data-service.jar
ENTRYPOINT ["java","-jar","data-service.jar"]
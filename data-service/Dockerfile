FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
WORKDIR /app
RUN mkdir templates
RUN apt update
RUN apt -y install wget
RUN wget https://github.com/wkhtmltopdf/packaging/releases/download/0.12.6-1/wkhtmltox_0.12.6-1.buster_amd64.deb
RUN wget http://ftp.de.debian.org/debian/pool/main/libj/libjpeg-turbo/libjpeg62-turbo_2.0.6-4_amd64.deb
RUN dpkg -i libjpeg62-turbo_2.0.6-4_amd64.deb
RUN apt install -y ./wkhtmltox_0.12.6-1.buster_amd64.deb
ADD ./data-service/target/data-service.jar data-service.jar
ENTRYPOINT ["java","-jar","data-service.jar"]
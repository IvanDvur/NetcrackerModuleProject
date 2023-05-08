FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8082
ADD target/new-sender-service.jar new-sender-service.jar
ENTRYPOINT ["java","-jar","new-sender-service.jar"]
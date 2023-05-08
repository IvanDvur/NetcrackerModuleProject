FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8081
ADD ./send-manager/target/send-manager.jar send-manager.jar
ENTRYPOINT ["java","-jar","send-manager.jar"]
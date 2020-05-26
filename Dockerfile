FROM openjdk:8


ADD target/web/WebApplicationService.jar    WebApplicationService.jar
ADD application-configuration.properties    application-configuration.properties
EXPOSE 8080

ENTRYPOINT ["java", "-jar", " -Dspring.profiles.active=default",
"-Dspring.config.location=application-configuration.properties", "WebApplicationService.jar"]
# similary run the docker imagages for Scheduler  ie SchedulerService.jar
# similary run the docker imagages for KafkaListener  ie ListenerService.jar
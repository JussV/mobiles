FROM openjdk:11-slim

MAINTAINER Joana Goshevska
VOLUME /tmp
#RUN apt-get update -y && apt-get install -y maven
#RUN mvn clean install
COPY target/mobiles*.jar /opt/subscriptions/mobiles.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar","/opt/subscriptions/mobiles.jar"]
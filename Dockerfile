FROM ubuntu:14:04

RUN apt-get update
RUN apt-get install openjdk-8-jdk

COPY CatServer.java /CatServer.java
VOLUME ["/data"]
COPY file /data/strings.txt
RUN javac CatServer.java
ENTRYPOINT ["java CatServer"]

FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y openjdk-7-jdk

COPY CatServer.java /CatServer.java
RUN javac CatServer.java
VOLUME ["/data"]
ENTRYPOINT java CatServer /data/strings.txt 8000

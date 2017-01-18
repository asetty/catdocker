FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install -y openjdk-7-jdk

COPY CatClient.java /CatClient.java
RUN javac CatClient.java

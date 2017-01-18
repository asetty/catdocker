FROM ubuntu:14.04

RUN mkdir /data
COPY file /data/strings.txt
VOLUME ["/data"]


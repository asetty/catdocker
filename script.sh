#!/bin/bash
docker build -t datacontainer .
docker build -t catserver ./server
docker build -t catclient ./client
docker run -d  --name datavol datacontainer /bin/bash
docker run -d -P --volumes-from datavol --name=server catserver
docker run -P --volumes-from datavol --name=client --link=server catclient
docker stop datavol
docker stop server
docker stop client
docker rm datavol
docker rm server
docker rm client

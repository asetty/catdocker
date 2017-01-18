#!/bin/bash
docker build -t datacontainer .
docker build -t catserver ./server
docker build -t catclient ./client
docker run -d  --name datavol datacontainer /bin/bash
docker run -d -P --volumes-from datavol --name=server catserver
HOST="$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' server)"
docker run -P --volumes-from datavol --name=client catclient \
java CatClient /data/strings.txt $HOST 8000
docker stop datavol
docker stop server
docker stop client
docker rm datavol
docker rm server
docker rm client

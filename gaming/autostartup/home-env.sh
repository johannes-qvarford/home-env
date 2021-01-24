#!/bin/bash

# Wait for docker to start up.
docker 2>/dev/null
while test $? -ne 0; do
    echo waiting for docker...
    sleep 5
    docker 2>/dev/null
done

# Just to be sure that docker accepts commands, wait a bit more.
sleep 5

docker stop docker-nginx
docker rm docker-nginx
docker run --name docker-nginx -p 80:80 -v /home/jq/home-env/gaming/win-server/sites-enabled:/etc/nginx/conf.d -d nginx
sleep 5
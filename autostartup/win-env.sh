#!/bin/bash

docker stop docker-nginx
docker rm docker-nginx
docker run --name docker-nginx -p 80:80 -v /home/jq/win-env/win-server/sites-enabled:/etc/nginx/conf.d -d nginx
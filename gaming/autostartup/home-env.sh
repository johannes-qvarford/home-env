#!/bin/bash

# Make sure that you don't enable automatic Docker Desktop startup - it cannot be restarted with a command from what I know.
cd "$(wslpath -a "C:\Program Files\Docker\Docker")"
"./Docker Desktop.exe" &
sleep 60s

docker-compose -f /home/current/home-env/gaming/win-server/docker-compose.yml up -d
sleep 5s
docker-compose -f /home/current/home-env/gaming/win-server/docker-compose.yml up -d
sleep 5s
docker-compose -f /home/current/home-env/gaming/win-server/docker-compose.yml up -d
sleep 5s
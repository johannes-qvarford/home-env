#!/bin/bash

# Make sure that you don't enable automatic Docker Desktop startup - it cannot be restarted with a command from what I know.
cd "$(wslpath -a "C:\Program Files\Docker\Docker")"
"./Docker Desktop.exe" &
sleep 1m

for i in $(seq 10); do
    r=$(curl -o /dev/null -s -w "%{http_code}\n" http://192.168.10.101/sonarr)
    if test "$r" -eq 200; then
        echo "Server is up!"
        break
    fi
    bash -c "docker-compose -f /home/current/home-env/gaming/win-server/docker-compose.yml up -d"
    echo "Waiting for server to start..."
    sleep 1m
done
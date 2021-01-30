#!/bin/bash

# Just to be sure that docker accepts commands, wait a bit.
sleep 600

docker-compose -f /home/jq/home-env/gaming/win-server/docker-compose.yml up -d
sleep 5
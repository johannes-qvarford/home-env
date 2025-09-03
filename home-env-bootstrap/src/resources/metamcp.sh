#!/bin/bash

set -e

# Create tools directory if it doesn't exist
mkdir -p ~/tools

# Clone MetaMCP repository
cd ~/tools
if [ ! -d "metamcp" ]; then
    git clone https://github.com/metatool-ai/metamcp.git
else
    cd metamcp
    git pull
    cd ..
fi

cd metamcp

# Set up environment file
if [ ! -f ".env" ]; then
    cp example.env .env
fi

# Update docker-compose to use restart: unless-stopped for app service
sed -i 's/restart: "no"/restart: unless-stopped/' docker-compose.yml

# Start MetaMCP with Docker Compose
docker compose up -d

echo "MetaMCP installed and started at http://localhost:12005"
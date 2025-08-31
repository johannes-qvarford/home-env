#!/bin/bash

set -e

# Create tools directory if it doesn't exist
mkdir -p ~/tools

# Clone MetaMCP repository
cd ~/tools
if [ ! -d "metamcp" ]; then
    git clone https://github.com/metatool-ai/metamcp.git
fi

cd metamcp

# Set up environment file
if [ ! -f ".env" ]; then
    cp example.env .env
fi

# Start MetaMCP with Docker Compose
docker compose up -d

echo "MetaMCP installed and started at http://localhost:12005"
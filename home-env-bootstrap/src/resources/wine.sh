#!/bin/bash

set -e

echo "Installing WINE..."

# Update package list
sudo apt update

# Install WINE
sudo apt install -y wine

# Verify installation
wine --version

echo "WINE installation complete!"
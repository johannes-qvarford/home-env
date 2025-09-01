#!/bin/bash

# Enable 32-bit architecture (required for Steam)
sudo dpkg --add-architecture i386

# Update package cache
sudo apt update

# Install Steam
sudo apt install -y steam
#!/bin/bash

# Auto-mount Z: drive in WSL
if [ ! -d "/mnt/z" ]; then
    sudo mkdir -p /mnt/z
fi

# Check if Z: drive is already mounted
if ! mountpoint -q /mnt/z; then
    # Try to mount Z: drive if it exists on Windows
    if [ -d "/mnt/z" ]; then
        sudo mount -t drvfs Z: /mnt/z 2>/dev/null || echo "Z: drive not available on Windows host"
    fi
fi
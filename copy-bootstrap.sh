#!/bin/bash

# Script to copy home-env-bootstrap sibling project as a subdirectory

set -e  # Exit on any error

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SOURCE_DIR="/home/jq/home-env-bootstrap"
TARGET_DIR="$SCRIPT_DIR/home-env-bootstrap"

echo "Copying home-env-bootstrap to subdirectory..."
echo "Source: $SOURCE_DIR"
echo "Target: $TARGET_DIR"

# Check if source exists
if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: Source directory $SOURCE_DIR does not exist"
    exit 1
fi

# Check if target already exists
if [ -d "$TARGET_DIR" ]; then
    echo "Warning: Target directory $TARGET_DIR already exists"
    read -p "Do you want to remove it and continue? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Aborted"
        exit 1
    fi
    rm -rf "$TARGET_DIR"
fi

# Copy the directory
cp -r "$SOURCE_DIR" "$TARGET_DIR"

echo "Successfully copied home-env-bootstrap to $TARGET_DIR"

# Optional: Show what was copied
echo -e "\nContents of copied directory:"
ls -la "$TARGET_DIR"
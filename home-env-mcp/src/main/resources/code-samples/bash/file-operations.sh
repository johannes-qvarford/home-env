#!/bin/bash

# File operations utility script

set -e

# Create directory if it doesn't exist
create_dir_if_not_exists() {
    local dir="$1"
    if [ ! -d "$dir" ]; then
        mkdir -p "$dir"
        echo "Created directory: $dir"
    fi
}

# Backup file with timestamp
backup_file() {
    local file="$1"
    if [ -f "$file" ]; then
        local backup="${file}.backup.$(date +%Y%m%d_%H%M%S)"
        cp "$file" "$backup"
        echo "Backed up $file to $backup"
    else
        echo "File $file does not exist"
        return 1
    fi
}

# Find files by extension
find_files_by_extension() {
    local dir="$1"
    local ext="$2"
    find "$dir" -name "*.$ext" -type f
}

# Usage examples
if [ "$0" = "${BASH_SOURCE[0]}" ]; then
    create_dir_if_not_exists "./temp"
    backup_file "./important.txt"
    find_files_by_extension "." "txt"
fi
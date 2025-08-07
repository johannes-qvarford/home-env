#!/bin/bash

# Script to install git hooks for home-env-mcp

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

echo "Installing git hooks for home-env-mcp..."

# Check if we're in the right repository structure
if [ ! -d "$REPO_ROOT/home-env-mcp" ]; then
    echo "Error: home-env-mcp directory not found at $REPO_ROOT/home-env-mcp"
    exit 1
fi

if [ ! -d "$REPO_ROOT/.git" ]; then
    echo "Error: Not in a git repository"
    exit 1
fi

# Ensure hooks directory exists
mkdir -p "$REPO_ROOT/.git/hooks"

# Install the pre-commit hook
if [ -f "$REPO_ROOT/home-env-mcp/pre-commit" ]; then
    cp "$REPO_ROOT/home-env-mcp/pre-commit" "$REPO_ROOT/.git/hooks/pre-commit"
    chmod +x "$REPO_ROOT/.git/hooks/pre-commit"
    echo "✅ Pre-commit hook installed successfully"
else
    echo "❌ Pre-commit hook script not found at $REPO_ROOT/home-env-mcp/pre-commit"
    exit 1
fi

echo "Git hooks installation completed!"
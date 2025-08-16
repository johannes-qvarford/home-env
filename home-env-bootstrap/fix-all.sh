#!/bin/bash

# Fix all issues that the pre-commit hook checks
set -euo pipefail

# Check if we're in the home-env-bootstrap directory or if Cargo.toml exists in home-env-bootstrap/
if [[ -f "home-env-bootstrap/Cargo.toml" ]]; then
    cd home-env-bootstrap
elif [[ -f "Cargo.toml" && "$(basename "$PWD")" == "home-env-bootstrap" ]]; then
    # Already in the right directory
    :
else
    echo "Fix-all script: No Rust project found (home-env-bootstrap/Cargo.toml), skipping Rust fixes"
    exit 0
fi

echo "Running Rust fixes..."

# Fix formatting
echo "Fixing Rust formatting..."
cargo fmt --all

# Fix clippy issues (where possible with --fix)
echo "Fixing Rust clippy issues..."
cargo clippy --fix --allow-dirty --allow-staged

echo "All Rust fixes applied!"
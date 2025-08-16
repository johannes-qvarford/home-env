#!/bin/bash

set -euo pipefail

echo "Installing git hooks for Rust formatting and clippy checks..."

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo "Error: Not in a git repository"
    exit 1
fi

# Get the git hooks directory
HOOKS_DIR="$(git rev-parse --git-dir)/hooks"

# Create the pre-commit hook
cat > "$HOOKS_DIR/pre-commit" << 'EOF'
#!/bin/bash

# Pre-commit hook for Rust formatting and clippy checks
set -euo pipefail

# Check if we're in the home-env-bootstrap directory or if Cargo.toml exists in home-env-bootstrap/
if [[ -f "home-env-bootstrap/Cargo.toml" ]]; then
    cd home-env-bootstrap
elif [[ -f "Cargo.toml" && "$(basename "$PWD")" == "home-env-bootstrap" ]]; then
    # Already in the right directory
    :
else
    echo "Pre-commit hook: No Rust project found (home-env-bootstrap/Cargo.toml), skipping Rust checks"
    exit 0
fi

echo "Running Rust pre-commit checks..."

# Check formatting
echo "Checking Rust formatting..."
if ! cargo fmt --all -- --check; then
    echo "Error: Code is not formatted correctly. Run 'cargo fmt --all' to fix."
    exit 1
fi

# Run clippy
echo "Running Rust clippy..."
if ! cargo clippy -- -D warnings; then
    echo "Error: Clippy found issues. Please fix the warnings above."
    exit 1
fi

echo "All Rust checks passed!"
EOF

# Make the hook executable
chmod +x "$HOOKS_DIR/pre-commit"

echo "Git pre-commit hook installed successfully!"
echo "The hook will check Rust formatting and clippy on every commit."
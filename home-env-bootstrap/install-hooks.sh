#!/bin/bash

# Script to install git hooks for the bootstrap project

set -e

echo "🔧 Installing git hooks for bootstrap project..."

# Get the script's directory (should be home-env-bootstrap)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Ensure we're in the bootstrap directory
if [[ ! -f "$SCRIPT_DIR/Cargo.toml" ]]; then
    echo "❌ Error: This script must be run from the home-env-bootstrap directory"
    exit 1
fi

# Create hooks directory if it doesn't exist
mkdir -p "$SCRIPT_DIR/.git/hooks"

# Install pre-commit hook
cat > "$SCRIPT_DIR/.git/hooks/pre-commit" << 'EOF'
#!/bin/bash

# Pre-commit hook to run format check, clippy, and tests
# This hook will run the check-all.sh script before allowing commits

echo "🚀 Running pre-commit checks..."

# Get the directory of the git repository
REPO_DIR=$(git rev-parse --show-toplevel)

# Change to the home-env-bootstrap directory relative to repo root
cd "$REPO_DIR/home-env-bootstrap" || {
    echo "❌ Error: Could not find home-env-bootstrap directory"
    exit 1
}

# Run the check script
if ./check-all.sh; then
    echo "✅ Pre-commit checks passed!"
    exit 0
else
    echo "❌ Pre-commit checks failed. Please fix the issues and try again."
    echo "💡 You can run './home-env-bootstrap/check-all.sh' to see the specific issues."
    exit 1
fi
EOF

# Make the hook executable
chmod +x "$SCRIPT_DIR/.git/hooks/pre-commit"

echo "✅ Git hooks installed successfully!"
echo "🎯 The pre-commit hook will now run format checks, clippy, and tests before each commit."
echo "💡 You can manually run './check-all.sh' to test your changes at any time."
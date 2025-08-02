# Install act - Run GitHub Actions locally
# https://github.com/nektos/act

# Check if Docker is available (act requires Docker)
if ! command -v docker &> /dev/null; then
    echo "Warning: Docker is not installed. Act requires Docker to run GitHub Actions locally."
    echo "Please install Docker first using the docker task in this bootstrap app."
    exit 1
fi

# Install act using the official installer
curl --proto '=https' --tlsv1.2 -sSf https://raw.githubusercontent.com/nektos/act/master/install.sh -b ~/.local/bin | sudo bash
mkdir -p ~/.local/bin
mv ./bin/act ~/.local/bin/act

# Verify installation
if command -v act &> /dev/null; then
    echo "✓ Act installed successfully!"
fi
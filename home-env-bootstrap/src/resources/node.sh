#!/bin/bash

echo "Installing Node.js..."
curl -fsSL https://deb.nodesource.com/setup_24.x | sudo -E bash -
sudo apt install -y nodejs
npm config set prefix '~/.npm'

echo "Installing Bun..."
curl -fsSL https://bun.sh/install | bash

# Add bun to current session PATH
export PATH="$HOME/.bun/bin:$PATH"

# Verify bun installation
if command -v bun &> /dev/null; then
    echo "Bun $(bun --version) installed successfully."
else
    echo "Warning: Bun installation may have failed or is not in PATH"
fi

echo "Installing Deno..."
curl -fsSL https://deno.land/install.sh | sh

# Add deno to current session PATH
export PATH="$HOME/.deno/bin:$PATH"

# Verify deno installation
if command -v deno &> /dev/null; then
    echo "Deno $(deno --version | head -n1) installed successfully."
else
    echo "Warning: Deno installation may have failed or is not in PATH"
fi

echo "Node.js, Bun, and Deno installation completed successfully."
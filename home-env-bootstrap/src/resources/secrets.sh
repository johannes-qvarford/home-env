#!/bin/bash
# Setup secure credential files for services used in config.fish

echo "Setting up secure credential files..."

# Create LibreChat password file
if [ ! -f ~/.librechat_user_password ]; then
    echo -n "Enter LibreChat user password: "
    read -s librechat_password
    echo
    echo "$librechat_password" > ~/.librechat_user_password
    chmod 600 ~/.librechat_user_password
    echo "Created ~/.librechat_user_password with secure permissions (600)"
else
    echo "~/.librechat_user_password already exists, skipping"
fi

# Create OpenRouter API key file
if [ ! -f ~/.openrouter_key ]; then
    echo -n "Enter OpenRouter API key: "
    read -s openrouter_key
    echo
    echo "$openrouter_key" > ~/.openrouter_key
    chmod 600 ~/.openrouter_key
    echo "Created ~/.openrouter_key with secure permissions (600)"
else
    echo "~/.openrouter_key already exists, skipping"
fi

# Create DigitalOcean API token file
if [ ! -f ~/.digitalocean_token ]; then
    echo -n "Enter DigitalOcean API token: "
    read -s digitalocean_token
    echo
    echo "$digitalocean_token" > ~/.digitalocean_token
    chmod 600 ~/.digitalocean_token
    echo "Created ~/.digitalocean_token with secure permissions (600)"
else
    echo "~/.digitalocean_token already exists, skipping"
fi

echo "Credential setup complete!"
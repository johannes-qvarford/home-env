#!/bin/bash
# Setup secure credential files for LibreChat and OpenRouter

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

echo "Credential setup complete!"
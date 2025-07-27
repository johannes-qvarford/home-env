#!/bin/bash
# Setup secure .env file for services used in config.fish

# Environment variables to collect
ENV_VARS=(
    "LIBRECHAT_USER_PASSWORD"
    "OPENROUTER_KEY"
    "DIGITALOCEAN_TOKEN"
    "MOONSHOT_API_KEY"
)

echo "Setting up secure .env file..."

# Create or update .env file
ENV_FILE=~/.env
touch "$ENV_FILE"
chmod 600 "$ENV_FILE"

# Function to update or add environment variable in .env file
update_env_var() {
    local var_name="$1"
    local var_value="$2"
    
    if grep -q "^export ${var_name}=" "$ENV_FILE" || grep -q "^${var_name}=" "$ENV_FILE"; then
        # Update existing variable (handle both export and non-export formats)
        sed -i "s/^export ${var_name}=.*/export ${var_name}=${var_value}/" "$ENV_FILE"
        sed -i "s/^${var_name}=.*/export ${var_name}=${var_value}/" "$ENV_FILE"
        echo "Updated ${var_name} in .env"
    else
        # Add new variable with export
        echo "export ${var_name}=${var_value}" >> "$ENV_FILE"
        echo "Added ${var_name} to .env"
    fi
}

# Process each environment variable
for var_name in "${ENV_VARS[@]}"; do
    if ! grep -q "^export ${var_name}=" "$ENV_FILE" && ! grep -q "^${var_name}=" "$ENV_FILE"; then
        echo -n "Enter ${var_name}: "
        read -s var_value
        echo
        update_env_var "$var_name" "$var_value"
    else
        echo "${var_name} already exists in .env, skipping"
    fi
done

echo "Created ~/.env with secure permissions (600)"
echo "Environment setup complete!"
#!/bin/bash

# Set up systemd service for dotfiles sync
SERVICE_NAME="dotfiles-sync"
SERVICE_FILE="/etc/systemd/system/${SERVICE_NAME}.service"

echo "Setting up systemd service for dotfiles sync..."

# Create service file content directly
sudo tee "$SERVICE_FILE" > /dev/null << 'EOF'
[Unit]
Description=Home Environment Dotfiles Sync Service
After=network.target
Wants=network.target

[Service]
Type=simple
User=jq
Group=jq
WorkingDirectory=/home/jq/home-env
ExecStart=/usr/bin/python3 /home/jq/home-env/dotfiles/bin/sync-dirs
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF

# Set proper permissions
sudo chmod 644 "$SERVICE_FILE"

# Reload systemd to recognize the new service
sudo systemctl daemon-reload

# Enable the service to start on boot
sudo systemctl enable "$SERVICE_NAME"

# Start the service immediately
sudo systemctl start "$SERVICE_NAME"

# Show service status
echo "Service status:"
sudo systemctl status "$SERVICE_NAME" --no-pager
#!/bin/bash

set -euo pipefail

echo "Configuring Bluetooth to use USB dongle instead of motherboard Bluetooth..."

# Check if we're running as root or have sudo access
if [[ $EUID -ne 0 ]]; then
    if ! command -v sudo &> /dev/null; then
        echo "Error: This script needs root privileges and sudo is not available."
        exit 1
    fi
    SUDO="sudo"
else
    SUDO=""
fi

# Create firmware symlinks for RTL8771 USB Bluetooth dongle
echo "Setting up RTL8771 firmware symlinks..."
$SUDO ln -sf rtl8761b_fw.bin /lib/firmware/rtl_bt/rtl8771_fw.bin
$SUDO ln -sf rtl8761b_config.bin /lib/firmware/rtl_bt/rtl8771_config.bin

# Create udev rule to disable Intel Bluetooth controller to prevent conflicts
echo "Creating udev rule to disable Intel Bluetooth..."
$SUDO tee /etc/udev/rules.d/99-disable-intel-bluetooth.rules > /dev/null << 'EOF'
# Disable Intel AX201 Bluetooth to prevent conflicts with USB dongle
ACTION=="add", SUBSYSTEM=="usb", ATTR{idVendor}=="8087", ATTR{idProduct}=="0026", ATTR{authorized}="0"
EOF

# Reload udev rules
echo "Reloading udev rules..."
$SUDO udevadm control --reload-rules
$SUDO udevadm trigger

# Restart Bluetooth service to pick up changes
echo "Restarting Bluetooth service..."
$SUDO systemctl restart bluetooth || true

echo "Bluetooth configuration complete!"
echo "The USB Bluetooth dongle will be used as the primary controller."
echo "Intel motherboard Bluetooth will be disabled to prevent conflicts."
echo "Changes will persist across reboots."
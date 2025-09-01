#!/bin/bash
set -euo pipefail

echo "Setting up Android SDK..."

# Define Android SDK paths
ANDROID_HOME="$HOME/android-sdk"
CMDLINE_TOOLS_DIR="$ANDROID_HOME/cmdline-tools"
LATEST_DIR="$CMDLINE_TOOLS_DIR/latest"

# Create directory structure
mkdir -p "$CMDLINE_TOOLS_DIR"

# Download command line tools if not exists
if [ ! -d "$LATEST_DIR" ]; then
    echo "Downloading Android command line tools..."
    cd /tmp
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
    unzip -q commandlinetools-linux-11076708_latest.zip
    mv cmdline-tools "$LATEST_DIR"
    rm commandlinetools-linux-11076708_latest.zip
fi

# Set up temporary environment variables for this script
export ANDROID_HOME="$ANDROID_HOME"
export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"

# Accept licenses and install essential components
yes | sdkmanager --licenses >/dev/null 2>&1 || true
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

echo "Note: Android SDK environment variables are configured in config.fish"

echo "Android SDK setup completed!"
echo "ANDROID_HOME: $ANDROID_HOME"
echo "Available tools: adb, fastboot, sdkmanager, avdmanager"
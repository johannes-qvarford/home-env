# home-env-bootstrap

A GUI-based environment setup tool for cross-platform development automation. This tool provides an intuitive interface for installing and configuring development tools, applications, and system settings on both Windows and Linux platforms.

## Features

- **GUI-Only Interface**: Clean, user-friendly graphical interface built with egui
- **Cross-Platform**: Supports both Windows and Linux (WSL) environments
- **Task-Based System**: Modular task execution with state tracking
- **Platform-Specific Tasks**: Automatically detects platform and provides relevant setup tasks
- **Error Handling**: Comprehensive error reporting and recovery

## Quick Commands

- `./check-all.sh` - Run format check, clippy, and tests
- `./install-hooks.sh` - Install git hooks for automated checks

## Setup

### Windows
1. Download the latest release from [GitHub Releases](https://github.com/johannes-qvarford/home-env-bootstrap/releases/latest)
2. Open an admin PowerShell window in the Downloads directory
3. Run `./bootstrap.exe`
4. Follow the GUI interface to execute desired setup tasks

### Linux/WSL
1. Build from source: `cargo build --release`
2. Run: `./target/release/bootstrap`
3. Use the GUI to select and execute Linux-specific setup tasks

## Architecture

### Core Components

- **Task Registry System**: Centralized management of platform-specific tasks
- **GUI Interface**: Modern interface built with egui framework  
- **Platform Abstraction**: Automatic platform detection and task provisioning
- **State Management**: Persistent task execution tracking

### Task Categories

**Linux Tasks:**
- Development tools (Rust, Node.js, Python, Java, etc.)
- System utilities (Docker, K3s, Fish shell)
- AI/ML tools (Claude, Gemini, SGPT)
- Media and productivity tools

**Windows Tasks:**
- Package managers (Winget, Chocolatey)
- Development environments (VS Code, Windows Terminal)
- Media applications (Firefox, VLC, Steam)
- System utilities and productivity tools

## Development

### Building

```bash
# Build for Linux
cargo build --release

# Cross-compile for Windows from Linux  
rustup target add x86_64-pc-windows-gnu
sudo apt-get install mingw-w64 lld
cargo build --target x86_64-pc-windows-gnu --release
```

### Code Quality

```bash
# Format code
cargo fmt --all

# Run linter
cargo clippy -- -D warnings

# Run all checks
./check-all.sh
```

## Configuration

Task execution state is maintained in the local config directory:
- Linux: `~/.config/home-env-bootstrap/`
- Windows: `%LOCALAPPDATA%/home-env-bootstrap/`

Additional setup notes for Windows:
- Download and run pulseaudio-win32 full installer from https://pgaskin.net/pulseaudio-win32/
- May need to create inbound/outbound firewall rule for port 45789
- If winget fails to connect, try installing App Installer from Microsoft Store
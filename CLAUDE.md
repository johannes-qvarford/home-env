# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is Johannes Qvarford's personal development environment automation system. The project consists of two main components:

1. **home-env-bootstrap** - A Rust CLI tool for cross-platform environment setup (Windows + Linux/WSL)
2. **home-env** - Configuration management with dotfiles, scripts, and real-time synchronization

## Development Commands

### Rust Bootstrap Tool (home-env-bootstrap/)
```bash
# Build for Linux
cd home-env-bootstrap && cargo build --release

# Cross-compile for Windows from Linux  
cd home-env-bootstrap && cargo build --target x86_64-pc-windows-gnu --release

# Run formatting and linting
cd home-env-bootstrap && cargo fmt --all -- --check
cd home-env-bootstrap && cargo clippy -- -D warnings

# Windows debug/release shortcuts
./home-env-bootstrap/win-run-debug.sh
./home-env-bootstrap/win-run-release.sh
```

### File Synchronization
```bash
# Start the file sync daemon (watches dotfiles/ ↔ terminal/)
python3 dotfiles/bin/sync-dirs

# Manual sync configuration in .sync.json
```

### Important Conventions
- Use `docker compose` instead of `docker-compose` (as noted in dotfiles/.claude/CLAUDE.md)
- File paths and configurations assume username "Johannes Qvarford" and specific drive mappings (L, D, G, F drives)

## Architecture

### Core Components

**home-env-bootstrap/src/**: Rust-based automation engine
- `main.rs` - Task execution system with skip functionality
- `platform.rs` - Platform-specific task definitions for Windows/Linux
- `linux_tasks/` - Linux environment setup (bash, packages, tools)
- `windows_tasks/` - Windows automation (Winget, Chocolatey, WSL, scheduled tasks)
- `utility/` - HTTP requests, process management, symlink creation
- `resources/` - Shell scripts for tool installation (rust, java, docker, etc.)

**dotfiles/**: User configuration and 70+ utility scripts
- `bin/` - Custom scripts including AI tools (`claude`, `ai-shell`, `ask`), deployment (`docker-ollama`, `deploy-win-shim`), and system utilities
- `.claude/` - Claude-specific configurations
- Git hooks and various tool configurations

**Synchronization System**: Real-time bidirectional sync for dotfiles and home directory.

### Key Configuration Files
- `.sync.json` - File synchronization rules with include/exclude patterns
- `continue/config.yaml` - Continue IDE configuration with multiple AI models (Claude Sonnet 4, Opus 4, Gemini 2.5)
- `terminal/settings.json` - Windows Terminal configuration

### Task Execution Model
The bootstrap tool uses a sequential task system where each platform (Windows/Linux) has defined tasks that can be executed selectively:
```bash
./bootstrap --task <task_name>  # Run specific task
./bootstrap --skip <number>     # Skip initial N tasks
```

Tasks include package management, tool installation, configuration deployment, and system integration setup.

### Cross-Platform Considerations
- Windows tasks handle Winget, Chocolatey, WSL installation, Windows Terminal integration
- Linux tasks manage apt packages, development tools, shell configuration
- Shared utilities handle HTTP downloads, process execution, and symlink creation across platforms
- Build system supports cross-compilation from Linux to Windows using mingw-w64
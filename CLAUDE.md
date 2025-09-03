# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is Johannes Qvarford's personal development environment automation system. The project consists of two main components:

1. **home-env-bootstrap** - A Rust GUI application for cross-platform environment setup (Windows + Linux/WSL)
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

The file synchronization system provides real-time bidirectional sync between dotfiles and their target locations using:

1. **`.sync.json`** - Configuration file defining sync pairs with support for:
   - File-to-file syncing (`file1`/`file2`)
   - Directory-to-directory syncing (`dir1`/`dir2`)
   - One-way sync (`"one_way": true`)
   - Regex-based include/exclude filtering (`include_regex`/`exclude_regex`)
   - Debounce timing (`debounce_ms`: 1000ms default)

2. **`sync-dirs` script** - Python daemon that:
   - Uses watchdog library for filesystem monitoring
   - Performs initial sync on startup (newest file wins for bidirectional, source->target for one-way)
   - Watches configured directories for changes and syncs immediately
   - Supports hot-reloading when `.sync.json` is modified
   - Filters temporary files (`.temp.digits.digits` pattern)

3. **`dotfiles-sync.service`** - System-level systemd service (not user service):
   - Runs as user `jq` from `/home/jq/home-env` working directory
   - Auto-starts on boot, restarts on failure
   - Logs to systemd journal
   - Enabled in `/etc/systemd/system/dotfiles-sync.service`

**Best Practice**: Always edit configuration files in the `dotfiles/` directory rather than their target locations (e.g., edit `dotfiles/.tmux.conf` not `/home/jq/.tmux.conf`). The sync system will automatically propagate changes to all configured locations, ensuring consistency across environments.

**Important**: When creating new configuration files in `dotfiles/`, ensure they are registered in `.sync.json` if not already covered by existing directory rules. Files without sync rules will remain isolated in the dotfiles directory.

### Important Conventions
- Use `docker compose` instead of `docker-compose` (as noted in dotfiles/.claude/CLAUDE.md)
- File paths and configurations assume username "Johannes Qvarford" and specific drive mappings (L, D, G, F drives)

## Architecture

### Core Components

**home-env-bootstrap/src/**: Rust-based GUI automation engine
- `main.rs` - GUI application entry point and initialization
- `task_registry.rs` - Centralized task management and platform abstraction
- `gui/` - GUI components built with egui framework
- `linux_tasks/` - Linux environment setup (bash scripts, development tools)
- `windows_tasks/` - Windows automation (Winget, Chocolatey, WSL, scheduled tasks)
- `utility/` - HTTP requests, process management, symlink creation, task state tracking
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
The bootstrap tool provides a GUI interface for task execution with automatic platform detection:
- Tasks are organized by platform (Windows/Linux) and automatically provided based on the current system
- Each task can be executed individually through the GUI interface
- Task state is persistently tracked to avoid re-running completed tasks
- Task categories include package management, tool installation, configuration deployment, and system integration setup

### Cross-Platform Considerations
- Windows tasks handle Winget, Chocolatey, WSL installation, Windows Terminal integration
- Linux tasks manage apt packages, development tools, shell configuration
- Shared utilities handle HTTP downloads, process execution, and symlink creation across platforms
- Build system supports cross-compilation from Linux to Windows using mingw-w64
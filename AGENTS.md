# AGENTS.md

## Build, Lint, and Test Commands

### Rust Bootstrap Tool (home-env-bootstrap/)
```bash
# Build for Linux
cd home-env-bootstrap && cargo build --release

# Cross-compile for Windows from Linux  
cd home-env-bootstrap && cargo build --target x86_64-pc-windows-gnu --release

# Run formatting and linting
cd home-env-bootstrap && cargo fmt --all -- --check
cd home-env-bootstrap && cargo clippy -- -D warnings

# Run tests
cd home-env-bootstrap && cargo test

# Run a single test
cd home-env-bootstrap && cargo test <test_name>
```

### Python Scripts & Tools
```bash
# Type check Python scripts
python3 -m mypy dotfiles/bin/

# Format Python code
python3 -m black dotfiles/bin/

# Lint Python code
python3 -m flake8 dotfiles/bin/
```

## Code Style Guidelines

### Rust
- **Imports**: Group std, external crates, then local modules
- **Formatting**: Use `cargo fmt` consistently
- **Types**: Always specify types for parameters and returns
- **Naming**: `snake_case` for vars/functions, `CamelCase` for types
- **Error Handling**: Use `Result<T, E>` and handle gracefully

### Python
- **Types**: Add type hints to all functions
- **Formatting**: Use black for consistent formatting
- **Naming**: `snake_case` for vars/functions, `PascalCase` for classes
- **Error Handling**: Use try/except with specific exception types

### Shell Scripts
- **Shebang**: Always include `#!/bin/bash` or `#!/usr/bin/env bash`
- **Quoting**: Always quote variables: `"$var"`
- **Error Handling**: Use `set -euo pipefail` at script start
- **Naming**: Use descriptive names with `.sh` extension

## Project Structure

### Core Components
- **home-env-bootstrap/**: Rust CLI for cross-platform setup
- **dotfiles/**: User configurations and 70+ utility scripts
- **terminal/**: Windows Terminal settings
- **continue/**: Continue IDE configuration
- **schedule/**: Cron jobs for backup/upgrade tasks

### Key Files
- `.sync.json`: File synchronization rules
- `bootstrap`: Main executable (Windows/Linux)
- `dotfiles/bin/`: Custom scripts (AI tools, deployment, utilities)

### Development Workflow
1. Make changes in appropriate component
2. Run linting/formatting for changed language
3. Test changes (cargo test for Rust, manual for scripts)
4. Use file sync daemon for dotfile changes
5. Cross-compile Windows version from Linux when needed

## Git Workflow

### Commit Guidelines
- Write clear, concise commit messages that explain the "why" not just the "what"
- Use present tense ("Add feature" not "Added feature")
- Keep commits focused on a single logical change
- Run linting before committing

### Commit Commands
```bash
# Stage specific files
git add <file1> <file2>

# Commit with message
git commit -m "Add feature X to improve Y

- Details about implementation
- Reasoning behind changes

🤖 Generated with [opencode](https://opencode.ai)

Co-Authored-By: opencode <noreply@opencode.ai>"
```
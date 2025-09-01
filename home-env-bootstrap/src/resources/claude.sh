#!/bin/bash

# Install Claude Code using the beta binary installer
curl -fsSL https://claude.ai/install.sh | bash

# Install claude-code-router via npm (still needed)
npm install -g @musistudio/claude-code-router
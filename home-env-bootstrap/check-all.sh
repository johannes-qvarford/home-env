#!/bin/bash

# Exit on any error
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}🧹 Running format check...${NC}"
if cargo fmt --all -- --check; then
    echo -e "${GREEN}✅ Format check passed${NC}"
else
    echo -e "${RED}❌ Format check failed. Run 'cargo fmt --all' to fix${NC}"
    exit 1
fi

echo -e "${YELLOW}🔍 Running clippy...${NC}"
if cargo clippy -- -D warnings; then
    echo -e "${GREEN}✅ Clippy passed${NC}"
else
    echo -e "${RED}❌ Clippy failed${NC}"
    exit 1
fi

echo -e "${YELLOW}🧪 Running tests...${NC}"
if cargo test; then
    echo -e "${GREEN}✅ Tests passed${NC}"
else
    echo -e "${RED}❌ Tests failed${NC}"
    exit 1
fi

echo -e "${GREEN}🎉 All checks passed!${NC}"
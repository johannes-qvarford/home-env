#!/bin/bash

# Common Git workflow operations

set -e

# Create and switch to new branch
create_branch() {
    local branch_name="$1"
    git checkout -b "$branch_name"
    echo "Created and switched to branch: $branch_name"
}

# Safe commit with checks
safe_commit() {
    local message="$1"
    
    # Check for staged changes
    if ! git diff --staged --quiet; then
        git commit -m "$message"
        echo "Committed changes: $message"
    else
        echo "No staged changes to commit"
        return 1
    fi
}

# Update branch from main
update_from_main() {
    local current_branch=$(git branch --show-current)
    git checkout main
    git pull origin main
    git checkout "$current_branch"
    git rebase main
    echo "Updated $current_branch with latest main"
}

# Clean up merged branches
cleanup_merged_branches() {
    git branch --merged main | grep -v "main\|master" | xargs -n 1 git branch -d
    echo "Cleaned up merged branches"
}

# Usage
case "${1:-}" in
    "branch")
        create_branch "$2"
        ;;
    "commit")
        safe_commit "$2"
        ;;
    "update")
        update_from_main
        ;;
    "cleanup")
        cleanup_merged_branches
        ;;
    *)
        echo "Usage: $0 {branch|commit|update|cleanup} [args...]"
        exit 1
        ;;
esac
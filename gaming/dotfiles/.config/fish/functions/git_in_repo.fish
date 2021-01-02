function git_in_repo
	[ -d .git ]; or git rev-parse --git-dir >/dev/null 2>&1
end
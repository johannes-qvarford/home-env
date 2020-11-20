function git_dir
	git rev-parse --git-dir >/dev/null; and git rev-parse --git-dir; or false;
end
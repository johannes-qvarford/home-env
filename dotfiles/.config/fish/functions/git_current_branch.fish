function git_current_branch
	cat (git_dir)/HEAD | sed 's:.*/::'
end
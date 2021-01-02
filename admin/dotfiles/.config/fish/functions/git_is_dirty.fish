function git_is_dirty
	git status -s | read line
end
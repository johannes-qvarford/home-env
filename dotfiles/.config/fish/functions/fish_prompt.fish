function fish_prompt -d "Write out the prompt"
	printf '%s %s %s' \
		(set_color $fish_color_cwd; prompt_pwd) \
		(branch) \
		(set_color $fish_color_normal; printf "> ")
end

function branch
	if git_in_repo
		print_branch git
	end
end

function print_branch
	set -l repo_type $argv[1]
	set -l branch (eval {$repo_type}_current_branch ^/dev/null)

	set_color --bold $color_repo_type
	printf "$repo_type"
	set_color $color_repo_paren
	printf "("
	set_color $color_repo_branch
	printf "$branch"
	set_color $color_repo_paren
	printf ")"
	if eval {$repo_type}_is_dirty ^/dev/null
		set_color $color_repo_dirty
		printf "X"
	end
end	

function print_bind_mode
	if test $fish_bind_mode = "insert"
		set_color -b black normal
		printf "[I]"
	else
		set_color -b red black
		printf "[N]"
	end
	set_color -b black normal
end

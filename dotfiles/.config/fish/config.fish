set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less

set -gx PATH $HOME/bin $HOME/.local/bin /usr/local/bin /usr/bin /bin / $PATH 
set -gx fish_function_path $HOME/.config/fish/functions $fish_function_path

remove_duplicates_in_array PATH
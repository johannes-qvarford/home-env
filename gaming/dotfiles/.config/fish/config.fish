set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -x PYTHONIOENCODING utf-8
set -x EDITOR vscode
set -x VISUAL vscode
set -x PAGER less

set -gx PATH $HOME/bin /usr/local/bin /usr/bin /bin / $PATH 
set -gx fish_function_path $HOME/.config/fish/functions $fish_function_path

remove_duplicates_in_array PATH

# Created by `userpath` on 2021-01-16 12:36:08
set PATH $PATH /home/jq/.local/bin

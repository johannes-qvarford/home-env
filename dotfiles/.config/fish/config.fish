set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less

fish_add_path $HOME/.local/bin
fish_add_path $HOME/bin
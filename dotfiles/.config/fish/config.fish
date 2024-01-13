set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less

set -gx PATH $HOME/bin $HOME/.local/share/JetBrains/Toolbox/scripts $HOME/.local/bin $HOME/.cargo/bin /usr/local/bin /usr/bin /bin / $PATH 

# /home/jq/.config/fish/functions /home/jq/.local/share/fish/vendor_functions.d /usr/share/fish/vendor_functions.d /usr/share/fish/functions /etc/fish/functions 
#set -gx fish_function_path $HOME/.config/fish/functions $fish_function_path
set -gx fish_function_path ~/.config/fish/functions ~/.local/share/fish/vendor_functions.d /usr/share/fish/vendor_functions.d /usr/share/fish/functions /etc/fish/functions 

remove_duplicates_in_array PATH
remove_duplicates_in_array fish_function_path
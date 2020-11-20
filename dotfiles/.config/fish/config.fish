set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -x PYTHONIOENCODING utf-8
set -x EDITOR vim
set -x VISUAL vim
set -x PAGER less

set -gx PATH $HOME/bin $HOME/.cargo/bin /usr/local/bin /usr/bin /bin / $PATH 
set -gx fish_function_path $HOME/.config/fish/functions $fish_function_path
set -gx MAVEN_OPTS '-Dmaven.wagon.http.ssl.insecure=true'

remove_duplicates_in_array PATH

set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less

if test "X$has_expanded_path" = "X"
    set -gpx PATH $HOME/.cargo/bin
    set -gpx PATH $HOME/.local/bin
    set -gpx PATH $HOME/.local/share/JetBrains/Toolbox/scripts
    set -gpx PATH $HOME/bin
    set -gpx fish_function_path ~/.config/fish/functions
    set -gx has_expanded_path true
end
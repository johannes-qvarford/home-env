set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less
set -gx PULSE_SERVER unix:/mnt/wslg/PulseServer
set -gx OPENAI_API_KEY sk-af18da76518d4847bac595061c86109c

fish_add_path $HOME/.cargo/bin
fish_add_path $HOME/.local/bin
fish_add_path $HOME/.local/share/JetBrains/Toolbox/scripts
fish_add_path $HOME/bin
fish_add_path $HOME/projects/vcpkg
set -x CMAKE_TOOLCHAIN_FILE $HOME/projects/vcpkg/scripts/buildsystems/vcpkg.cmake
alias claude="/home/jq/.claude/local/claude"

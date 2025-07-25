set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less
set -gx PULSE_SERVER unix:/mnt/wslg/PulseServer
set -x OPENROUTER_KEY (cat ~/.openrouter_key)
set -x LIBRECHAT_USER_PASSWORD (cat ~/.librechat_user_password)
set -x DIGITALOCEAN_TOKEN (cat ~/.digitalocean_token)
set -gx OPENAI_API_KEY $OPENROUTER_KEY
#set -x KUBECONFIG ~/.kube/config

fish_add_path $HOME/.npm/bin
fish_add_path $HOME/.cargo/bin
fish_add_path $HOME/.local/bin
fish_add_path $HOME/.local/share/JetBrains/Toolbox/scripts
fish_add_path $HOME/projects/vcpkg
fish_add_path $HOME/.opencode/bin
fish_add_path $HOME/bin

set -x CMAKE_TOOLCHAIN_FILE $HOME/projects/vcpkg/scripts/buildsystems/vcpkg.cmake

# opencode

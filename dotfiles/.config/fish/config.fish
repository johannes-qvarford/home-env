set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less
set -gx PULSE_SERVER unix:/mnt/wslg/PulseServer
# Load environment variables from .env file using bass
if test -f ~/.env
    bass source ~/.env
end
set -gx OPENAI_API_KEY $OPENROUTER_KEY
#set -x KUBECONFIG ~/.kube/config

# Android SDK
set -gx ANDROID_HOME ~/android-sdk

fish_add_path $HOME/.npm/bin
fish_add_path $HOME/.cargo/bin
fish_add_path $HOME/.local/bin
fish_add_path $HOME/.local/share/JetBrains/Toolbox/scripts
fish_add_path $HOME/projects/vcpkg
fish_add_path $HOME/.opencode/bin
fish_add_path $HOME/bin
fish_add_path $ANDROID_HOME/platform-tools
fish_add_path $ANDROID_HOME/cmdline-tools/latest/bin


set -x CMAKE_TOOLCHAIN_FILE $HOME/projects/vcpkg/scripts/buildsystems/vcpkg.cmake



# opencode

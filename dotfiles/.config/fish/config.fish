set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "code"
set -gx VISUAL "code"
set -gx PAGER less
set -gx PULSE_SERVER unix:/mnt/wslg/PulseServer
set -gx SUDO_ASKPASS "$HOME/bin/askpass"
# Load environment variables from .env file using bass
if test -f ~/.env
    bass source ~/.env
end
set -gx OPENAI_API_KEY $OPENROUTER_KEY
# Android SDK
set -gx ANDROID_HOME ~/android-sdk

fish_add_path $HOME/.npm/bin
fish_add_path $HOME/.cargo/bin
fish_add_path $HOME/.local/bin
fish_add_path $HOME/.local/share/JetBrains/Toolbox/scripts
fish_add_path $HOME/bin
fish_add_path $ANDROID_HOME/platform-tools
fish_add_path $ANDROID_HOME/cmdline-tools/latest/bin

# Auto-mount Z: drive in WSL
if not mountpoint -q /mnt/z 2>/dev/null
    if test -d /mnt/z; or sudo mkdir -p /mnt/z 2>/dev/null
        sudo mount -t drvfs Z: /mnt/z 2>/dev/null
    end
end

set color_repo_type blue
set color_repo_paren purple
set color_repo_branch cyan
set color_repo_dirty yellow

set -gx PYTHONIOENCODING utf-8
set -gx EDITOR "emacs -nw"
set -gx VISUAL "emacs -nw"
set -gx PAGER less

set -gx PATH $HOME/bin $HOME/.local/bin /usr/local/bin /usr/bin /bin / $PATH 
set -gx fish_function_path $HOME/.config/fish/functions $fish_function_path

remove_duplicates_in_array PATH

set -gx FZF_FIND_FILE_COMMAND "
    command find -L \$dir -mindepth 1 \\( -path \$dir'*/\\.*(?=config)' -o -path './winhome/*' -o -fstype 'sysfs' -o -fstype 'devfs' -o -fstype 'devtmpfs' \\) -prune \
    -o -type f -print \
    -o -type d -print \
    -o -type l -print 2> /dev/null | sed 's@^\./@@'"

set -gx FZF_CD_COMMAND "
    command find -L \$dir -mindepth 1 \\( -path \$dir'*/\\.*(?=config)' -o -path './winhome/*' -o -fstype 'sysfs' -o -fstype 'devfs' -o -fstype 'devtmpfs' \\) -prune \
    -o -type d -print 2> /dev/null | sed 's@^\./@@'"

set -gx FZF_CD_WITH_HIDDEN_COMMAND "
    command find -L \$dir \
    \\( -path '*/\\.git*' -o -path './winhome/*' -o -fstype 'dev' -o -fstype 'proc' \\) -prune \
    -o -type d -print 2> /dev/null | sed 1d | cut -b3-"

set -gx FZF_OPEN_COMMAND "
    command find -L \$dir -mindepth 1 \\( -path \$dir'*/\\.*(?=config)' -o -path './winhome/*' -o -fstype 'sysfs' -o -fstype 'devfs' -o -fstype 'devtmpfs' \\) -prune \
    -o -type f -print \
    -o -type d -print \
    -o -type l -print 2> /dev/null | sed 's@^\./@@'"

# May be needed for gwsl.
# TODO: Remove when proper graphical support comes to wsl.
set --export WSL2 1
set ipconfig_exec (wslpath "C:\\Windows\\System32\\ipconfig.exe")
if which ipconfig.exe >/dev/null
    set ipconfig_exec (which ipconfig.exe)
end

set wsl2_d_tmp (eval $ipconfig_exec | grep -n -m 1 "Default Gateway.*: [0-9a-z]" | cut -d : -f 1)
if test -n "$wsl2_d_tmp"
    set first_line (expr $wsl2_d_tmp - 4)
    set wsl2_d_tmp (eval $ipconfig_exec | sed $first_line,$wsl2_d_tmp!d | grep IPv4 | cut -d : -f 2 | sed -e "s|\s||g" -e "s|\r||g")
    set --export DISPLAY "$wsl2_d_tmp:0"
    set -e first_line
else
    set --export DISPLAY (cat /etc/resolv.conf | grep nameserver | awk '{print $2}'):0
end

set -e wsl2_d_tmp
set -e ipconfig_exec
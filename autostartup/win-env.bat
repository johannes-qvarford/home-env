%echo off
echo Waiting a bit before attempting to start wsl script.
timeout 10
C:\WINDOWS\system32\wsl.exe "/home/jq/win-env/autostartup/win-env.sh"

%echo off
echo Waiting a bit before attempting to start wsl script.
timeout 600
C:\WINDOWS\system32\wsl.exe "/home/jq/home-env/gaming/autostartup/home-env.sh"

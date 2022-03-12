%echo off
echo Waiting a bit before attempting to start wsl script.
timeout 60
C:\WINDOWS\system32\wsl.exe -d fedoraremix "/home/jq/home-env/autostartup/home-env.sh"

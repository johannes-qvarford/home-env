$exePath="~/Downloads/wsl_update_x64.msi"
Invoke-WebRequest https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi -OutFile $exePath 
Invoke-Expression ($exePath)

wsl --set-default-version 2

choco install -y wsl-fedoraremix vcxsrv
fedoraremix.exe run 'useradd johqva -g wheel; passwd johqva;'
fedoraremix.exe config --username johqva

$tempDir = [System.IO.Path]::GetTempPath();
Invoke-WebRequest "https://gitlab.com/johannes-q/home-env/-/archive/master/home-env-master.zip" -OutFile "~/Downloads/home-env-master.zip";
Expand-Archive "~/Downloads/home-env-master.zip" -DestinationPath  "$tempDir"
Set-Location "$tempDir/home-env-master/gaming"
fedoraremix.exe run './bootstrap-wsl.sh'

Copy-Item autostartup/home-env.bat "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp"

Set-Location ~

Stop-Service sshd
Start-Service sshd
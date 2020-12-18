$exePath="~/Downloads/wsl_update_x64.msi"
Invoke-WebRequest https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi -OutFile $exePath 
Invoke-Expression ($exePath)

wsl --set-default-version 2

choco install -y wsl-fedoraremix vcxsrv
fedoraremix.exe run 'useradd jq -g wheel; passwd jq;'
fedoraremix.exe config --username jq

$tempDir = [System.IO.Path]::GetTempPath();
Invoke-WebRequest "https://gitlab.com/johannes-q/win-env/-/archive/master/win-env-master.zip" -OutFile "~/Downloads/win-env-master.zip";
Expand-Archive "~/Downloads/win-env-master.zip" -DestinationPath  "$tempDir"
Set-Location "$tempDir/win-env-master"
fedoraremix.exe run './bootstrap-wsl.sh'
Copy-Item autostartup/win-env.bat "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp"
Copy-Item autostartup/win-env.sh "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp"

Set-Location ~


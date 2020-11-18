$tempDir = [System.IO.Path]::GetTempPath();
Invoke-WebRequest "https://gitlab.com/johannes-q/win-env/-/archive/master/win-env-master.zip" -OutFile "~/Downloads/win-env-master.zip";
Expand-Archive "~/Downloads/win-env-master.zip" -DestinationPath  "$tempDir"
Move-Item "$tempDir/win-env-master" "~/win-env"

[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072;
Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'));

choco install -y microsoft-windows-terminal --pre 
choco install -y dropdown vscode keepass megasync brave

dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
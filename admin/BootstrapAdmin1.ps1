[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072;
Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'));

choco install -y microsoft-windows-terminal --pre 
choco install -y dropbox vscode keepass brave teamviewer docker-desktop megatools

dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

mkdir "C:\Program Files\OpenSSH\"
Invoke-WebRequest "https://github.com/PowerShell/Win32-OpenSSH/releases/download/v8.1.0.0p1-Beta/OpenSSH-Win64.zip" -OutFile "C:\Program Files\OpenSSH\OpenSSH-Win64.zip";
Expand-Archive "C:\Program Files\OpenSSH\OpenSSH-Win64.zip" -DestinationPath  "C:\Program Files\OpenSSH\"
Set-Location "C:\Program Files\OpenSSH\OpenSSH-Win64"
powershell.exe -ExecutionPolicy Bypass -File install-sshd.ps1

Add-WindowsCapability -Online -Name OpenSSH.Server~~~~0.0.1.0
Set-Service -Name sshd -StartupType 'Automatic'
New-ItemProperty -Path "HKLM:\SOFTWARE\OpenSSH" -Name DefaultShell -Value "C:\WINDOWS\System32\bash.exe" -PropertyType String -Force
Get-Service -Name ssh-agent | Set-Service -StartupType Automatic
Start-Service sshd
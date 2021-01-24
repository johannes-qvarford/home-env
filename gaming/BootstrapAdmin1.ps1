[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072;
Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'));

choco install -y microsoft-windows-terminal --pre 
choco install -y dropbox vscode keepass megasync firefox steam kodi teamviewer itch docker-desktop megatools vlc cpod bleachbit jackett radarr  stretchly
# sonarr has to be installed manually, since chocolatey don't support v3

dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

Add-WindowsCapability -Online -Name OpenSSH.Server~~~~0.0.1.0
Set-Service -Name sshd -StartupType 'Automatic'
New-ItemProperty -Path "HKLM:\SOFTWARE\OpenSSH" -Name DefaultShell -Value "C:\WINDOWS\System32\bash.exe" -PropertyType String -Force
Get-Service -Name ssh-agent | Set-Service -StartupType Automatic
Start-Service sshd

function Register-Weekly-Backup {
    param (
        $Name,
        $At
    )

    $trigger =  New-ScheduledTaskTrigger -Weekly -DaysOfWeek Sunday -At $At
    $action = New-ScheduledTaskAction -Execute 'C:\WINDOWS\system32\wsl.exe' -Argument "/home/jq/home-env/gaming/backup/backup-$Name"
    Register-ScheduledTask -Action $action -Trigger $trigger -TaskName "Backup $Name" -Description "Backup $Name"  -TaskPath "\Backups\"
}

Register-Weekly-Backup -Name qbittorrent -At "11:00 am"
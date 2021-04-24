[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072;
Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'));

choco feature enable -n allowGlobalConfirmation
choco install microsoft-windows-terminal --pre 
choco install dropbox vscode keepass megasync firefox steam kodi teamviewer itch docker-desktop megatools vlc bleachbit sqlitebrowser stretchly wireshark handbrake fiddler
# sonarr has to be installed manually, since chocolatey don't support v3

dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

Add-WindowsCapability -Online -Name OpenSSH.Server~~~~0.0.1.0
Set-Service -Name sshd -StartupType 'Automatic'
New-ItemProperty -Path "HKLM:\SOFTWARE\OpenSSH" -Name DefaultShell -Value "C:\WINDOWS\System32\bash.exe" -PropertyType String -Force
Get-Service -Name ssh-agent | Set-Service -StartupType Automatic
Start-Service sshd

function Register-Weekly-Task {
    param (
        $Name,
        $At,
        $Type
    )

    $trigger =  New-ScheduledTaskTrigger -Weekly -DaysOfWeek Sunday -At $At
    $action = New-ScheduledTaskAction -Execute 'C:\WINDOWS\system32\wsl.exe' -Argument "/home/jq/home-env/gaming/schedule/$Name"
    if ($Type -eq "pwsh") {
        $action = New-ScheduledTaskAction -Execute 'C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe' -Argument "\\wsl$\fedoraremix\home\jq\home-env\gaming\schedule\$Name.ps1"
    }
    Register-ScheduledTask -Action $action -Trigger $trigger -TaskName "$Name" -Description "$Name"  -TaskPath "\Schedule\"
}

Register-Weekly-Task -Name backup-media -Type wsl -At "11:00 am"
Register-Weekly-Task -Name shred-data -Type wsl -At "10:00 am"
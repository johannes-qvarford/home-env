[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072;
Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'));

choco feature enable -n allowGlobalConfirmation
choco install microsoft-windows-terminal --pre 
choco install dropbox vscode keepass megasync firefox brave steam kodi teamviewer itch docker-desktop megatools vlc bleachbit sqlitebrowser stretchly wireshark handbrake fiddler gsudo microsoft-windows-terminal autohotkey

dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

$modulePath = [Environment]::GetEnvironmentVariable('PSModulePath', 'User')
$modulePath += ';\\wsl$\fedoraremix\home\johqva\home-env\gaming\powershell'
[Environment]::SetEnvironmentVariable('PSModulePath', $modulePath, 'User')

function Register-Weekly-Task {
    param (
        $Name,
        $At,
        $Type
    )

    $trigger =  Trigger-Sunday -At $At
    $action = WSL-Task -Name $Name
    if ($Type -eq "pwsh") {
        $action = Powershell-Task -Name $Name
    }
    Register -Action $action -Trigger $trigger -Name "$Name"
}

function WSL-Task {
    param ($Name)
    return New-ScheduledTaskAction -Execute 'C:\WINDOWS\system32\wsl.exe' -Argument "-d fedoraremix /home/current/home-env/gaming/schedule/$Name"
}

function Powershell-Task {
    param ($Name)
    return New-ScheduledTaskAction -Execute 'C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe' -Argument "\\wsl$\fedoraremix\home\current\home-env\gaming\schedule\$Name.ps1"
}

function Trigger-Sunday {
    param($At)
    return New-ScheduledTaskTrigger -Weekly -DaysOfWeek Sunday -At $At
}

function Register {
    param (
        $Action,
        $Trigger,
        $Name
    )
    Register-ScheduledTask -Action $Action -Trigger $Trigger -TaskName "$Name" -Description "$Name"  -TaskPath "\Schedule\"
}

Register-Weekly-Task -Name backup-media -Type wsl -At "11:00 am"
Register-Weekly-Task -Name shred-data -Type wsl -At "10:00 am"
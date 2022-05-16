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
    return New-ScheduledTaskAction -Execute 'C:\WINDOWS\system32\wsl.exe' -Argument "/home/jq/home-env/schedule/$Name"
}

function Powershell-Task {
    param ($Name)
    return New-ScheduledTaskAction -Execute 'C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe' -Argument "\\wsl$\Ubuntu\home\jq\home-env\schedule\$Name.ps1"
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
Register-Weekly-Task -Name Shred-Data -Type pwsh -At "10:00 am"
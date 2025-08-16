# Create scheduled task for auto-attach at startup
Write-Host "Creating scheduled task for auto-attach..."
$taskName = "AutoAttachDevDrive"
$action = New-ScheduledTaskAction -Execute "powershell.exe" -Argument "-WindowStyle Hidden -Command `"if (-not (Test-Path 'Z:\')) { `$diskpartScript = @'`ncreate vdisk file=`"`$env:USERPROFILE\DevDrive.vhdx`"`nselect vdisk file=`"`$env:USERPROFILE\DevDrive.vhdx`"`nattach vdisk`nassign letter=Z`nexit`n'@; `$scriptFile = `"`$env:TEMP\attach_dev_drive.txt`"; `$diskpartScript | Out-File -FilePath `$scriptFile -Encoding ASCII; Start-Process -FilePath 'diskpart.exe' -ArgumentList '/s `"`$scriptFile`"' -Wait -WindowStyle Hidden; Remove-Item -Path `$scriptFile -Force -ErrorAction SilentlyContinue }`""
$trigger = New-ScheduledTaskTrigger -AtStartup
$principal = New-ScheduledTaskPrincipal -UserId "SYSTEM" -LogonType ServiceAccount -RunLevel Highest
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries -StartWhenAvailable

try {
    Unregister-ScheduledTask -TaskName $taskName -Confirm:$false -ErrorAction SilentlyContinue
    Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Principal $principal -Settings $settings -Force
    Write-Host "Scheduled task created for auto-attach at startup"
} catch {
    Write-Warning "Failed to create scheduled task: $_"
}
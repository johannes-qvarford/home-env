# Auto-attach DEV drive VHD at startup
if (-not (Test-Path "Z:\")) {
    $vhdPath = "$env:USERPROFILE\DevDrive.vhdx"
    if (Test-Path $vhdPath) {
        $diskpartScript = @"
select vdisk file="$vhdPath"
attach vdisk
assign letter=Z
exit
"@
        $scriptFile = "$env:TEMP\attach_dev_drive.txt"
        $diskpartScript | Out-File -FilePath $scriptFile -Encoding ASCII
        Start-Process -FilePath "diskpart.exe" -ArgumentList "/s `"$scriptFile`"" -Wait -WindowStyle Hidden
        Remove-Item -Path $scriptFile -Force -ErrorAction SilentlyContinue
    }
}
# PowerShell script to play a random MP3 alert sound using VLC

# Path to alerts directory
$AlertsPath = "C:\Users\deffo\Proton Drive\deffon.d\My files\home\media\audio\alerts"

try {
    # Check if directory exists
    if (-not (Test-Path $AlertsPath)) {
        Write-Host "Alerts directory not found: $AlertsPath"
        exit 1
    }

    # Get all MP3 files in the directory
    $Mp3Files = Get-ChildItem -Path $AlertsPath -Filter "*.mp3" -File

    if ($Mp3Files.Count -eq 0) {
        Write-Host "No MP3 files found in $AlertsPath"
        exit 1
    }

    # Select a random MP3 file
    $RandomFile = $Mp3Files | Get-Random
    Write-Host "Playing: $($RandomFile.Name)"
    
    # Use VLC to play the file and exit with boosted volume
    & "C:\Program Files\VideoLAN\VLC\vlc.exe" --play-and-exit --intf dummy --gain 6 "$($RandomFile.FullName)"
    
    Write-Host "Finished playing alert sound"
}
catch {
    Write-Host "Error playing alert sound: $_"
    exit 1
}
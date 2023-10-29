# TODO: Create 10GB drive on Z: programmatically.

function Move-AppData {
    param (
        $Directory
    )
    $SourceDirectory = "$env:HOMEPATH\AppData\$Directory"
    $Item = Get-Item $SourceDirectory
    $LinkExists = $Item.Attributes -band [System.IO.FileAttributes]::ReparsePoint

    if ($LinkExists) {
        Write-Output "Link already exists for $SourceDirectory"
        return
    }

    $DestinationDirectory = "Z:\$Directory"
    Move-Item -Path "$SourceDirectory" -Destination "$DestinationDirectory"
    New-Item -Path "$SourceDirectory" -ItemType SymbolicLink -Value "$DestinationDirectory"
}

New-Item -ItemType Directory Z:\Secrets
New-Item -ItemType Directory Z:\Local
New-Item -ItemType Directory Z:\Roaming

Move-AppData -Directory "Roaming\Mozilla"
Move-AppData -Directory "Roaming\Bitwarden"
Move-AppData -Directory "Local\Mozilla"
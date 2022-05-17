$packages = "Microsoft.WindowsTerminal", "Mozilla.Firefox", "Microsoft Visual Studio Code", `
    "Brave", "Dropbox", "Keepass", "MEGAsync", "Valve.Steam",  "XBMCFoundation.Kodi", "TeamViewer.TeamViewer", `
     "Docker Desktop", "VLC media player", "BleachBit", "DBBrowserForSQLite.DBBrowserForSQLite", `
     "Wireshark", "HandBrake", "Telerik.Fiddler.Classic", "gsudo", "AutoHotkey", "ProtonVPN", `
     "Messenger", "SlackTechnologies.Slack", "Stretchly.Stretchly" `
;

foreach ($package in $packages)
{
  winget install $package
}

Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# winget is being as I'm writing this, convert these to winget once it settles down.
choco feature enable -n=allowGlobalConfirmation
choco install bleachbit
choco install 7zip
choco install tartube
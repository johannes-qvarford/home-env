$packages = "Microsoft.WindowsTerminal", "Mozilla.Firefox", "Microsoft.VisualStudioCode", `
    "Brave.Brave", "Dropbox.Dropbox", "DominikReichl.KeePass", "Mega.MEGASync", "Valve.Steam",  "XBMCFoundation.Kodi", "TeamViewer.TeamViewer", `
    "VideoLAN.VLC", "DBBrowserForSQLite.DBBrowserForSQLite", `
    "WiresharkFoundation.Wireshark", "HandBrake.HandBrake", "Telerik.Fiddler.Classic", "gerardog.gsudo", "Lexikos.AutoHotkey", "ProtonTechnologies.ProtonVPN", `
    "Messenger", "SlackTechnologies.Slack", "Stretchly.Stretchly", "Oracle.VirtualBox", "WinDirStat.WinDirStat", `
    "BleachBit.BleachBit", "7zip.7zip", "Microsoft.PowerToys", "Canonical.Ubuntu", `
;
foreach ($package in $packages)
{
  winget install $package
}

Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

choco feature enable -n=allowGlobalConfirmation
choco install tartube
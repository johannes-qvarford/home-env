Invoke-WebRequest https://i.redd.it/r235334p4tl61.png -OutFile "~/Pictures/terminal_background.png"

New-Item -ItemType SymbolicLink `
-Path "$HOME\AppData\Local\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\LocalState\settings.json" `
-Target "..\terminal\settings.json"
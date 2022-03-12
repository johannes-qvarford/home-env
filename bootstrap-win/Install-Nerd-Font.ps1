mkdir "$HOME\AppData\Local\Microsoft\Windows\Fonts"
Invoke-WebRequest https://github.com/ryanoasis/nerd-fonts/blob/master/patched-fonts/JetBrainsMono/Ligatures/Regular/complete/JetBrains%20Mono%20Regular%20Nerd%20Font%20Complete%20Windows%20Compatible.ttf?raw=true `
-OutFile "$HOME\AppData\Local\Microsoft\Windows\Fonts\JetbrainsMono NF.ttf"

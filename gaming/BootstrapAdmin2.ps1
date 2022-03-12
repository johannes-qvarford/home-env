wsl --install

$tempDir = [System.IO.Path]::GetTempPath();
Invoke-WebRequest "https://github.com/johannes-qvarford/home-env/archive/refs/heads/master.zip" -OutFile "~/Downloads/home-env-master.zip";
Expand-Archive "~/Downloads/home-env-master.zip" -DestinationPath  "$tempDir"
Set-Location "$tempDir/home-env-master/gaming"
wsl run './bootstrap-wsl.sh'

Copy-Item autostartup/home-env.bat "C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp"

Set-Location ~
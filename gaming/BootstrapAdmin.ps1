wsl --install

$tempDir = [System.IO.Path]::GetTempPath();
Invoke-WebRequest "https://github.com/johannes-qvarford/home-env/archive/refs/heads/master.zip" -OutFile "~/Downloads/home-env-master.zip";
Expand-Archive "~/Downloads/home-env-master.zip" -DestinationPath  "$tempDir"
Set-Location "$tempDir/home-env-master/gaming"

wsl run './bootstrap-wsl.sh'
# home-env should now be installed in $HOME/home-env and fish should be the default shell.
Set-Location (wsl.exe wslpath -aw '(wsl.exe' echo '$HOME/home-env/bootstrap-win)')
./Main.ps1

#dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
#dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
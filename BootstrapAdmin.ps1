wsl --install

# We want to download the repository right away.
# This doesn't require git, but the zip file that we download doesn't contain a .git directory.
# bootstrap-wsl will download it for real.
$tempDir = [System.IO.Path]::GetTempPath();
Invoke-WebRequest "https://github.com/johannes-qvarford/home-env/archive/refs/heads/master.zip" -OutFile "~/Downloads/home-env-master.zip";
Expand-Archive "~/Downloads/home-env-master.zip" -DestinationPath  "$tempDir"
Set-Location "$tempDir/home-env-master/gaming"

# In order for bootstrap-wsl to download the repository using git, we need to acquire the Github credentials.
winget Dropbox

wsl run './bootstrap-wsl.sh'
# home-env should now be installed in $HOME/home-env and fish should be the default shell.
Set-Location (wsl.exe wslpath -aw '(wsl.exe' echo '$HOME/home-env/bootstrap-win)')
./Main.ps1

#dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
#dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
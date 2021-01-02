# Instructions

* Type Win+Powershell, right click run as administrator
* Copy+Paste "Set-ExecutionPolicy Bypass -Scope Process -Force; Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://gitlab.com/johannes-q/home-env/-/raw/master/
BootstrapAdmin1.ps1'));"
* Restart
* Copy+Paste "Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://gitlab.com/johannes-q/home-env/-/raw/master/
BootstrapAdmin2.ps1'));"

## References

* Read https://docs.microsoft.com/en-us/windows/wsl/install-win10
* Read https://itnext.io/using-wsl-2-to-develop-java-application-on-windows-8aac1123c59b?gi=adf3bcc4fa53
* Read https://docs.microsoft.com/en-us/windows/terminal/get-started
* Read https://techcommunity.microsoft.com/t5/windows-dev-appconsult/running-wsl-gui-apps-on-windows-10/ba-p/1493242

For some reason the personal startup directory changed from Startup to STARTUP-.
Had to restore it:
https://answers.microsoft.com/en-us/windows/forum/windows_7-performance/shortcuts-in-the-startup-folder-not-running-during/646845d6-8c10-4456-b6ef-b99f4a03aa10

## TODO

* Evaluate IntelliJ through X
* Convert win-server docker script to docker-compose
* Setup automatic loading of cmdlets.
* Cmdlet for switching to administrator (interactive)
* Cmdlet for running file as Administrator
* Cmdlet for opening wsl jq home directory
* Cmdlet for switching to wsl
* Cmdlet for switching to wsl and open current directory
* Script for opening powershell as administrator
* Script for opening powershell
* Script for opening pwsh as administrator
## Temporary Garbage

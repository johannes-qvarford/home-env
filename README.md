# Instructions

* Type Win+Powershell, right click run as administrator
* Copy+Paste "Set-ExecutionPolicy Bypass -Scope Process -Force; Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://gitlab.com/johannes-q/win-env/-/raw/master/
BootstrapAdmin1.ps1'));"
* Restart
* Copy+Paste "Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://gitlab.com/johannes-q/win-env/-/raw/master/
BootstrapAdmin2.ps1'));"

## References

* Read https://docs.microsoft.com/en-us/windows/wsl/install-win10
* Read https://itnext.io/using-wsl-2-to-develop-java-application-on-windows-8aac1123c59b?gi=adf3bcc4fa53
* Read https://docs.microsoft.com/en-us/windows/terminal/get-started
* Read https://techcommunity.microsoft.com/t5/windows-dev-appconsult/running-wsl-gui-apps-on-windows-10/ba-p/1493242

## TODO

* TODO: sync visual studio code config
* TODO: Evaluate https://chocolatey.org/packages/caprine
* TODO: Evaluate IntelliJ through X
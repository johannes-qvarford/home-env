Type Win+Powershell, right click run as administrator
Copy+Paste BootstrapAdminManual.ps1 to terminal
Read https://docs.microsoft.com/en-us/windows/wsl/install-win10
./BootstrapAdmin1.ps1
Restart
Run https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi
./BootstrapAdmin2.ps1

Copy to fish.config:
    export DISPLAY="`grep nameserver /etc/resolv.conf | sed 's/nameserver //'`:0"

Read https://itnext.io/using-wsl-2-to-develop-java-application-on-windows-8aac1123c59b?gi=adf3bcc4fa53
Read https://docs.microsoft.com/en-us/windows/terminal/get-started
Read https://techcommunity.microsoft.com/t5/windows-dev-appconsult/running-wsl-gui-apps-on-windows-10/ba-p/1493242
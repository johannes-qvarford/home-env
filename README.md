
# Instructions

Username has to be "Johannes Qvarford".

* Turn on Developer Mode (https://stackoverflow.com/a/34905638)
* Manually create Z: drive.
* Type Win+Powershell, right click run as administrator
* Copy+Paste the content of the install file.
* Do vscode settings sync with Github account.
* Make sure that Drive letters are correct: L=Launchbox, D=Videos, G=Proton, F=VideosBackup.
* Manually install font through "explorer.exe $HOME\AppData\Local\Microsoft\Windows\Fonts\"
* Set game directory for Steam on G:
* Install https://kodi.wiki/view/Add-on:Backup for Kodi, and restore backup from G:\MEGAsync\Public\Media\T...
* * If there is a Kodi version mismatch - When adding video sources, remember that movies are in separate directories.


Commands to cross-compile to windows from Ubuntu.

```
rustup target add x86_64-pc-windows-gnu
sudo apt-get install -y mingw-w64
cargo run --target x86_64-pc-windows-gnu
```
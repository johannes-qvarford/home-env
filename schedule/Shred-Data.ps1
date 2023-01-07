echo "Beginning to shred"

echo "Kill browsers"
taskkill /IM firefox.exe /F
taskkill /IM brave.exe /F

echo "Actually shred"
& "C:\Users\deffo\AppData\Local\BleachBit\bleachbit_console.exe" --clean --overwrite `
    firefox.cookies firefox.dom firefox.forms firefox.passwords firefox.site_preferences firefox.url_history
& "C:\Users\deffo\AppData\Local\BleachBit\bleachbit_console.exe" --shred `
    'G:\Secrets\Consumed'

echo "Restore directory"
mkdir G:\Secrets\Consumed

echo "Done shreding"
sleep 3
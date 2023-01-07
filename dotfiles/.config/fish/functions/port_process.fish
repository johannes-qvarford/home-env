function port_process
     set -l port $argv[1]
     ss -tulpn | grep :$port
     powershell.exe -Command "Get-Process -Id \(Get-NetTCPConnection -LocalPort $port\).OwningProcess" 2>/dev/null
end
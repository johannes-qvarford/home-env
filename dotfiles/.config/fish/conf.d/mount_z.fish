if not mountpoint -q /mnt/z 2>/dev/null
    if test -d /mnt/z; or sudo mkdir -p /mnt/z 2>/dev/null
        sudo mount -t drvfs Z: /mnt/z 2>/dev/null
    end
end
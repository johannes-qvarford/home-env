#!/bin/bash

sudo apt install libfuse2

curl 'https://download-cdn.jetbrains.com/toolbox/jetbrains-toolbox-1.27.2.13801.tar.gz' -H 'User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:108.0) Gecko/20100101 Firefox/108.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8' -H 'Accept-Language: en-GB,en;q=0.5' -H 'Accept-Encoding: gzip, deflate, br' -H 'Connection: keep-alive' -H 'Cookie: cf_country-region=SE-O' -H 'Upgrade-Insecure-Requests: 1' -H 'Sec-Fetch-Dest: document' -H 'Sec-Fetch-Mode: navigate' -H 'Sec-Fetch-Site: same-site' -H 'Sec-Fetch-User: ?1'
curl -L https://download.jetbrains.com/toolbox/jetbrains-toolbox-1.27.2.13801.tar.gz >/tmp/toolbox.tar.gz
cd /tmp
sudo tar -xzf toolbox.tar.gz -C /opt


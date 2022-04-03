#!/bin/bash

sudo apt install -y git

ssh-keygen -t rsa -b 2048 -C "jq.email+github@pm.me"
echo
echo paste the below into https://github.com/settings/keys
echo
cat ~/.ssh/id_rsa.pub
echo
read -p "Have you copied the above? "
echo
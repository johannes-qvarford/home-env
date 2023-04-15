#!/bin/bash

sudo apt install -y git

ssh-keygen -t rsa -b 2048 -C "jq.email+github@pm.me"
echo
echo paste the below into https://github.com/settings/keys and 
echo
cat ~/.ssh/id_rsa.pub
echo
read -p "Have you copied the above? "


ssh-keygen -t rsa -b 2048 -C "jq.email+github@pm.me" -f ~/.ssh/id_rsa_privacy
echo
echo paste the below into https://cloud.digitalocean.com/account/security
echo 
cat ~/.ssh/id_rsa_privacy.pub
echo
echo now a passphrase will be requested.
ssh-keygen -p -o -f ~/.ssh/id_rsa_privacy
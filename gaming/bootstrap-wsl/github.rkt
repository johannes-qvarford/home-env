#lang rash

sudo dnf install -y git

git config user.name 'Johannes Qvarford'
git config user.email 'johqva.email+github@pm.me'

ssh-keygen -t rsa -b 2048 -C "jq.email+github@pm.me"
echo
echo paste the below into <TODO: ssh keys page on github>
echo
cat ~/.ssh/id_rsa.pub
echo
read -p "Have you copied the above? "
echo
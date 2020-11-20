sudo dnf install -y git fish

sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
curl https://packages.microsoft.com/config/rhel/7/prod.repo | sudo tee /etc/yum.repos.d/microsoft.repo
sudo dnf check-update
sudo dnf install -y compat-openssl10 powershell util-linux-user node

git config user.name 'Johannes Qvarford'
git config user.email 'jq.email+gitlab@pm.me'

ssh-keygen -t rsa -b 2048 -C "jq.email+gitlab@pm.me"
echo
echo paste the below into https://gitlab.com/-/profile/keys
echo
cat ~/.ssh/id_rsa.pub
echo
read -p "Have you copied the above? "
echo 

git clone https://gitlab.com/johannes-q/win-env ~/win-env

NEW=~
OLD=~/win-env/dotfiles
mkdir -p $NEW/.config
mkdir -p ~/bin
ln -s  $OLD/.config/fish $NEW/.config/fish 
rm -f $NEW/.gitconfig
ln -s $OLD/.gitconfig $NEW/.gitconfig
ln -s $OLD/bin/vscode $NEW/bin/vscode

ln -s /mnt/c/Users/Johannes\ Qvarford/ ~/win
sudo dnf install -y git fish

sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
curl https://packages.microsoft.com/config/rhel/7/prod.repo | sudo tee /etc/yum.repos.d/microsoft.repo
sudo dnf check-update
sudo dnf install -y compat-openssl10 powershell util-linux-user node fzf snapd

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

git clone git@gitlab.com:johannes-q/home-env.git ~/home-env

NEW=~
OLD=~/home-env/dotfiles
mkdir -p $NEW/.config
mkdir -p ~/bin
ln -s  $OLD/.config/fish $NEW/.config/fish 
rm -f $NEW/.gitconfig
ln -s $OLD/.gitconfig $NEW/.gitconfig
ln -s $OLD/bin/vscode $NEW/bin/vscode

ln -s /mnt/c/Users/Johannes\ Qvarford/ ~/win

mkdir -p ~/home-env/vscode-settings
ln -s ~/win/AppData/Roaming/Code/User/settings.json ~/home-env/vscode-settings/settings.json
ln -s ~/win/AppData/Roaming/Code/User/snippets ~/home-env/vscode-settings/snippets

sudo dnf -y install https://download1.rpmfusion.org/free/fedora/rpmfusion-free-release-$(rpm -E %fedora).noarch.rpm
sudo dnf -y install https://download1.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-$(rpm -E %fedora).noarch.rpm
sudo dnf -y install ffmpeg
sudo npm install -g crunchyroll-dl

# sudo snap install intellij-idea-community --classic

fisher
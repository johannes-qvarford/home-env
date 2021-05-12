sudo dnf install -y git fish

sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
curl https://packages.microsoft.com/config/rhel/7/prod.repo | sudo tee /etc/yum.repos.d/microsoft.repo
sudo dnf check-update
sudo dnf install -y powershell util-linux-user nodejs fzf nmap python3-pip rsync emacs-nox racket

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

echo "Adding public keys to ssh-server, and committing new key to repo"
DEVICE=gaming
cd ~/win/.ssh
touch authorized_keys
cat ~/home-env/gaming/gaming.id_rsa.pub >>authorized_keys
cat ~/home-env/admin/admin.id_rsa.pub >>authorized_keys
cp ~/.ssh/id_rsa.pub ~/home-env/$DEVICE/$DEVICE.id_rsa.pub
cp ~/.ssh/id_rsa ~/.ssh/id_rsa.pub ~/win/.ssh/

chmod 700 ~/.ssh
chmod 644 ~/.ssh/known_hosts
chmod 600 ~/.ssh/id_rsa
chmod 644 ~/.ssh/id_rsa.pub

chmod 700 ~/winhome/.ssh
chmod 644 ~/winhome/.ssh/authorized_keys
chmod 644 ~/winhome/.ssh/known_hosts
chmod 600 ~/winhome/.ssh/id_rsa
chmod 644 ~/winhome/.ssh/id_rsa.pub

cd ~/home-env/
git add **/*.id_rsa.pub
git ci -m "Updated public key for $DEVICE."
git push

NEW=~
OLD=~/home-env/gaming/dotfiles
mkdir -p $NEW/.config
ln -s  $OLD/.config/fish $NEW/.config/fish 
rm -f $NEW/.gitconfig
ln -s $OLD/.gitconfig $NEW/.gitconfig

ln -s $OLD/bin $NEW/bin


rm ~/win/AppData/Roaming/Code/User/settings.json
rm -rf ~/win/AppData/Roaming/Code/User/snippets

ln -s /mnt/c/ProgramData/chocolatey/bin/megatools.exe ~/bin/megatools

python3 -m pip install --user pipx
python3 -m pipx ensurepath
set PATH $PATH /home/jq/.local/bin
pipx install twitch-dl

chsh -s /usr/bin/fish

fish -c "fisher"
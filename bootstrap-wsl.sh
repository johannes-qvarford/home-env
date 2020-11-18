sudo dnf install -y git fish

sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
curl https://packages.microsoft.com/config/rhel/7/prod.repo | sudo tee /etc/yum.repos.d/microsoft.repo
sudo dnf check-update
sudo dnf install -y compat-openssl10 powershell
ssh-keygen -t rsa -b 2048 -C "jq.email+gitlab@pm.me"
echo
echo paste the below into https://gitlab.com/-/profile/keys
echo
cat ~/.ssh/id_rsa.pub
echo

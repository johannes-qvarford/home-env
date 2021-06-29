#lang rash

sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
curl https://packages.microsoft.com/config/fedora/34/prod.repo | sudo tee /etc/yum.repos.d/microsoft.repo

; powershell is only available on the Red Hat MS repo, don't want to install the repo just for that.
;sudo dnf install -y compat-openssl10
sudo dnf install -y https://github.com/PowerShell/PowerShell/releases/download/v7.1.3/powershell-7.1.3-1.rhel.7.x86_64.rpm
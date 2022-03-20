#!/bin/bash

if [[ "$USER" != "jq" ]]; then
    echo "Running as root on the first invocation..."
    sudo adduser jq
    sudo usermod -aG sudo jq
    
    cd /home/jq
    sudo su --login jq -- tocopy/setup.sh
    exit 0
fi
echo "Now running as jq..."

function disable-root-login() {
    sudo cp -rp /root/tocopy/* ~/tocopy
    sudo chown -R jq:jq ~/tocopy
    sudo cp -R /root/.ssh ~
    chown -R jq:jq ~/.ssh
    sudo sed -i 's/^[# ]*PermitRootLogin.*/PermitRootLogin no/' /etc/ssh/sshd_config
    sudo service ssh restart
}

function install-docker() {
    sudo apt update
    sudo apt install apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
    apt-cache policy docker-ce
    sudo apt install docker-ce
    sudo systemctl status docker
    # sudo apt install -y docker
    sudo usermod -aG docker jq
    sudo apt install docker-compose
}

function install-certbot() {
    sudo snap install core
    sudo snap refresh core
    sudo snap install --classic certbot
    sudo ln -s /snap/bin/certbot /usr/bin/certbot
    sudo snap set certbot trust-plugin-with-root=ok
    sudo snap install certbot-dns-digitalocean


    echo "What is your digical ocean key?"
    read -s DO_KEY
    cat >/home/jq/.digitalocean.ini <<EOF
dns_digitalocean_token = $DO_KEY
EOF
    unset DO_KEY

    sudo certbot certonly \
        --dns-digitalocean \
        --dns-digitalocean-credentials ~/.digitalocean.ini \
        -d privacy.qvarford.net \
        -d '*.privacy.qvarford.net'
    sudo certbot renew --dry-run
}

function install-nginx() {
    cd /home/jq/tocopy/nginx
    sudo ufw allow 80
    sudo ufw allow 443
    docker-compose up -d
}

function install-invidious() {
    cd ~
    sudo apt install -y git
    git clone https://github.com/johannes-qvarford/invidious.git
    cd invidious
    git checkout jq
    # sudo
    docker-compose up -d
}

function install-nitter() {
    cd ~/tocopy/nitter
    docker-compose up -d
}

disable-root-login
install-certbot
install-docker
install-invidious
install-nitter
install-nginx




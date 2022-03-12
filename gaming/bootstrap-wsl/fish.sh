#!/bin/bash

sudo apt update
sudo apt upgrade
sudo apt-add-repository -y ppa:fish-shell/release-3
sudo apt update

sudo apt install -y fish
chsh -s /usr/bin/fish
fish -c "fisher"
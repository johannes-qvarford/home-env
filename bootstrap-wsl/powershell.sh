#!/bin/bash

sudo apt update
sudo apt install -y wget apt-transport-https software-properties-common
cd /tmp
wget -q https://packages.microsoft.com/config/ubuntu/20.04/packages-microsoft-prod.deb 
sudo dpkg -i packages-microsoft-prod.deb
sudo apt update
sudo apt install -y powershell
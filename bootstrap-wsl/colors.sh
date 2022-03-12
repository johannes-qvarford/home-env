#!/bin/bash

# lsd
# https://github.com/Peltoche/lsd/releases/download/0.21.0/lsd_0.21.0_amd64.deb
file=lsd_0.21.0_amd64.deb
cd /tmp
wget https://github.com/Peltoche/lsd/releases/download/0.21.0/$file
sudo dpkg -i /tmp/$file
sudo apt install -f

file=git-delta_0.11.3_amd64.deb
cd /tmp
wget https://github.com/dandavison/delta/releases/download/0.11.3/$file
sudo dpkg -i /tmp/$file
sudo apt install -f

# http://mirrors.kernel.org/ubuntu/pool/universe/r/rust-fd-find/fd-find_7.4.0-2build1_amd64.deb
file=fd-find_7.4.0-2build1_amd64.deb
cd /tmp
wget http://mirrors.kernel.org/ubuntu/pool/universe/r/rust-fd-find/$file
sudo dpkg -i /tmp/$file
sudo apt install -f
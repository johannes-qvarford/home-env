#!/bin/bash

bash github.sh
bash clone-home-env.sh
cd ~/home-env/bootstrap-wsl

bash dotfiles.sh
bash fish.sh
bash colors.sh

bash powershell.sh
bash rust.sh
bash backup.sh
bash fzf.sh
bash extra.sh
bash media.sh

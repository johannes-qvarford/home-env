#!/bin/bash
bash clone-home-env.sh
cd ~/home-env/bootstrap-wsl

bash github.sh
bash dotfiles.sh
bash fish.sh
bash colors.sh

bash powershell.sh
bash backup.sh
bash extra.sh
bash privacy.sh
bash media.sh
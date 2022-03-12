#!/bin/bash

bash github.sh
bash clone-home-env.sh
cd ~/home-env/bootstrap-wsl


bash dotfiles.sh
bash fish.sh
bash colors.sh

bash powershell.sh
bash backup.sh
bash extra.sh
bash privacy.sh
bash media.sh

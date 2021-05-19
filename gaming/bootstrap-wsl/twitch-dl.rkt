#lang rash

sudo dnf install -y python3-pip

python3 -m pip install --user pipx
python3 -m pipx ensurepath
/home/current/.local/bin/pipx install twitch-dl
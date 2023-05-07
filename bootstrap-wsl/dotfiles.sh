#!/bin/bash

dir() {
    path="$1"
    mkdir -p ~/"$path"
}

link() {
    path="$1"
    ln -sfn ~/home-env/dotfiles/"$path" ~/"$path"
}

dir .ssh
link .ssh/config

dir .config
link .config/fish
link .config/icons

dir .docker
link .docker/config.json

link bin
link .gitconfig
link .tmux.conf
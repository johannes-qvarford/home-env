#!/bin/bash

new=$HOME
old="$HOME/home-env/dotfiles"
mkdir -p $new/.config
rm -f $new/.config/fish
ln -s  $old/.config/fish $new/.config/fish

rm -f $new/.gitconfig
ln -s $old/.gitconfig $new/.gitconfig
rm -f $new/.tmux.conf
ln -s $old/.tmux.conf $new/.tmux.conf

rm -f $new/bin
ln -s $old/bin $new/bin

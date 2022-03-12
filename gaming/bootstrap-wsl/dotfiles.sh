#!/bin/bash

new=~
old="~/home-env/gaming/dotfiles"
mkdir -p $new/.config
ln -s  $old/.config/fish $new/.config/fish

rm -f $new/.gitconfig
ln -s $old/.gitconfig $new/.gitconfig
rm -f $new/.tmux.conf
ln -s $old/.tmux.conf $new/.tmux.conf

ln -s $old/bin $new/bin

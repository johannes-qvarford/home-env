#lang rash

(define new "/home/current")
(setenv old "/home/current/home-env/gaming/dotfiles")
mkdir -p $new/.config
ln -s  $old/.config/fish $new/.config/fish 
rm -f $new/.gitconfig
ln -s $old/.gitconfig $new/.gitconfig

ln -s $old/bin $new/bin
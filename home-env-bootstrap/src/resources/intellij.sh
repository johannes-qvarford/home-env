#!/bin/bash

# Add the Flathub repository
sudo flatpak remote-add --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

# Install IntelliJ IDEA Community Edition
sudo flatpak install -y flathub com.jetbrains.IntelliJ-IDEA-Community
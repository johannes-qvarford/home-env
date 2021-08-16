#lang rash

curl -s "https://get.sdkman.io" | bash
fisher add reitzig/sdkman-for-fish@v1.4.0
sdk install java 11.0.11.hs-adpt

curl -s https://raw.githubusercontent.com/babashka/babashka/master/install | bash

curl -s https://download.clojure.org/install/linux-install-1.10.3.855.sh >/tmp/clojure-install
chmod +x /tmp/clojure-install
sudo /tmp/clojure-install
sudo dnf install -y rlwrap
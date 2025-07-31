#!/bin/bash

cd $(mktemp -d)

wget https://app.warp.dev/download?package=deb -O warp.deb
sudo dpkg -i warp.deb
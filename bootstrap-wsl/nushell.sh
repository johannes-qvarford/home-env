#!/bin/bash

version="0.89.0"
tar_dir="nu-$version-x86_64-unknown-linux-gnu"
tar_file="$tar_dir.tar.gz"

cd (mktemp -d)
curl "https://github.com/nushell/nushell/releases/download/$version/$tar_file" -L -o "$tar_file"
mkdir "$tar_dir"
tar --extract --gzip --file "$tar_file" --strip-components=1 "$tar_dir/nu"
sudo mv nu /usr/local/bin
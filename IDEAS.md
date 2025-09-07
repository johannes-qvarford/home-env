# Introduction

Ideas for how home-env could evolve in the future.

I want to be able to add more self-contained "things".
Currently, downloading bun is partically part of the binary, and part of fish configuration that mingles with other kinds of configuration.
It would be nice if both of those parts were at the same place, so you could add them more smoothly, remove them easier etc.

We can call these jqpacks

A jqpack is a directory or git repository that contains the following:
install (script that installs apt packages, curl-to-bash etc. Can be python or bash etc.)
dotfiles/
fish_conf.d/
.sync.json
bin/
docker-compose.yml

Could have multiple foundation jqpacks in home-env, like:
bun
node
android
jetbrains
rust
sdkman
docker
utility

also projects in different git repos:
ai-remote-control
some-mcp

The bootstrap would mainly be given a list of jqpacks, and be responsible for installing them, handling the dotfile syncing etc.
# Introduction

Portable computer intended for programming, writing, browsing and general administration of the home network.

Uses windows with WSL for great software compatibility.

Can SSH into other devices and set them up remotely, except for router.

Is used to write home-env.

Programming is done using VSCode with Remote-WSL.

Fish is the shell of choice.

Javascript is the programming language of choice - it can be used for server backends and frontends, as well as scripts, with a relatively small memory footprint. A proper commercial product from me might not have the same setup.

# Instructions

* Update windows.
* Install OpenSSH.
* Download "Switch out of S mode" from Microsoft Store.
* Type Win+X, Select Powershell (administrator)
* Copy+Paste the content of the install file.
* Restart
* Copy+Paste the content of the install2 file.

# How to Connect

ssh "deffo@gaming.lan"

Note: Two accounts exists:
* johannes.qvarford@outlook.com (alias johan),
* deffo
The second one doesn't have the expected password, and needs to change for ssh to work (using Ctrl+Alt+Delete -> Change Password).


## Ideas

### cast4api

router.lan could act like a chromecast.
It could have an api (cast4pi) that e.g. invoked livestreamer cli for twitch and youtube, and opened a url in default browser in the worst case.

Browser extension for quickly sending the url to the api?

Would maybe have to write a web app for autoplaying plex media?
Could be hard, selenium or something might be easier that opens browser, logs in, goes to url and hits play?

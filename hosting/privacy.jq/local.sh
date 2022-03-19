#!/bin/bash
# Prerequisite: You have registered the public ssh key with the root user.
IP=privacy.qvarford.net

scp -rp tocopy root@${IP}:/root
ssh root@${IP} tocopy/setup.sh

ssh-copy-id jq@${IP}
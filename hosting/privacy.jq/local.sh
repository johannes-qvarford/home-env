#!/bin/bash
# Prerequisite: You have registered the public ssh key with the root user.
IP=TODO

scp -rp tocopy root@${IP}:/root
ssh root@${IP} tocopy/setup.sh
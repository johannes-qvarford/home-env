#!/bin/bash
# Prerequisite: You have registered the public ssh key with the root user.
IP=privacy.qvarford.net
OPENVPN_FILE="$1"

scp -rp tocopy root@${IP}:./
scp -rp "$OPENVPN_FILE" root@${IP}:./tocopy
ssh root@${IP} tocopy/setup.sh
# At this point, only the jq user can ssh in.
#lang racket


echo "Adding public keys to ssh-server, and committing new key to repo"
(setenv DEVICE "gaming")
cd ~/win/.ssh
touch authorized_keys
cat ~/home-env/gaming/gaming.id_rsa.pub >>authorized_keys
cat ~/home-env/admin/admin.id_rsa.pub >>authorized_keys
cp ~/.ssh/id_rsa.pub ~/home-env/$DEVICE/$DEVICE.id_rsa.pub
cp ~/.ssh/id_rsa ~/.ssh/id_rsa.pub ~/win/.ssh/

chmod 700 ~/.ssh
chmod 644 ~/.ssh/known_hosts
chmod 600 ~/.ssh/id_rsa
chmod 644 ~/.ssh/id_rsa.pub

chmod 700 ~/winhome/.ssh
chmod 644 ~/winhome/.ssh/authorized_keys
chmod 644 ~/winhome/.ssh/known_hosts
chmod 600 ~/winhome/.ssh/id_rsa
chmod 644 ~/winhome/.ssh/id_rsa.pub

cd ~/home-env/
git add **/*.id_rsa.pub
git ci -m "Updated public key for $DEVICE."
git push
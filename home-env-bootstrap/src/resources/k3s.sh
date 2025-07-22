#!/bin/bash
# K3s lightweight Kubernetes installation script
# Official installation method from https://k3s.io/

# Download and install K3s
curl -sfL https://get.k3s.io | sh -

# Start and enable K3s service
sudo systemctl enable k3s
sudo systemctl start k3s

# Set up kubeconfig for current user
mkdir -p ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $USER:$USER ~/.kube/config
chmod 600 ~/.kube/config
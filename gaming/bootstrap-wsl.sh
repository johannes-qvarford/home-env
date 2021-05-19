# Try to run the rest of the scripts through racket
sudo dnf install -y racket
raco pkg install rash stream-etc algorithms
cd bootstrap-wsl
racket main.rkt


# docker-compose dependencies
sudo dnf install -y libxcrypt-compat
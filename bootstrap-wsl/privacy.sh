cd ~/home-env
git clone git@github.com:johannes-qvarford/invidious.git
cd invidious
docker-compose up -d
cd ../nitter
docker-compose up -d
# Invidious hosting

* Buy qvarford.net
* Register it with DigitalOcean
* Verify that invidious.privacy.qvarford.net:80 can be reached.
* Install Let's Encrypt and configure nginx to use the SSL certificate.
* Configure invidious repo to use port 443 and invidious.privacy.qvarford.net
* Verify that 80 redirects to 443, and that 443 works.
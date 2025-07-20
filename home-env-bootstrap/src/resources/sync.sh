#!/bin/bash

python3 -c "import watchdog; print('watchdog available')" 2>/dev/null || echo "Need to install watchdog: pip3 install watchdog"
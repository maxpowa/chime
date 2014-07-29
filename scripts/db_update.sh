#!/bin/sh +x
curl -X PUT -d '$(./scripts/db_data.sh)' https://irc-bot-backend.firebaseio.com/builds.json?auth=cufRWsNn5Xos90zvdJE8qHMp2kCiXwZ6Xw5a2n6p
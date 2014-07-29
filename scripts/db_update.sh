#!/bin/bash
JAVA_VERSION=`java -version 2>&1 | grep "java version" | awk '{print $3}' | sed -e "s/\"//g"`
TEMP_JSON=`printf '{"%s":{"JAVA_VERSION":"%s","BRANCH":"%s","JOB_ID":"%s"}}\n' "$COMMIT" "$JAVA_VERSION" "$BRANCH" "$JOB_ID"`
curl -X PUT -d $TEMP_JSON https://irc-bot-backend.firebaseio.com/builds.json?auth=cufRWsNn5Xos90zvdJE8qHMp2kCiXwZ6Xw5a2n6p 
echo $TEMP_JSON
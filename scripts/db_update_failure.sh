#!/bin/bash 
JAVA_VERSION=`java -version 2>&1 | grep "java version" | awk '{print $3}' | sed -e "s/\"//g"` 
TEMP_JSON=`printf '{"SUCCESS":false,"FORGEVERSION":"%s","COMMIT":"%s","JAVA_VERSION":"%s","BRANCH":"%s","JOB_ID":"%s","PULL_REQUEST":"%s"}\n' "$FORGEVERSION" "$COMMIT" "$JAVA_VERSION" "$BRANCH" "$JOB_ID" "$PULL_REQUEST"` 
URL=`printf 'https://irc-bot-backend.firebaseio.com/builds/%s.json?auth=cufRWsNn5Xos90zvdJE8qHMp2kCiXwZ6Xw5a2n6p' "$BUILD_NUMBER"` 
curl -X PUT -d $TEMP_JSON $URL 
echo $TEMP_JSON
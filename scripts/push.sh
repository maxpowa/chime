#!/bin/bash 
MC_VERSION=`echo "$FORGEVERSION" | awk -F'-' '{print $1}'`
FILE_NAME="Chime-$MAJOR_MINOR.$BUILD_NUMBER.jar"
FILE_LOCATION="build/libs/$FILE_NAME"
JSON='{
  "version": {
    "name": "$MAJOR_MINOR.$BUILD_NUMBER",
    "minecraft": "$MC_VERSION",
    "changelog": "http://github.com/maxpowa/chime/commits/$COMMIT"
  },
  "filename": "$FILE_NAME"
}'

echo "Ready to push file to mods.io"
echo "$JSON"

if [ "$PUSH" = true ] ; then
    curl -H 'X-API-Key: $MODS_IO_APIKEY' -X POST -H 'Accept: application/json' -F body='$JSON' -F file='@$FILE_LOCATION' https://mods.io/mods/1086/versions/create.json
fi
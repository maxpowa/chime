#!/bin/bash 
MC_VERSION=`echo "$FORGEVERSION" | awk -F'-' '{print $1}'`
FILE_NAME=`echo "Chime-$MAJOR_MINOR.$BUILD_NUMBER.jar"`
FILE_LOCATION=`echo "build/libs/$FILE_NAME"`
JSON=`printf '{
  "version": {
    "name": "%s.%s",
    "minecraft": "%s",
    "changelog": "http://github.com/maxpowa/chime/commits/%s"
  },
  "filename": "%s"
}' "$MAJOR_MINOR" "$BUILD_NUMBER" "$MC_VERSION" "$COMMIT" "$FILE_NAME"`

echo "Ready to push file to mods.io"
echo "$JSON"

if [ "$PUSH" = true ] ; then
    curl -H 'X-API-Key: ${MODS_IO_APIKEY}' -X POST -H 'Accept: application/json' -F body='$JSON' -F file='@${FILE_LOCATION}' https://mods.io/mods/1086/versions/create.json
fi
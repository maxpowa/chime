#!/bin/bash 
MC_VERSION=`echo "$FORGEVERSION" | awk -F'-' '{print $1}'`
FILE_NAME=`echo "Chime-$MAJOR_MINOR.$BUILD_NUMBER.jar"`
FILE_LOCATION=`echo "./build/libs/$FILE_NAME"`
JSON=`printf '{"version": { "name": "%s.%s", "minecraft": "%s", "changelog": "See commit [url=http://github.com/maxpowa/chime/commit/%s]%s[/url] for full change details.", "tag": "%s" }, "filename": "%s" }' "$MAJOR_MINOR" "$BUILD_NUMBER" "$MC_VERSION" "$COMMIT" "$COMMIT" "$TAG" "$FILE_NAME"`

echo "Ready to push file to mods.io"
echo "$JSON"

if [ "$PULL_REQUEST" = "None" ] ; then
    if [ "$PUSH" = true ] ; then
       
        curl -i -H "X-API-Key: $MODS_IO_KEY" \
        -X POST \
        -H 'Accept: application/json' \
        -F body="$JSON" \
        -F file="@$FILE_LOCATION" \
        https://mods.io/mods/1087/versions/create.json

    fi
fi
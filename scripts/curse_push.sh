#!/bin/bash 
FILE_NAME=`echo "Chime-$MAJOR_MINOR.$BUILD_NUMBER.jar"`
FILE_LOCATION=`echo "./build/libs/$FILE_NAME"`
JSON=`printf '{"changelog": "See http://github.com/maxpowa/chime/commit/%s for full change details.", "gameVersions":%s, "releaseType": "%s"}' "$COMMIT" "$CURSE_VERSION_IDS" "$TAG"`

echo "Ready to push file to curse"
echo "$JSON"

if [ "$PULL_REQUEST" = "None" ] ; then
    if [ "$PUSH" = true ] ; then
       
        RESPONSE=$(curl -H "X-Api-Token: $CURSE_API_KEY" \
        -X POST \
        -F metadata="$JSON" \
        -F file="@$FILE_LOCATION" \
        http://minecraft.curseforge.com/api/projects/223265/upload-file)

        TEMP_JSON=`printf '{"curse":"%s", "type":"%s", "public":"%s", "version":"%s"}' "$RESPONSE" "$TAG" "$PUSH" "$MAJOR_MINOR.$BUILD_NUMBER"` 
        URL=`printf 'https://irc-bot-backend.firebaseio.com/releases/%s.json?auth=cufRWsNn5Xos90zvdJE8qHMp2kCiXwZ6Xw5a2n6p' "$BUILD_NUMBER"` 
        curl -X PUT -d $TEMP_JSON $URL 
        echo $TEMP_JSON

    fi
fi
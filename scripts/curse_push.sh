#!/bin/bash
FILE_NAME=`echo "Chime-$MAJOR_MINOR.$BUILD_NUMBER.jar"`
FILE_LOCATION=`echo "./build/libs/$FILE_NAME"`
JSON=`printf '{"changelog": "See http://github.com/maxpowa/chime/commit/%s for full change details.", "gameVersions":%s, "releaseType": "%s"}' "$COMMIT" "$CURSE_VERSION_IDS" "$TAG"`

echo "Ready to push file to curse"
echo "$JSON"

if [ "$PULL_REQUEST" = false ] ; then
    if [ "$PUSH" = true ] ; then

        RESPONSE=`curl -H "X-Api-Token: $CURSE_API_KEY" -X POST  -F metadata="$JSON"  -F file="@$FILE_LOCATION" https://minecraft.curseforge.com/api/projects/223265/upload-file`
        echo $RESPONSE

    fi
fi

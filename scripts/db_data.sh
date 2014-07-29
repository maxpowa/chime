#!/bin/sh
printf '{"%s":{"JAVA_VERSION":"%s","BRANCH":"%s","JOB_ID":"%s"}}\n' "$COMMIT" "$(./scripts/java_version.sh)" "$BRANCH" "$JOB_ID"
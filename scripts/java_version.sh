#!/bin/sh
JAVA_VERSION=`echo "$(java -version 2>&1)" | grep "java version" | awk '{ print substr($3, 2, length($3)-2); }'`
export JAVA_VERSION
echo $JAVA_VERSION
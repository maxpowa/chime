#!/bin/bash

./gradlew clean
./gradlew build
read -n1 -r -p "Press any key to continue..." key

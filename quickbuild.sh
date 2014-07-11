#!/bin/bash

./gradle clean
./gradle build
read -n1 -r -p "Press any key to continue..." key
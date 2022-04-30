#!/bin/bash

REPOSITORY=/home/ubuntu/chamchimayo
APP_NAME=chamchimayo-api

JAR_NAME=$(ls $REPOSITORY/$APP_NAME/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$APP_NAME/build/libs/$JAR_NAME

echo "> 새 애플리케이션 배포 : $JAR_PATH"
echo "> JAR name : $JAR_NAME"

nohup java -jar $JAR_PATH 3>&1 &

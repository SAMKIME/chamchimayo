#!/bin/bash

REPOSITORY=/home/ubuntu/chamchimayo
APP_NAME=chamchimayo-api

PRIVATE_PATH=/home/ubuntu/privateFile
API_RESOURCE_PATH=$REPOSITORY/$APP_NAME/src/main/resources
echo  "> move private file : application-oauth.yml"
cp $PRIVATE_PATH/application-oauth.yml $API_RESOURCE_PATH

COMMON_RESOURCE_PATH=$REPOSITORY/chamchimayo-common/src/main/resources
echo  "> move private file : application.yml"
cp $PRIVATE_PATH/application-oauth.yml $COMMON_RESOURCE_PATH

echo "> go to chamchimayo directory"
cd $REPOSITORY

echo "> project build start"
sudo bash ./gradlew build

JAR_NAME=$(ls $REPOSITORY/$APP_NAME/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$APP_NAME/build/libs/$JAR_NAME

echo "> current running application PID check"
CURRENT_PID=$(pgrep -f ${APP_NAME}.*.jar)
echo " current running application PID : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동중인 게 없으므로 종료!"
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

#echo "> 새 애플리케이션 배포 : $JAR_PATH"
#echo "> JAR name : $JAR_NAME"
#
#nohup java -jar $JAR_PATH 3>&1 &

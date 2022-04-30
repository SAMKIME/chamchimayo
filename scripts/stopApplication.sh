#!/bin/bash

APP_NAME=chamchimayo-api

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

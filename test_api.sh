#!/bin/bash

set -e

#Starting the Container
echo "** Starting the Container **" 
/usr/bin/docker run -it -d -p 8077:5000 --name=StreakAI neeabhishek/streak_ai:6

#Validate the Container
echo "** Validating the Container **"
CONTAINER_RESPONSE=$(docker ps | grep streak_ai)
echo "Container Response - ${CONTAINER_RESPONSE}"

echo "** Waiting for 5 sec for API Testing **"
sleep 5

#API Respone capture 
API_RESPONSE=$(curl "http://localhost:8077/add?num1=10&num2=20")

if [ "$API_RESPONSE" == "30" ]; then
    exit 0
else
    exit 1
fi

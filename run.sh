#!/bin/sh

echo "********************************************************"
echo "Waiting for the database server to start on port $DATABASESERVER_PORT"
echo "********************************************************"
while ! `nc -z walletdb $DATABASESERVER_PORT`; do sleep 3; done
echo ">>>>>>>>>>>> Database Server has started"

java  -Dspring.profiles.active=$PROFILE -DCURRENCY_CONVERTER_API_KEY=a31a6e67f79b887a2b70135638476106 -jar /usr/local/walletapi/wallet-api-0.0.1-SNAPSHOT.jar
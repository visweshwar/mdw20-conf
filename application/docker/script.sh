#!/bin/bash

mkdir -p /keys # file.txt will come at the end of the script
echo $MASTER_KEY >> /keys/master-keys.txt

#supervisord -c "/etc/supervisor.conf"
java -jar /app.jar
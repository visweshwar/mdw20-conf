#!/bin/bash

mkdir -p /keys # file.txt will come at the end of the script
echo $MASTER_KEY >>/keys/master-keys.txt

#supervisord -c "/etc/supervisor.conf"
java -agentlib:jdwp=transport=dt_socket,address=5006,server=y,suspend=n -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m -jar /app.jar

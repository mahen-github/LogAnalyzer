#!/bin/bash


mvn clean install 

cp /home/mqp29/workspace/LogAnalyzer/target/LogAnalyzer-1.0-SNAPSHOT.jar /home/mqp29/work/1/opt/edh/

docker cp /home/mqp29/workspace/WebLogsStreaming/src/main/resources_bkp/logs/access.log worker1:/tmp/
docker cp /home/mqp29/workspace/WebLogsStreaming/src/main/resources/GeoLiteCity.dat worker1:/tmp/

hadoop fs -put /tmp/access.log /tmp/logStreaming/input/access1.log

spark-submit --class "dev.mahen.streaming.Analyse"  --master local[*] /opt/edh/LogAnalyzer-1.0-SNAPSHOT.jar "/tmp/logStreaming/input" "/tmp/logStreaming/output"

/tmp/logStreaming/input

su - hdfs -c "hadoop fs -mkdir -p /tmp/logStreaming/input/"
su - hdfs -c "hadoop fs -mkdir -p /tmp/logStreaming/output/"
su - hdfs -c "hadoop fs -chmod -R 777 /tmp/logStreaming"
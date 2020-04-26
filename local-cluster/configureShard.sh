#usr/bin/bash

#configure the config server replicaset
docker exec -it mongocfg1 bash -c "echo 'rs.initiate({_id: \"config-rs\",configsvr: true, members: [{ _id : 0, host : \"mongocfg1\" },{ _id : 1, host : \"mongocfg2\" }, { _id : 2, host : \"mongocfg3\" }]})' | mongo"
sleep 2s
#Valiate that the replicaset is good
docker exec -it mongocfg1 bash -c "echo 'rs.status()' | mongo"
sleep 2s

#Conifgure the shard replicaset
docker exec -it us-standard bash -c "echo 'rs.initiate({_id : \"us-standard-rs\", members: [{ _id : 0, host : \"us-standard\" }]})' | mongo"
docker exec -it us-premium-1 bash -c "echo 'rs.initiate({_id : \"us-premium-rs\", members: [{ _id : 0, host : \"us-premium-1\" },{ _id : 1, host : \"us-premium-2\" },{ _id : 2, host : \"us-premium-3\" }]})' | mongo"
docker exec -it us-archive-1 bash -c "echo 'rs.initiate({_id : \"us-archive-rs\", members: [{ _id : 0, host : \"us-archive-1\" },{ _id : 1, host : \"us-archive-2\" },{ _id : 2, host : \"us-archive-3\" }]})' | mongo"
docker exec -it eu-standard bash -c "echo 'rs.initiate({_id : \"eu-standard-rs\", members: [{ _id : 0, host : \"eu-standard\" }]})' | mongo"
docker exec -it eu-premium-1 bash -c "echo 'rs.initiate({_id : \"eu-premium-rs\", members: [{ _id : 0, host : \"eu-premium-1\" },{ _id : 1, host : \"eu-premium-2\" },{ _id : 2, host : \"eu-premium-3\" }]})' | mongo"
docker exec -it eu-archive-1 bash -c "echo 'rs.initiate({_id : \"eu-archive-rs\", members: [{ _id : 0, host : \"eu-archive-1\" },{ _id : 1, host : \"eu-archive-2\" },{ _id : 2, host : \"eu-archive-3\" }]})' | mongo"
sleep 2s

#Add the shard to th router
#You will have to do this on just one of the QRs
docker exec -it mongos1 bash -c "echo 'sh.addShard(\"us-standard-rs/us-standard\")' | mongo "
docker exec -it mongos1 bash -c "echo 'sh.addShard(\"us-premium-rs/us-premium-1\")' | mongo "
docker exec -it mongos1 bash -c "echo 'sh.addShard(\"us-archive-rs/us-archive-1\")' | mongo "
docker exec -it mongos1 bash -c "echo 'sh.addShard(\"eu-standard-rs/eu-standard\")' | mongo "
docker exec -it mongos1 bash -c "echo 'sh.addShard(\"eu-premium-rs/eu-premium-1\")' | mongo "
docker exec -it mongos1 bash -c "echo 'sh.addShard(\"eu-archive-rs/eu-archive-1\")' | mongo "
sleep 2s

#Check the shard status
docker exec -it mongos1 bash -c "echo 'sh.status()' | mongo "


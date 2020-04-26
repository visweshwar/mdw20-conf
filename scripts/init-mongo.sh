#!/bin/bash
#*************#
#*** SETUP ***#
#*************#

set -e # Exit on any error

THIS_DIR=$(dirname "$0")
. $THIS_DIR/config.sh

run() {
  set -xe # Echo command
  "${@}"
  { set +x; } 2>/dev/null  # Turn off echo without writing to console
}

#*************************#
#***  FIND MONGOS POD  ***#
#*************************#

KUBECTL_ARGS="--context=$MONGOS_CLUSTER"

# Find Pod name for query server
MONGOS_POD=$(run kubectl get pods \
              --selector "app=mongos-$MONGOS_DNS_NAME" \
              --output=name \
              $KUBECTL_ARGS \
    | sed 's/^pod\///' \
)

#*********************************#
#***  INITIALIZE REPLICA SETS  ***#
#*********************************#

for REPLICA_SET_INDEX in "${!MONGO_REPLICA_SETS[@]}"; do

  REPLICA_SET_NAME=${MONGO_REPLICA_SETS[$REPLICA_SET_INDEX]}
  REPLICA_SET_DNS_NAMES=(${MONGO_DNS_NAMES[$REPLICA_SET_INDEX]})

  MEMBERS=()
  id=0
  for DNS_NAME in "${REPLICA_SET_DNS_NAMES[@]}"; do
    MEMBERS+=( "{ _id: $id, host: '$DNS_NAME.$DNS_ZONE:$MONGOD_PORT' }" )
    ((id++))
  done
  ALL_MEMBERS=$(IFS=","; echo "${MEMBERS[*]}")
  
  run kubectl exec $KUBECTL_ARGS $MONGOS_POD -i mongo mongodb://${REPLICA_SET_DNS_NAMES[0]}.$DNS_ZONE:$MONGOD_PORT <<EOF
rs.initiate({
    _id: "$REPLICA_SET_NAME",
    version: 1,
    members: [$ALL_MEMBERS]
})
EOF

done

#***************************#
#***  INITIALIZE SHARDS  ***#
#***************************#

for REPLICA_SET_INDEX in "${!MONGO_REPLICA_SETS[@]}"; do

  # Skip the first replica set - config
  if [[ $REPLICA_SET_INDEX -gt 0 ]]; then

    REPLICA_SET_NAME=${MONGO_REPLICA_SETS[$REPLICA_SET_INDEX]}
    REPLICA_SET_DNS_NAMES=( ${MONGO_DNS_NAMES[$REPLICA_SET_INDEX]} )

    MEMBERS=()
    for DNS_NAME in ${REPLICA_SET_DNS_NAMES[*]}; do
      MEMBERS+=( $DNS_NAME.$DNS_ZONE:$MONGOD_PORT )
    done
    ALL_MEMBERS=$(IFS=","; echo "${MEMBERS[*]}")

    echo "sh.addShard(\"$REPLICA_SET_NAME/$ALL_MEMBERS\")" \
        | run kubectl exec $KUBECTL_ARGS $MONGOS_POD -i mongo
  fi
done

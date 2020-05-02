# Find the load balancer ip and port for the service a list of kubernetes services.  
# We assume the pod name matches the service name.
get_services() {
  NODES=()
  
  while [[ $# -gt 0 ]]; do
    if [[ $# -eq 1 ]]; then
      echo "ERROR: Invalid number of arguments to get_services"
      exit
    fi

    local _CONTEXT="$1"; shift
    local _SERVICE_NAME="$1"; shift

    run kubectl wait pod/$_SERVICE_NAME --for=condition=Ready --context="$_CONTEXT" --timeout=5m

    local _IP=$(run kubectl --context="$_CONTEXT" get service $_SERVICE_NAME -o 'jsonpath={.status.loadBalancer.ingress[0].ip}')
    while [[ $_IP == "" ]]; do
      run sleep 30
      _IP=$(run kubectl --context="$_CONTEXT" get service "$_SERVICE_NAME" -o 'jsonpath={.status.loadBalancer.ingress[0].ip}')
    done
    local _PORT=$(run kubectl --context="$_CONTEXT" get service "$_SERVICE_NAME" -o 'jsonpath={.spec.ports[0].port}')

    NODES+=( $_IP:$_PORT )
  done
}

initialize_replica_set() {
  REPLICA_SET_NAME="$1"; shift

  get_services "$@"

  MEMBERS=()
  local _id=0
  local _MEMBER
  for _MEMBER in "${NODES[@]}"; do
    MEMBERS+=( "{ _id: $_id, host: '$_MEMBER' }" )
    ((_id++))
  done

  # connect to the first pod to initialize the replicaset, and:
  # 1. Init with for replicaset
  # 2. Wait for all replicas to initialize
  # 3. Add admin user
  run kubectl exec -i --context="$1" "$2" -- mongo --quiet admin <<EOF
try {
  // get status
  var status = rs.status();
  var userAdded = false;

  // login if needed
  if ( ! status.ok ) {
    if (status.code === 13) {
      if ( ! db.auth('$ADMIN_USERNAME','$ADMIN_PASSWORD') ) {
        throw "ERROR: Cannot authenticate using root user";
      }
      var userAdded = true;
      var status=rs.status();
    }
  }

  if ( ! status.ok ) {
    // status.code of 94 means the configuration is missing
    if (status.code === 94) {
      var status=rs.initiate({
        _id: '$REPLICA_SET_NAME',
        members: [$(IFS=","; echo "${MEMBERS[*]}")]
      });
    }
  }

  if ( ! status.ok ) {
    // Status should be ok by this point, exit with error if not
    throw status;
  }

  // Wait for all members to have a status of PRIMARY or SECONDARY.
  var status = rs.status();
  var all_members = status.members.map(s => s.stateStr);
  var ready_members=all_members.filter(s => s === "PRIMARY" || s === "SECONDARY");
  var wait_time=5;
  while ( all_members.length != ready_members.length ) {
    print( ready_members.length + " of " + all_members.length + " member ready [" + all_members.toString() + "]; waiting " + wait_time + " seconds");
    sleep( wait_time * 1000 );
    var status = rs.status();
    var all_members = status.members.map(s => s.stateStr);
    var ready_members=all_members.filter(s => s === "PRIMARY" || s === "SECONDARY");
    var wait_time = Math.min( wait_time * 2, 30 );
  }

  if ( ! userAdded ) {
    // we need to connect to the primary node in order to add a user
    connect('mongodb://$(IFS=","; echo "${NODES[*]}")/admin');
    db.createUser(
      {
        user: '$ADMIN_USERNAME',
        pwd: '$ADMIN_PASSWORD', 
        roles: [ 
          { role: "root", db: "admin" }
        ]
      }
    );
    var userAdded = true;
  }

  if (all_members.length === ready_members.length && userAdded) {
    // SUCCESS!
    printjson(status);
    quit(0);
  }
} catch (e) {
  printjson(e);
  quit(198);
}
quit(199); // should never get here
EOF
}

initialize_shard() {
  # Shards need to be initialized as a replica set first.
  initialize_replica_set "$@"

  # Retrieve admin username and password if not yet done
  if [[ "$MONGO_CONNECTION" == "" ]]; then
    local _ADMIN_USER=$( \
        kubectl get secret mongo-shared-secrets -o jsonpath='{.data.admin-username}' \
        | base64 --decode \
        | sed 's/\@/\%40/g' \
    )
    local _ADMIN_PASSWORD=$( \
        kubectl get secret mongo-shared-secrets -o jsonpath='{.data.admin-password}' \
        | base64 --decode \
        | sed 's/\@/\%40/g' \
    )
    MONGO_CONNECTION="mongodb://$_ADMIN_USER:$_ADMIN_PASSWORD@localhost:27017/admin"
  fi

  # Once initialized, the shard needs to be added to the config servers via the query router.
  # REPLICA_SET_NAME and NODES are set by initialize_replica_set
  run kubectl exec -i --context=payx-demo-us-east deployment/mdw20-mongos -- mongo "$MONGO_CONNECTION" <<EOF
var shard = sh.addShard('$REPLICA_SET_NAME/$(IFS=","; echo "${NODES[*]}")');
printjson(shard);
quit(shard.ok?0:200)
EOF
}

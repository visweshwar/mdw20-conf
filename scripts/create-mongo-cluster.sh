#!/bin/bash

#*************#
#*** SETUP ***#
#*************#

THIS_DIR=$(dirname "$0")
. "$THIS_DIR/functions/support_functions.sh"
. "$THIS_DIR/functions/mongodb_functions.sh"

#**********************************#
#***  CREATE REPLICA SET NODES  ***#
#**********************************#

# Generate random keyfile for mongo internal communication
KEYFILE="$THIS_DIR/kubernetes/mongodb/shared/base/build/keyfile"
if [[ ! -f "$KEYFILE" ]]; then
  run mkdir -p $(dirname "$KEYFILE")
  run openssl rand -base64 755 > "$KEYFILE"
fi

# Ask user for admin username, if it doesn't already exist
promptForVariable \
  ADMIN_USERNAME                                                  `# VARIABLE_NAME` \
  "$THIS_DIR/kubernetes/mongodb/shared/base/build/admin-username" `# SAVE_TO_FILE` \
  "Enter admin username"                                          `# PROMPT`

# Ask user for admin password, if it doesn't already exist
promptForVariable \
  ADMIN_PASSWORD                                                  `# VARIABLE_NAME` \
  "$THIS_DIR/kubernetes/mongodb/shared/base/build/admin-password" `# SAVE_TO_FILE` \
  "Enter admin password"                                          `# PROMPT`

# Deploy the mongo nodes to Kubernetes
for CLUSTER in payx-demo-us-east payx-demo-us-west payx-demo-eu; do

  # Namespace for apps
  run kustomize build kubernetes/clusters/overlays/namespace \
    | log_stdin \
    | run kubectl apply -f - --context=$CLUSTER

  # Create mongo nodes
  run kustomize build kubernetes/clusters/overlays/$CLUSTER \
    | log_stdin \
    | run kubectl apply -f - --context=$CLUSTER

done

#*************************************#
#*** Initialize config servers  ***#
#*************************************#

# Config Replica Set - 1 Node in US East, 1 Node in US West, 1 Node in EU
initialize_replica_set \
  MDW20-config `# Replica Set` \
  payx-demo-us-east mdw20-config-mongod-0 `# Node` \
  payx-demo-us-west mdw20-config-mongod-0 `# Node` \
  payx-demo-eu mdw20-config-mongod-0 `# Node`

#*****************************#
#***  Deploy query router  ***#
#*****************************#

# create file with config server IPs and Ports
# NODES was set above when the config servers were initialized
CONFIG_REPLICA_SET_MEMBERS_FILE="$THIS_DIR/kubernetes/mongodb/mongos/overlays/MDW20/build/config-replica-set-members"
echo -n $(IFS=","; echo "${NODES[*]}") > "$CONFIG_REPLICA_SET_MEMBERS_FILE"

# Deploy the query router to Kubernetes
run kustomize build kubernetes/mongodb/mongos/overlays/MDW20 \
  | log_stdin \
  | kubectl apply -f - --context=payx-demo-us-east

# Wait for the query router to become ready
run kubectl wait deployment/mdw20-mongos --for=condition=Available --context=payx-demo-us-east --timeout=5m

#**************************#
#***  Configure shards  ***#
#**************************#

# US Standard Replica Set - 1 node in US East
initialize_shard \
  MDW20-us `# Replica Set` \
  payx-demo-us-east mdw20-us-mongod-0 `# Node`

# US Premium Replica Set - 2 Nodes in US East, 1 Node in US West
initialize_shard \
  MDW20-us-premium `# Replica Set` \
  payx-demo-us-east mdw20-us-premium-mongod-0 `# Node` \
  payx-demo-us-east mdw20-us-premium-mongod-1 `# Node` \
  payx-demo-us-west mdw20-us-premium-mongod-0 `# Node`

# US Archive Replica Set - 1 Node in US East, 2 Nodes in US West
initialize_shard \
  MDW20-us-archive `# Replica Set` \
  payx-demo-us-east mdw20-us-archive-mongod-0 `# Node` \
  payx-demo-us-west mdw20-us-archive-mongod-0 `# Node` \
  payx-demo-us-west mdw20-us-archive-mongod-1 `# Node`

# EU Replica Set - 1 node in EU
initialize_shard \
  MDW20-eu `# Replica Set` \
  payx-demo-eu mdw20-eu-mongod-0 `# Node` 

#*****************#
#*** CLEAN UP  ***#
#*****************#

echo "SUCCESS"

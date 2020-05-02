#!/bin/bash

#*************#
#*** SETUP ***#
#*************#

THIS_DIR=$(dirname "$0")
. "$THIS_DIR/functions/support_functions.sh"
. "$THIS_DIR/functions/az_functions.sh"

#***********************************#
#***  CREATE CONTAINER REGISTRY  ***#
#***********************************#

promptForVariable \
  AZ_ACR_SUBSCRIPTION                                  `# VARIABLE_NAME` \
  "$THIS_DIR/build/acr/subscription"                   `# SAVE_TO_FILE` \
  "Enter the subscription for the container registry"  `# PROMPT`

promptForVariable \
  AZ_ACR_RESOURCE_GROUP_NAME                             `# VARIABLE_NAME` \
  "$THIS_DIR/build/acr/resource_group"                   `# SAVE_TO_FILE` \
  "Enter the resource group for the container registry"  `# PROMPT`

promptForVariable \
  AZ_ACR_NAME                              `# VARIABLE_NAME` \
  "$THIS_DIR/build/acr/name"               `# SAVE_TO_FILE` \
  "Enter name for the container registry"  `# PROMPT`

createContainerRegistry \
  "$AZ_ACR_SUBSCRIPTION"       `# SUBSCRIPTION` \
  $AZ_ACR_RESOURCE_GROUP_NAME  `# RESOURCE_GROUP_NAME` \
  $AZ_ACR_NAME                 `# ACR_NAME`

#*************************#
#***  CREATE CLUSTERS  ***#
#*************************#

for AZ_AKS_NAME in payx-demo-us-east payx-demo-us-west payx-demo-eu; do
  promptForVariable \
    AZ_AKS_SUBSCRIPTION                                               `# VARIABLE_NAME` \
    "$THIS_DIR/build/aks/$AZ_AKS_NAME/subscription"                   `# SAVE_TO_FILE` \
    "Enter the subscription for the kubernetes cluster $AZ_AKS_NAME"  `# PROMPT`
  
  promptForVariable \
    AZ_AKS_RESOURCE_GROUP_NAME                                          `# VARIABLE_NAME` \
    "$THIS_DIR/build/aks/$AZ_AKS_NAME/resource_group"                   `# SAVE_TO_FILE` \
    "Enter the resource group for the kubernetes cluster $AZ_AKS_NAME"  `# PROMPT`

  promptForVariable \
    AZ_AKS_LOCATION                                               `# VARIABLE_NAME` \
    "$THIS_DIR/build/aks/$AZ_AKS_NAME/location"                   `# SAVE_TO_FILE` \
    "Enter the location for the kubernetes cluster $AZ_AKS_NAME"  `# PROMPT`

  if [[ $CLUSTER == payx-demo-us-east ]]; then
    AZ_AKS_NUMBER_OF_COMPUTE_NODES=2
  else
    AZ_AKS_NUMBER_OF_COMPUTE_NODES=1
  fi

  createCluster \
    "$AZ_AKS_SUBSCRIPTION" \
    $AZ_AKS_RESOURCE_GROUP_NAME \
    $AZ_AKS_NAME \
    $AZ_AKS_LOCATION \
    $AZ_AKS_NUMBER_OF_COMPUTE_NODES 
done

#********************************#
#***  UPDATE KUBECTL CONTEXT  ***#
#********************************#

# Update the kubectl config context
"$THIS_DIR/update-kubectl-context.sh"

echo "SUCCESS"

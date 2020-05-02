#!/bin/bash

#*************#
#*** SETUP ***#
#*************#

THIS_DIR=$(dirname "$0")
. "$THIS_DIR/functions/support_functions.sh"

#********************************#
#***  UPDATE KUBECTL CONTEXT  ***#
#********************************#

for AZ_AKS_NAME in payx-demo-us-east payx-demo-us-west payx-demo-eu; do

  promptForVariable \
    AZ_AKS_SUBSCRIPTION                                               `# VARIABLE_NAME` \
    "$THIS_DIR/build/aks/$AZ_AKS_NAME/subscription"                   `# SAVE_TO_FILE` \
    "Enter the subscription for the kubernetes cluster $AZ_AKS_NAME"  `# PROMPT`

  promptForVariable \
    AZ_AKS_RESOURCE_GROUP_NAME                                          `# VARIABLE_NAME` \
    "$THIS_DIR/build/aks/$AZ_AKS_NAME/resource_group"                   `# SAVE_TO_FILE` \
    "Enter the resource group for the kubernetes cluster $AZ_AKS_NAME"  `# PROMPT`

  run az aks get-credentials \
    --name=$AZ_AKS_NAME \
    --subscription="$AZ_AKS_SUBSCRIPTION" \
    --resource-group=$AZ_AKS_RESOURCE_GROUP_NAME \
    --overwrite-existing

  run kubectl config set-context --current --namespace=mdw20

done

run kubectl config use-context payx-demo-us-east

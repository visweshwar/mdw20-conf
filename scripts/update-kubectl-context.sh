#!/bin/bash

#*************#
#*** SETUP ***#
#*************#

set -e # Exit on any error

THIS_DIR=$(dirname "$0")
. $THIS_DIR/config.sh

run() {
  set -x # Echo command
  "${@}"
  { set +x; } 2>/dev/null  # Turn off echo without writing to console
}

#********************************#
#***  UPDATE KUBECTL CONTEXT  ***#
#********************************#

run az aks get-credentials \
  --name=$AZ_AKS_NAME_US_EAST \
  --subscription=$AZ_AKS_SUBSCRIPTION_US_EAST \
  --resource-group=$AZ_RESOURCE_GROUP_NAME \
  --overwrite-existing

run kubectl config set-context --current --namespace=$MONGO_K8S_NAMESPACE

run az aks get-credentials \
  --name=$AZ_AKS_NAME_US_WEST \
  --subscription=$AZ_AKS_SUBSCRIPTION_US_WEST \
  --resource-group=$AZ_RESOURCE_GROUP_NAME \
  --overwrite-existing

run kubectl config set-context --current --namespace=$MONGO_K8S_NAMESPACE

run az aks get-credentials \
  --name=$AZ_AKS_NAME_EU \
  --subscription=$AZ_AKS_SUBSCRIPTION_EU \
  --resource-group=$AZ_RESOURCE_GROUP_NAME \
  --overwrite-existing

run kubectl config set-context --current --namespace=$MONGO_K8S_NAMESPACE

run kubectl config use-context $AZ_AKS_NAME_US_EAST

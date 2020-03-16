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

#********************************#
#***  CREATE RESOURCE GROUPS  ***#
#********************************#

for SUBSCRIPTION in $( tr ' ' '\n' <<< "$AZ_SUBSCRIPTION $AZ_AKS_SUBSCRIPTION_US_EAST $AZ_AKS_SUBSCRIPTION_US_WEST $AZ_AKS_SUBSCRIPTION_EU" | sort | uniq); do
  RG_ID=$( run az group list --query="[?name=='$AZ_RESOURCE_GROUP_NAME']|[0].id" --output=tsv --subscription=$SUBSCRIPTION )
  if [[ -z "$RG_ID" ]]; then
    RG_ID=$( run az group create \
      --name=$AZ_RESOURCE_GROUP_NAME \
      --location=$AZ_LOCATION \
      --subscription=$SUBSCRIPTION \
    )
  else
    echo "SKIPING: Resource Group '$RG_ID' already exists"
  fi
done

#***********************************#
#***  CREATE CONTAINER REGISTRY  ***#
#***********************************#

AZ_FLAGS="--subscription=$AZ_SUBSCRIPTION --resource-group=$AZ_RESOURCE_GROUP_NAME"
ACR_ID=$( run az acr list --query="[?name=='$AZ_ACR_NAME']|[0].id" --output=tsv $AZ_FLAGS )
if [[ -z "$ACR_ID" ]]; then
  ACR_ID=$( run az acr create $AZ_FLAGS \
      --name $AZ_ACR_NAME \
      --sku basic \
      --query=id \
      --output=tsv \
  )
else
  echo "SKIPING: Container Registry '$ACR_ID' already exists"
fi

#*************************#
#***  CREATE DNS ZONE  ***#
#*************************#

AZ_FLAGS="--subscription=$AZ_SUBSCRIPTION --resource-group=$AZ_RESOURCE_GROUP_NAME"
DNS_ID=$( run az network dns zone list --query="[?name=='$DNS_ZONE']|[0].id" --output=tsv $AZ_FLAGS )
if [[ -z "$DNS_ID" ]]; then
  DNS_ID=$( run az network dns zone create \
      --name $DNS_ZONE \
      --query=id \
      --output=tsv \
      $AZ_FLAGS \
  )
else
  echo "SKIPING: Key Vault '$DNS_ID' already exists"
fi

#*************************#
#***  CREATE CLUSTERS  ***#
#*************************#

AZ_FLAGS="--subscription=$AZ_AKS_SUBSCRIPTION_US_EAST --resource-group=$AZ_RESOURCE_GROUP_NAME"
AKS_ID=$( run az aks list --query="[?name=='$AZ_AKS_NAME_US_EAST']|[0].id" --output=tsv $AZ_FLAGS )
if [[ -z "$AKS_ID" ]]; then
  AKS_ID=$( run az aks create $AZ_FLAGS \
      --name=$AZ_AKS_NAME_US_EAST \
      --attach-acr=$ACR_ID \
      --disable-rbac \
      --load-balancer-sku=basic \
      --location=$AZ_AKS_LOCATION_US_EAST \
      --vm-set-type VirtualMachineScaleSets \
      --node-count 2 \
      --node-vm-size $AZ_AKS_COMPUTE_NODE_VM_SIZE \
  )
else
  echo "SKIPING: AKS Cluster '$AKS_ID' already exists"
fi

AZ_FLAGS="--subscription=$AZ_AKS_SUBSCRIPTION_US_WEST --resource-group=$AZ_RESOURCE_GROUP_NAME"
AKS_ID=$( run az aks list --query="[?name=='$AZ_AKS_NAME_US_WEST']|[0].id" --output=tsv $AZ_FLAGS )
if [[ -z "$AKS_ID" ]]; then
  AKS_ID=$( run az aks create $AZ_FLAGS \
      --name=$AZ_AKS_NAME_US_WEST \
      --attach-acr=$ACR_ID \
      --disable-rbac \
      --load-balancer-sku=basic \
      --location=$AZ_AKS_LOCATION_US_WEST \
      --vm-set-type VirtualMachineScaleSets \
      --node-count 1 \
      --node-vm-size $AZ_AKS_COMPUTE_NODE_VM_SIZE \
  )
else
  echo "SKIPING: AKS Cluster '$AKS_ID' already exists"
fi

AZ_FLAGS="--subscription=$AZ_AKS_SUBSCRIPTION_EU --resource-group=$AZ_RESOURCE_GROUP_NAME"
AKS_ID=$( run az aks list --query="[?name=='$AZ_AKS_NAME_EU']|[0].id" --output=tsv $AZ_FLAGS )
if [[ -z "$AKS_ID" ]]; then
  AKS_ID=$( run az aks create $AZ_FLAGS \
      --name=$AZ_AKS_NAME_EU \
      --attach-acr=$ACR_ID \
      --disable-rbac \
      --load-balancer-sku=basic \
      --location=$AZ_AKS_LOCATION_EU \
      --vm-set-type VirtualMachineScaleSets \
      --node-count 1 \
      --node-vm-size $AZ_AKS_COMPUTE_NODE_VM_SIZE \
  )
else
  echo "SKIPING: AKS Cluster '$AKS_ID' already exists"
fi

#******************#
#***  CLEAN UP  ***#
#******************#

# Update the kubectl config context
"$THIS_DIR/update-kubectl-context.sh"

echo "SUCCESS"

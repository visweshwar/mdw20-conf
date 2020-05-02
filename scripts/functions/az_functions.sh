createContainerRegistry() {
    local _SUBSCRIPTION="$1"; shift
    local _RESOURCE_GROUP_NAME="$1"; shift
    local _ACR_NAME="$1"; shift

    AZ_ACR_ID=$( run az acr list \
        --query="[?name=='$AZ_ACR_NAME']|[0].id" \
        --output=tsv $_AZ_FLAGS \
        --subscription="$_SUBSCRIPTION" \
        --resource-group=$_RESOURCE_GROUP_NAME \
    )
    if [[ -z "$AZ_ACR_ID" ]]; then
        AZ_ACR_ID=$( run az acr create $_AZ_FLAGS \
            --name $_ACR_NAME \
            --sku basic \
            --query=id \
            --output=tsv \
            --subscription="$_SUBSCRIPTION" \
            --resource-group=$_RESOURCE_GROUP_NAME \
        )
    else
        echo "SKIPPING: Container Registry '$AZ_ACR_ID' already exists";
    fi
}

# Creates an Azure Kubernetes Cluster
# Expects createContainerRegistry to be called prior
createCluster() {
    if [[ -z $AZ_ACR_ID ]]; then
      echo "ERROR: missing call to createContainerRegistry"
      exit 200
    fi

    if [[ $# -ne 5 ]]; then
        echo "ERROR: invalid number of arguments to createCluster"
        exit 201
    fi

    local _SUBSCRIPTION="$1"; shift
    local _RESOURCE_GROUP_NAME="$1"; shift
    local _AKS_NAME="$1"; shift
    local _LOCATION="$1"; shift
    local _NUMBER_OF_COMPUTE_NODES="$1"; shift

    AZ_AKS_ID=$( run az aks list \
        --query="[?name=='$_AKS_NAME']|[0].id" \
        --output=tsv $_AZ_FLAGS \
        --subscription="$_SUBSCRIPTION" \
        --resource-group=$_RESOURCE_GROUP_NAME \
    )
    if [[ -z "$AZ_AKS_ID" ]]; then
        AZ_AKS_ID=$( run az aks create \
            --name=$_AKS_NAME \
            --attach-acr=$AZ_ACR_ID \
            --disable-rbac \
            --load-balancer-sku=basic \
            --location=$_LOCATION \
            --vm-set-type VirtualMachineScaleSets \
            --node-count $_NUMBER_OF_COMPUTE_NODES \
            --node-vm-size Standard_B2s \
            --subscription="$_SUBSCRIPTION" \
            --resource-group=$_RESOURCE_GROUP_NAME \
        )
    else
        echo "SKIPPING: AKS Cluster '$AZ_AKS_ID' already exists"
    fi
}

# Azure subscription to use for all resources (except AKS clusters)
AZ_SUBSCRIPTION=7cdaab60-f457-444b-b525-1c77d9af47b9

# Azure resource group name for ressources
AZ_RESOURCE_GROUP_NAME=payx-demo

# Azure location to use for all resources (except AKS clusters)
AZ_LOCATION=eastus2

# Name for the container registry (must be gobally unique)
AZ_ACR_NAME=payxdemo

# DNS Zone that will resolve names for our AKS clusters
DNS_ZONE=payx-demo.com

# Azure subscription, name, and location for US East AKS Cluster
AZ_AKS_SUBSCRIPTION_US_EAST=f4c5f01b-343a-4d39-be0c-27951f7cd6db
AZ_AKS_NAME_US_EAST=payx-demo-us-east
AZ_AKS_LOCATION_US_EAST=eastus2

# Azure subscription, name, and location for US West AKS Cluster
AZ_AKS_SUBSCRIPTION_US_WEST=e0722b85-fc5b-4743-803e-a5444ab9c8c0
AZ_AKS_NAME_US_WEST=payx-demo-us-west
AZ_AKS_LOCATION_US_WEST=westus2

# Azure subscription, name, and location for Europe AKS Cluster
AZ_AKS_SUBSCRIPTION_EU=7cdaab60-f457-444b-b525-1c77d9af47b9
AZ_AKS_NAME_EU=payx-demo-eu
AZ_AKS_LOCATION_EU=northeurope

# Azure node types for compute nodes in AKS Clusters
AZ_AKS_COMPUTE_NODE_VM_SIZE=Standard_B2s

# Kubernetes namespace to deploy mongo nodes to
MONGO_K8S_NAMESPACE=mdw20

# Mongo cluster name
MONGO_CLUSTER_NAME=MDW20

# Mongo nodes in US East cluster
MONGO_US_EAST=( \
  `#  REPLICA SET NAME  NUMBER OF NODES` \
  `#  ================  ===============` \
      MDW20-config      1 \
      MDW20-us          1 \
      MDW20-us-premium  2 \
      MDW20-us-archive  1 \
)

# First replica set is assumed to be the config, the rest shards
MONGO_REPLICA_SETS=(   MDW20-config  MDW20-us  MDW20-eu  MDW20-us-premium  MDW20-us-archive )
MONGO_DNS_SRV_NAMES=(  mdw20-config  mdw20-us  mdw20-eu  mdw20-us-premium  mdw20-us-archive )

#
# For each replica set, indicate the clusters that will have a mongod instance.  If you want multiple instances
# in a cluster, repeat the cluster name for each instance you want
#
MONGO_CLUSTERS=(  \
    `#                    CLUSTER 1                      CLUSTER 2                      CLUSTER 3` \
    `#                    -----------------              -----------------              -----------------` \
    `# MDW20-config`     "payx-demo-us-east              payx-demo-us-west              payx-demo-eu" \
    `# MDW20-us`         "payx-demo-us-east" \
    `# MDW20-eu`         "payx-demo-eu" \
    `# MDW20-us-premium` "payx-demo-us-east              payx-demo-us-east              payx-demo-us-west" \
    `# MDW20-us-archive` "payx-demo-us-west              payx-demo-us-west              payx-demo-us-east" \
)

MONGO_DNS_NAMES=(  \
    `#                    DNS NAME 1                     DNS NAME 2                     DNS NAME 3` \
    `#                    -----------------------------  -----------------------------  ---------------------------` \
    `# MDW20-config`     "rs-us-east.mdw20-config        rs-west.mdw20-config           rs-eu.mdw20-config" \
    `# MDW20-us`         "rs-us-east.mdw20-us" \
    `# MDW20-eu`         "rs-eu.mdw20-eu" \
    `# MDW20-us-premium` "rs-us-east-1.mdw20-us-premium  rs-us-east-2.mdw20-us-premium  rs-us-west.mdw20-us-premium" \
    `# MDW20-us-archive` "rs-us-west-1.mdw20-us-archive  rs-us-west-2.mdw20-us-archive  rs-us-east.mdw20-us-archive" \
)

MONGOD_PORT=27017 # Port the mongod instance listens on
MONGOD_AZ_STORAGE_CLASS=default # Azure storage class for mounted storage
MONGOD_STORAGE_SIZE=5Gi # Size of storage for mongod image

MONGOS_CLUSTER=payx-demo-us-east
MONGOS_PORT=27017 # PORT the mongos instance listens on
MONGOS_DNS_NAME=mdw20 # DNS Name for the standalone query server

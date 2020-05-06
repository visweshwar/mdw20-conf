//initialize the database
var db = db.getSisterDB('client');
//clear database
db.dropDatabase()

//load data
var file = cat("testdata/clients.json")
var cltArr = JSON.parse(file)
cltArr.forEach(clt => {
    clt.region = clt.country == "USA" ? "us" : "eu";
})
db.clientDetails.insertMany(cltArr);

//add shard tags
sh.addShardTag("us-standard-rs", "US-S");
sh.addShardTag("eu-standard-rs", "EU-S");

sh.enableSharding("client");
db.clientDetails.ensureIndex({region: 1, clientId: 1}, {name: "pre_ctry_cid_idx"});
//shard the client collection
sh.shardCollection('client.clientDetails', {region: 1, clientId: 1});

sh.addTagRange(
    "client.clientDetails",
    {"region": "us", "clientId": MinKey},
    {"region": "us", "clientId": MaxKey},
    "US-S"
);

sh.addTagRange(
    "client.clientDetails",
    {"region": "eu", "clientId": MinKey},
    {"region": "eu", "clientId": MaxKey},
    "EU-S"
);

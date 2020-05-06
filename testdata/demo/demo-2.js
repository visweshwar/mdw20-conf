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
//initialize the database
var db = db.getSisterDB('client');

sh.addShardTag("us-standard-rs", "US-S");
sh.addShardTag("us-premium-rs", "US-P");
sh.addShardTag("eu-standard-rs", "EU-S");
sh.addShardTag("eu-premium-rs", "EU-P");

sh.enableSharding("client");
db.clientDetails.ensureIndex({region: 1, premium: 1, clientId: 1}, {name: "pre_ctry_cid_idx"});
//shard the client collection on premium or not
sh.shardCollection('client.clientDetails', {region: 1, premium: 1, clientId: 1});

sh.addTagRange(
    "client.clientDetails",
    {"region": "us", "premium": false, "clientId": MinKey},
    {"region": "us", "premium": false, "clientId": MaxKey},
    "US-S"
);

sh.addTagRange(
    "client.clientDetails",
    {"region": "us", "premium": true, "clientId": MinKey},
    {"region": "us", "premium": true, "clientId": MaxKey},
    "US-P"
);

sh.addTagRange(
    "client.clientDetails",
    {"region": "eu", "premium": false, "clientId": MinKey},
    {"region": "eu", "premium": false, "clientId": MaxKey},
    "EU-S"
);

sh.addTagRange(
    "client.clientDetails",
    {"region": "eu", "premium": true, "clientId": MinKey},
    {"region": "eu", "premium": true, "clientId": MaxKey},
    "EU-P"
);
  
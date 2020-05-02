//initialize the database
var db = db.getSisterDB('client');

sh.addShardTag("us-standard-rs", "US-S");
sh.addShardTag("us-premium-rs", "US-P");
sh.addShardTag("us-archive-rs", "US-A");
sh.addShardTag("eu-standard-rs", "EU-S");
sh.addShardTag("eu-premium-rs", "EU-P");
sh.addShardTag("eu-archive-rs", "EU-A");

sh.enableSharding("client");
db.clientDetails.ensureIndex({country: 1, active: 1, premium: 1, clientId: 1}, {name: "pre_ctry_cid_idx"});
//shard the client collection on premium or not
sh.shardCollection('client.clientDetails', {country: 1, active: 1, premium: 1, clientId: 1});

sh.addTagRange(
    "client.clientDetails",
    {"country": "USA", "active": true, "premium": false, "clientId": MinKey},
    {"country": "USA", "active": true, "premium": false, "clientId": MaxKey},
    "US-S"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "USA", "active": true, "premium": true, "clientId": MinKey},
    {"country": "USA", "active": true, "premium": true, "clientId": MaxKey},
    "US-P"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "USA", "active": false, "premium": true, "clientId": MinKey},
    {"country": "USA", "active": false, "premium": true, "clientId": MaxKey},
    "US-A"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "USA", "active": false, "premium": false, "clientId": MinKey},
    {"country": "USA", "active": false, "premium": false, "clientId": MaxKey},
    "US-A"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "UK", "active": true, "premium": false, "clientId": MinKey},
    {"country": "UK", "active": true, "premium": false, "clientId": MaxKey},
    "EU-S"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "UK", "active": true, "premium": true, "clientId": MinKey},
    {"country": "UK", "active": true, "premium": true, "clientId": MaxKey},
    "EU-P"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "UK", "active": false, "premium": true, "clientId": MinKey},
    {"country": "UK", "active": false, "premium": true, "clientId": MaxKey},
    "EU-A"
);

sh.addTagRange(
    "client.clientDetails",
    {"country": "UK", "active": false, "premium": false, "clientId": MinKey},
    {"country": "UK", "active": false, "premium": false, "clientId": MaxKey},
    "EU-A"
);

db.employees.ensureIndex({country: 1, active: 1, premium: 1, employeeId: 1}, {name: "pre_ctry_eid_idx"});
//shard the client collection on premium or not
sh.shardCollection('client.employees', {country: 1, active: 1, premium: 1, employeeId: 1});

sh.addTagRange(
    "client.employees",
    {"country": "USA", "active": true, "premium": false, "employeeId": MinKey},
    {"country": "USA", "active": true, "premium": false, "employeeId": MaxKey},
    "US-S"
);

sh.addTagRange(
    "client.employees",
    {"country": "USA", "active": true, "premium": true, "employeeId": MinKey},
    {"country": "USA", "active": true, "premium": true, "employeeId": MaxKey},
    "US-P"
);


sh.addTagRange(
    "client.employees",
    {"country": "USA", "active": false, "premium": true, "employeeId": MinKey},
    {"country": "USA", "active": false, "premium": true, "employeeId": MaxKey},
    "US-A"
);
sh.addTagRange(
    "client.employees",
    {"country": "USA", "active": false, "premium": false, "employeeId": MinKey},
    {"country": "USA", "active": false, "premium": false, "employeeId": MaxKey},
    "US-A"
);

sh.addTagRange(
    "client.employees",
    {"country": "UK", "active": true, "premium": false, "employeeId": MinKey},
    {"country": "UK", "active": true, "premium": false, "employeeId": MaxKey},
    "EU-S"
);

sh.addTagRange(
    "client.employees",
    {"country": "UK", "active": true, "premium": true, "employeeId": MinKey},
    {"country": "UK", "active": true, "premium": true, "employeeId": MaxKey},
    "EU-P"
);

sh.addTagRange(
    "client.employees",
    {"country": "UK", "active": false, "premium": true, "employeeId": MinKey},
    {"country": "UK", "active": false, "premium": true, "employeeId": MaxKey},
    "EU-A"
);

sh.addTagRange(
    "client.employees",
    {"country": "UK", "active": false, "premium": false, "employeeId": MinKey},
    {"country": "UK", "active": false, "premium": false, "employeeId": MaxKey},
    "EU-A"
);

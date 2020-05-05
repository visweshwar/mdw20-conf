var db = db.getSisterDB('client');
var REGION = 'eu'

var clients = []
var cltCursor = db.clientDetails.find({region: REGION}).projection({'clientId': 1, 'premium': 1, 'country': 1, _id: 0});
cltCursor.count()
cltCursor.forEach(function (clt) {
    clients.push(clt)
});
var ee1 = cat("testdata/employees-1.json")
var ee2 = cat("testdata/employees-2.json")
var ee3 = cat("testdata/employees-3.json")
var ee4 = cat("testdata/employees-4.json")

var eeArr = ((JSON.parse(ee3))
    .concat(JSON.parse(ee4)));

eeArr.forEach(function (ee, position) {

    ee.clientId = clients[position % 4].clientId;
    ee.premium = clients[position % 4].premium;
    ee.country = clients[position % 4].country;
    ee.region = ee.country == "USA" ? "us" : "eu";
});


// db.employee.insertMany(eeArr);

printjson(eeArr.filter(ee => ee.region == REGION));
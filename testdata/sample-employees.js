var db = db.getSisterDB('client');

db.clientDetails.remove({auto: true})
var file = cat("testdata/clients.json")
var cltArr = JSON.parse(file)
db.clientDetails.insertMany(cltArr);

var clients = []
var cltCursor = db.clientDetails.find({}).projection({'clientId': 1, 'premium': 1, 'country': 1, _id: 0});
cltCursor.count()

cltCursor.forEach(function (clt) {
    clients.push(clt)
});
var ee1 = cat("testdata/employees-1.json")
var ee2 = cat("testdata/employees-2.json")
var ee3 = cat("testdata/employees-3.json")
var ee4 = cat("testdata/employees-4.json")

var eeArr = (JSON.parse(ee1)
    .concat(JSON.parse(ee2))
    .concat(JSON.parse(ee3))
    .concat(JSON.parse(ee4)));

eeArr.forEach(function (ee) {
    ee.clientId = clients[Math.floor(Math.random(1000) * 1000)].clientId;
    ee.premium = clients[Math.floor(Math.random(1000) * 1000)].premium;
    ee.country = clients[Math.floor(Math.random(1000) * 1000)].country;

});

// db.employee.insertMany(eeArr);

printjson(eeArr);
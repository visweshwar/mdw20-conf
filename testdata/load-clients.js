var db = db.getSisterDB('client');

db.clientDetails.remove({auto: true})
var file = cat("testdata/clients.json")
var cltArr = JSON.parse(file)
cltArr.forEach(clt => {
    clt.region = clt.country == "USA" ? "us" : "eu";
})
db.clientDetails.insertMany(cltArr);
//
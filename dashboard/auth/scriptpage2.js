
// Get a database reference to our posts
var db = firebase.database();
var ref = db.ref();

// Attach an asynchronous callback to read the data at our posts reference
let data = [];
ref.orderbykey().on("value", function(snapshot) {
    data = [];
    Object.entries(snapshot.val().officer).forEach(([key,val]) => {
        data.push({
            id: key,
            name: val.name,
            dept: val.department
            
        })
    })-

    console.log(data)
  console.log(snapshot.val().officer);
}, function (errorObject) {
  console.log("The read failed: " + errorObject.code);
});
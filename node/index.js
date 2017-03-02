var firebase = require('firebase-admin');
var request = require('request');
var config = require('./config');
var serviceAccount = require("./firebase-service-account.json");

var API_KEY = config.firebase.API_KEY;

firebase.initializeApp({
    credential: firebase.credential.cert(serviceAccount),
    databaseURL: config.firebase.databaseURL
});
ref = firebase.database().ref();

function listenForNotificationRequests() {
    var requests = ref.child('notificationRequest');
    requests.on('child_added', function(requestSnapshot) {
        var fcmToken = requestSnapshot.val().locationRequest.fcmToken;
        console.log("fcmToken = " + fcmToken);
        // 
        // sendNotificationToUser(
        //   request.username, 
        //   request.message,
        //   function() {
        //     requestSnapshot.ref.remove();
        //   }
        // );
    }, function(error) {
        console.error(error);
    });
};

function sendNotificationToUser(username, message, onSuccess) {
//   request({
//     url: 'https://fcm.googleapis.com/fcm/send',
//     method: 'POST',
//     headers: {
//       'Content-Type' :' application/json',
//       'Authorization': 'key='+API_KEY
//     },
//     body: JSON.stringify({
//       notification: {
//         title: message
//       },
//       to : '/topics/user_'+username
//     })
//   }, function(error, response, body) {
//     if (error) { console.error(error); }
//     else if (response.statusCode >= 400) { 
//       console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
//     }
//     else {
//       onSuccess();
//     }
//   });
}

listenForNotificationRequests();
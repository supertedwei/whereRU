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
    requests.on('child_added', function (requestSnapshot) {
        var fcmToken = requestSnapshot.val().locationRequest.fcmToken;
        console.log("fcmToken = " + fcmToken);
        sendNotificationToDevice(
            fcmToken,
            {
                data: {
                    command: "requestLocation"
                }
            },
            function (response) {
                requestSnapshot.ref.remove();
            }
        );
        // 
        // sendNotificationToUser(
        //   request.username, 
        //   request.message,
        //   function() {
        //     requestSnapshot.ref.remove();
        //   }
        // );
    }, function (error) {
        console.error(error);
    });
};

function sendNotificationToDevice(registrationToken, payload, callback) {
    // Set the message as high priority and have it expire after 24 hours.
    var options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };

    // Send a message to the device corresponding to the provided
    // registration token.
    firebase.messaging().sendToDevice(registrationToken, payload, options).then(function (response) {
        // See the MessagingDevicesResponse reference documentation for
        // the contents of response.
        console.log("Successfully sent message:", response);
        callback(response);
    }).catch(function (error) {
        console.log("Error sending message:", error);
    });
}

listenForNotificationRequests();
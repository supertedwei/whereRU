package com.supergigi.whereru.firebase;

/**
 * Created by tedwei on 3/2/17.
 */

public class FbNotificationRequest {

    public static FbNotificationRequest createLocationRequest(String fcmToken) {
        FbNotificationRequest fbNotificationRequest = new FbNotificationRequest();
        fbNotificationRequest.locationRequest = new LocationRequest();
        fbNotificationRequest.locationRequest.fcmToken = fcmToken;
        return fbNotificationRequest;
    }

    public static class LocationRequest {
        public String fcmToken;
    }

    public LocationRequest locationRequest;
}

package com.supergigi.whereru.firebase;

/**
 * Created by tedwei on 3/2/17.
 */

public class FbNotificationRequest {

    public static FbNotificationRequest createLocationRequest(String deviceId) {
        FbNotificationRequest fbNotificationRequest = new FbNotificationRequest();
        fbNotificationRequest.locationRequest = new LocationRequest();
        fbNotificationRequest.locationRequest.deviceId = deviceId;
        return fbNotificationRequest;
    }

    public static class LocationRequest {
        public String deviceId;
    }

    public LocationRequest locationRequest;
}

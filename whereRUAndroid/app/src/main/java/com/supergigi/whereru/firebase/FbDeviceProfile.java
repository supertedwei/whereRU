package com.supergigi.whereru.firebase;

/**
 * Created by tedwei on 28/02/2017.
 */

public class FbDeviceProfile {

    private String name;
    private String fcmToken;
    private FbLocation lastLocation;
    private boolean requestingLocation = false;

    public FbLocation getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(FbLocation lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public boolean isRequestingLocation() {
        return requestingLocation;
    }

    public void setRequestingLocation(boolean requestingLocation) {
        this.requestingLocation = requestingLocation;
    }
}

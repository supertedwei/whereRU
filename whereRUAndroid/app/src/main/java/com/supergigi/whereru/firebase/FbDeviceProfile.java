package com.supergigi.whereru.firebase;

/**
 * Created by tedwei on 28/02/2017.
 */

public class FbDeviceProfile {

    private String name;
    private FbLocation lastLocation;

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
}

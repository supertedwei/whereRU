package com.supergigi.whereru.firebase;

import com.google.firebase.database.ServerValue;

/**
 * Created by tedwei on 27/02/2017.
 */

public class FbLocation {

    private double latitude;
    private double longitude;
    private float accuracy;
    private String address;
    private Object timestamp = ServerValue.TIMESTAMP;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

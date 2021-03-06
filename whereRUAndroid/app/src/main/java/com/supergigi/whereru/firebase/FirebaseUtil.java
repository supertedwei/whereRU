package com.supergigi.whereru.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tedwei on 27/02/2017.
 */

public class FirebaseUtil {

    private static final String LOG_TAG = FirebaseUtil.class.getSimpleName();

    private static final String FIREBASE_DEVICE_LOCATION_LOG = "deviceLocationLog";
    private static final String FIREBASE_DEVICE_PROFILE = "deviceProfile";
    private static final String FIREBASE_NOTIFICATION_REQUEST = "notificationRequest";

    public static final String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getUid();
    }

    public static final DatabaseReference getDeviceLocationLog() {
        return getDeviceLocationLog(getUid());
    }

    public static final DatabaseReference getDeviceLocationLog(String deviceId) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        return rootRef.child(FIREBASE_DEVICE_LOCATION_LOG).child(deviceId);
    }

    public static final DatabaseReference getDeviceProfile() {
        String uid = getUid();
        if (uid == null) {
            return null;
        }
        return getDeviceProfileList().child(getUid());
    }

    public static final DatabaseReference getDeviceProfile(String deviceId) {
        return getDeviceProfileList().child(deviceId);
    }

    public static final DatabaseReference getDeviceProfileList() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        return rootRef.child(FIREBASE_DEVICE_PROFILE);
    }

    public static final void updateDeviceProfile(String name) {
        getDeviceProfile().child("name").setValue(name);
    }

    public static final void updateDeviceFcmToken(String token) {
        Log.d(LOG_TAG, "fcmToken = " + token);
        if (token == null) {
            return;
        }
        DatabaseReference databaseReference = getDeviceProfile();
        if (databaseReference == null) {
            return;
        }
        databaseReference.child("fcmToken").setValue(token);
    }

    public static final void updateRequestingLocation(boolean data) {
        getDeviceProfile().child("requestingLocation").setValue(data);
    }

    public static final DatabaseReference getDeviceLastLocation() {
        return getDeviceProfile().child("lastLocation");
    }

    public static final void pushNotificationRequest(FbNotificationRequest fbNotificationRequest) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child(FIREBASE_NOTIFICATION_REQUEST).push().setValue(fbNotificationRequest);
    }

}

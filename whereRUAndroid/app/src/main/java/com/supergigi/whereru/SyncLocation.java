package com.supergigi.whereru;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by tedwei on 28/02/2017.
 */

public class SyncLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String LOG_TAG = SyncLocation.class.getSimpleName();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Context context;
    private int countDown = 4;
    private float accuracyThreadhold = 100;

    public SyncLocation(Context context) {
        this.context = context;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Connected to GoogleApiClient");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "onLocationChanged = " + countDown + "/" + location);
        if (location.getAccuracy() > accuracyThreadhold && countDown > 0) {
            countDown--;
            return;
        }
        if (location != null) {
            mCurrentLocation = location;
            Log.i(LOG_TAG, "mCurrentLocation = " + mCurrentLocation);
        }
        synchronized (this) {
            notifyAll();
        }
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(LOG_TAG, "Building GoogleApiClient");
        if (mGoogleApiClient != null) {
            Log.i(LOG_TAG, "mGoogleApiClient has been initialzed");
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

        // Start the connection
        if (mGoogleApiClient != null) {
            if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void stop() {
        Log.i(LOG_TAG, "stop()");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public Location getLocationBlocking() {
        buildGoogleApiClient();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        setResyncGeofenseBlocking(mCurrentLocation);

        stop();
        return mCurrentLocation;
    }

    public void setResyncGeofenseBlocking(Location center) {
        removeGeofencesBlocking();
        addGeofencesBlocking(center);
    }

    private void addGeofencesBlocking(Location center) {
        Log.d(LOG_TAG, "Add geofences");
        final Object syncObject = new Object();
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(center),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Log.d(LOG_TAG, "[addGeofences:onResult] : success - " + status);
                    } else {
                        // Get the status code for the error and log it using a user-friendly message.
                        String errorMessage = getErrorString(context, status.getStatusCode());
                        Log.e(LOG_TAG, "[addGeofences:onResult] : fail - " + errorMessage);
                    }

                    synchronized (syncObject) {
                        syncObject.notifyAll();
                    }
                }
            }); // Result processed in onResult().

            synchronized (syncObject) {
                syncObject.wait();
            }
        } catch (SecurityException securityException) {
            Log.e(LOG_TAG, "", securityException);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private GeofencingRequest getGeofencingRequest(Location center) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(getGeofenceList(center));

        // Return a GeofencingRequest.
        return builder.build();
    }

    private ArrayList<Geofence> getGeofenceList(Location center) {
        ArrayList<Geofence> geofenceList = new ArrayList<>();
        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("Resync Geofence")

                // Set the circular region of this geofence.
                .setCircularRegion(
                        center.getLatitude(),
                        center.getLongitude(),
                        10000        // in meter
                )

                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(24L * 60L * 60L * 1000L)

                // Set the transition types of interest. Alerts are only generated for these
                // transition. We track entry and exit transitions in this sample.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)

                // Create the geofence.
                .build());
        return geofenceList;
    }

    private void removeGeofencesBlocking() {
        try {
            Log.d(LOG_TAG, "Remove geofences");
            final Object syncObject = new Object();
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        Log.d(LOG_TAG, "[removeGeofences:onResult] : success - " + status);
                    } else {
                        // Get the status code for the error and log it using a user-friendly message.
                        String errorMessage = getErrorString(context, status.getStatusCode());
                        Log.e(LOG_TAG, "[removeGeofences:onResult] : fail - " + errorMessage);
                    }
                    synchronized (syncObject) {
                        syncObject.notifyAll();
                    }
                }
            }); // Result processed in onResult().

            synchronized (syncObject) {
                syncObject.wait();
            }
        } catch (SecurityException securityException) {
            Log.e(LOG_TAG, "", securityException);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GEOFENCE_NOT_AVAILABLE";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "GEOFENCE_TOO_MANY_GEOFENCES";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            default:
                return "unknown_geofence_error";
        }
    }
}
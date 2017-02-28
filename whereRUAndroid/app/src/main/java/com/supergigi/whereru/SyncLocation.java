package com.supergigi.whereru;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by tedwei on 28/02/2017.
 */

public class SyncLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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
        stop();
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
        return mCurrentLocation;
    }

}
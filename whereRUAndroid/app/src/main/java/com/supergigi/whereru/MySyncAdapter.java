package com.supergigi.whereru;

/**
 * Created by tedwei on 28/02/2017.
 */

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.supergigi.whereru.firebase.FbLocation;
import com.supergigi.whereru.firebase.FirebaseUtil;

import java.io.IOException;
import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class MySyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = MySyncAdapter.class.getSimpleName();

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public MySyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.d(LOG_TAG, "constructor called 1");
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync() - " + Thread.currentThread().getName());

        SyncLocation syncLocation = new SyncLocation(getContext());
        Location location = syncLocation.getLocationBlocking();
        Log.i(LOG_TAG, "retrieved location - " + location);
        FbLocation fbLocation = new FbLocation();
        fbLocation.setLatitude(location.getLatitude());
        fbLocation.setLongitude(location.getLongitude());
        fbLocation.setAccuracy(location.getAccuracy());

        Geocoder geocoder = new Geocoder(getContext());
        fbLocation.setAddress("Unknown");
        try {
            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                String addressLine = address.getAddressLine(0);
                Log.d(LOG_TAG, "addressLine - " + addressLine);
                fbLocation.setAddress(addressLine);
            }
        } catch (Exception e) {
            fbLocation.setAddress("" + e.getMessage());
            Log.e(LOG_TAG, "", e);
        }

        FirebaseUtil.getDeviceLocationLog().push().setValue(fbLocation);
        FirebaseUtil.getDeviceLastLocation().setValue(fbLocation);


    }

}
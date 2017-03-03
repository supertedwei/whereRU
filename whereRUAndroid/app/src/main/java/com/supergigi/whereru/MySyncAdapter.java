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
import com.supergigi.whereru.geocoder.GeocoderException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

        try {
            FirebaseUtil.updateRequestingLocation(true);

            internalOnPerformSync(account, extras, authority, provider, syncResult);
        } finally {
            FirebaseUtil.updateRequestingLocation(false);
        }
    }

    private void internalOnPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {

        SyncLocation syncLocation = new SyncLocation(getContext());
        Location location = syncLocation.getLocationBlocking();
        Log.i(LOG_TAG, "retrieved location - " + location);
        FbLocation fbLocation = new FbLocation();
        fbLocation.setLatitude(location.getLatitude());
        fbLocation.setLongitude(location.getLongitude());
        fbLocation.setAccuracy(location.getAccuracy());

        fbLocation.setAddress("Unknown");
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                String addressLine = address.getAddressLine(0);
                Log.d(LOG_TAG, "addressLine - " + addressLine);
                fbLocation.setAddress(addressLine);
            }
        } catch (IOException ioex) {
            Log.e(LOG_TAG, "", ioex);
            com.supergigi.whereru.geocoder.Geocoder geocoder2 = new com.supergigi.whereru.geocoder.Geocoder(getContext(), Locale.getDefault());
            try {
                List<com.supergigi.whereru.geocoder.Address> list = geocoder2.getFromLocation(location.getLatitude(), location.getLongitude(), 1, true);
                if (list.size() > 0) {
                    com.supergigi.whereru.geocoder.Address address = list.get(0);
                    String formattedAddress = address.getFormattedAddress();
                    Log.d(LOG_TAG, "formattedAddress - " + formattedAddress);
                    fbLocation.setAddress(formattedAddress);
                }
            } catch (GeocoderException e) {
                fbLocation.setAddress("" + e.getMessage());
                Log.e(LOG_TAG, "", e);
            }
        } catch (Exception e) {
            fbLocation.setAddress("" + e.getMessage());
            Log.e(LOG_TAG, "", e);
        }

        FirebaseUtil.getDeviceLocationLog().push().setValue(fbLocation);
        FirebaseUtil.getDeviceLastLocation().setValue(fbLocation);
    }

}
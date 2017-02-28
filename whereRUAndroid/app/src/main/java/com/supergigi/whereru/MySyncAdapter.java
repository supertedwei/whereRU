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
import android.os.Bundle;
import android.util.Log;

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

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    @Deprecated
    public MySyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        Log.d(LOG_TAG, "constructor called 2");
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

    }
}
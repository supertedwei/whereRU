package com.supergigi.whereru;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.supergigi.whereru.util.SyncUtil;

/**
 * Created by tedwei on 3/6/17.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String LOG_TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    public GeofenceTransitionsIntentService() {
        super(LOG_TAG);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent() : " + intent);
        SyncUtil.requestSync();
    }
}

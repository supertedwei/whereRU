package com.supergigi.whereru;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

/**
 * Created by tedwei on 28/02/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

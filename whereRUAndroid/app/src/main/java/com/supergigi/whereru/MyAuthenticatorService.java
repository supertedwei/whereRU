package com.supergigi.whereru;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tedwei on 28/02/2017.
 */

public class MyAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private MyAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MyAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

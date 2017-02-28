package com.supergigi.whereru;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

@Deprecated
public class MyLocationIntentService extends IntentService {

    private static final String LOG_TAG = MyLocationIntentService.class.getSimpleName();
    private static final String ACTION_FINE_LOCATION = "com.supergigi.whereru.action.ACTION_FINE_LOCATION";

    private static final int NOTIFICATION_ID = 1001;

//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.supergigi.whereru.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.supergigi.whereru.extra.PARAM2";

    public MyLocationIntentService() {
        super("MyLocationIntentService");
    }

    public static void startActionFineLocation(Context context/*, String param1, String param2*/) {
        Intent intent = new Intent(context, MyLocationIntentService.class);
        intent.setAction(ACTION_FINE_LOCATION);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate()");
        startForeground();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FINE_LOCATION.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFineLocation();
            } else {
                handleActionFineLocation();
            }
        }
    }

    private void handleActionFineLocation() {
        Log.d(LOG_TAG, "handleActionFineLocation()");
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startForeground() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("WhereRU");
        builder.setTicker("WhereRU running");
//        builder.setContentText("content text");
        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pi);
        final Notification notification = builder.build();

        startForeground(NOTIFICATION_ID, notification);
    }

}

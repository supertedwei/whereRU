package com.supergigi.whereru;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.supergigi.whereru.firebase.FbDeviceProfile;
import com.supergigi.whereru.firebase.FbLocation;
import com.supergigi.whereru.firebase.FbNotificationRequest;
import com.supergigi.whereru.firebase.FirebaseUtil;
import com.supergigi.whereru.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by tedwei on 28/02/2017.
 */

public class MapsMarkerActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = MapsMarkerActivity.class.getSimpleName();
    private static final String EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID";
    private static final int MAX_HISTORY_COUNT = 30;

    private String deviceId;
    private FbDeviceProfile fbDeviceProfile;
    ArrayList<FbLocation> historyLocations = new ArrayList<FbLocation>();
    private boolean isFirstRefresh = true;
    private GoogleMap googleMap;
    private TextView addressView;
    private View spinnerView;

    public static final Intent createIntent(Context context) {
        return createIntent(context, FirebaseUtil.getUid());
    }

    public static final Intent createIntent(Context context, String deviceId) {
        Intent intent = new Intent(context, MapsMarkerActivity.class);
        intent.putExtra(EXTRA_DEVICE_ID, deviceId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps_marker);

        addressView = (TextView) findViewById(R.id.address);
        spinnerView = findViewById(R.id.spinner);

        Intent intent = getIntent();
        deviceId = intent.getStringExtra(EXTRA_DEVICE_ID);

        DatabaseReference databaseReference = FirebaseUtil.getDeviceProfile(deviceId);
        addValueEventListener(databaseReference, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fbDeviceProfile = dataSnapshot.getValue(FbDeviceProfile.class);
                if (isFirstRefresh) {
                    isFirstRefresh = false;
                    onFirstRefresh();
                } else {
                    onRefresh();
                    Toast.makeText(MapsMarkerActivity.this, "New Location Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_marker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_get_location) {
            FbNotificationRequest fbNotificationRequest = FbNotificationRequest.createLocationRequest(fbDeviceProfile.getFcmToken());
            FirebaseUtil.pushNotificationRequest(fbNotificationRequest);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onFirstRefresh() {
        Log.d(LOG_TAG, "onFirstRefresh()");
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onRefresh() {
        Log.d(LOG_TAG, "onRefresh()");

        getSupportActionBar().setSubtitle(fbDeviceProfile.getName());

        googleMap.clear();
        FbLocation lastLocation = fbDeviceProfile.getLastLocation();
        LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(location).title(lastLocation.getAccAddress()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18.0f));

        addressView.setText(getString(lastLocation));
        if (fbDeviceProfile.isRequestingLocation()) {
            spinnerView.setVisibility(View.VISIBLE);
        } else {
            spinnerView.setVisibility(View.INVISIBLE);
        }

        // display history location
        int i = 0;
        for (FbLocation hisFbLocation : historyLocations) {
            int alpha = (255 / MAX_HISTORY_COUNT * i);
            LatLng hisLocation = new LatLng(hisFbLocation.getLatitude(), hisFbLocation.getLongitude());
            Log.d(LOG_TAG, "alpha : " + alpha + "/" + getString(hisFbLocation));
            googleMap.addMarker(new MarkerOptions()
                    .position(hisLocation)
                    .title(TimeUtil.toString(hisFbLocation.getLongTimestamp()))
                    .icon(BitmapDescriptorFactory.fromBitmap(createDot(alpha)))
            );
            i++;
        }
    }

    private String getString(FbLocation location) {
        StringBuffer buffer = new StringBuffer();
        if (location != null) {
            buffer.append("   (" + TimeUtil.toString(location.getLongTimestamp()) + ")");
            buffer.append("\n" + location.getAccAddress());
        }
        return buffer.toString();
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        onRefresh();

        DatabaseReference locationLogRef = FirebaseUtil.getDeviceLocationLog(deviceId);
        Query lastLogQuery = locationLogRef.limitToLast(MAX_HISTORY_COUNT);
        addValueEventListener(lastLogQuery, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<FbLocation> locations = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    locations.add(child.getValue(FbLocation.class));
                }
                historyLocations = locations;
                onRefresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Bitmap createDot(int alpha) {
        int width = 20;
        int height = width;

        Bitmap dotBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dotBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha(alpha);
        canvas.drawCircle(width/2, height/2, width/2-1, paint);

        return dotBitmap;
    }

}

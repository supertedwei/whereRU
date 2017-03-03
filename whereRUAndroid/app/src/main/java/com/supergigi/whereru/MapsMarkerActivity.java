package com.supergigi.whereru;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.supergigi.whereru.firebase.FbDeviceProfile;
import com.supergigi.whereru.firebase.FbLocation;
import com.supergigi.whereru.firebase.FbNotificationRequest;
import com.supergigi.whereru.firebase.FirebaseUtil;
import com.supergigi.whereru.util.TimeUtil;

/**
 * Created by tedwei on 28/02/2017.
 */

public class MapsMarkerActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = MapsMarkerActivity.class.getSimpleName();
    private static final String EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID";

    private String deviceId;
    private FbDeviceProfile fbDeviceProfile;
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
        googleMap.addMarker(new MarkerOptions().position(location).title(lastLocation.getAddress()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18.0f));

        StringBuffer buffer = new StringBuffer();
        if (lastLocation != null) {
            buffer.append("   (" + TimeUtil.toString(lastLocation.getLongTimestamp()) + ")");
            buffer.append("\n" + lastLocation.getAccAddress());
        }
        addressView.setText(buffer.toString());
        if (fbDeviceProfile.isRequestingLocation()) {
            spinnerView.setVisibility(View.VISIBLE);
        } else {
            spinnerView.setVisibility(View.INVISIBLE);
        }
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
    }

}

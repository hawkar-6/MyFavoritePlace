package com.example.choose;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.example.choose.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapActivity:
 * - Shows Google Map
 * - User long-presses to select a location
 * - Returns lat/lng to AddPlaceActivity
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private static final int REQ_LOCATION_PERMISSION = 4001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        enableMyLocationIfPermitted();

        // Default camera position (you can change)
        LatLng defaultLoc = new LatLng(37.422, -122.084);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 14f));

        // Long-press to choose location
        map.setOnMapLongClickListener(latLng -> {
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng).title("Selected location"));

            // Return coordinates
            getIntent().putExtra("lat", latLng.latitude);
            getIntent().putExtra("lng", latLng.longitude);
            setResult(RESULT_OK, getIntent());
            finish();
        });
    }

    private void enableMyLocationIfPermitted() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOCATION_PERMISSION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocationIfPermitted();
            }
        }
    }
}
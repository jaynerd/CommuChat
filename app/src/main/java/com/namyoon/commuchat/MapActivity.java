package com.namyoon.commuchat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtilLight;

public class MapActivity extends AppCompatActivity {
    TextView tv_out_mylocation;
    Button mapButton;

    Double lat;
    Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        tv_out_mylocation = (TextView) findViewById(R.id.tv_out_mylocation);
        Button btn_mylocation = (Button) findViewById(R.id.btn_mylocation);
        Button mapButton = (Button) findViewById(R.id.map_button);
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCurrentLocation();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, GMapActivity.class);
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", lon);
                startActivity(intent);
            }
        });

    }

    private void myCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener gpsListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                lat = latitude;
                lon = longitude;
                String msg = "Latitude: " + latitude + "\nLongitude: " + longitude;
                tv_out_mylocation.setText("My location: " + latitude + ", " + longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, gpsListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();
                tv_out_mylocation.setText("my location: " + latitude + ", " + longitude);
                Toast.makeText(getApplicationContext(), "last location: " + "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_LONG).show();
                lat = latitude;
                lon = longitude;
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Start locating", Toast.LENGTH_LONG).show();
    }
}

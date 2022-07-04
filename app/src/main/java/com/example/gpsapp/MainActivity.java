package com.example.gpsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private TextView tv_accuracy, tv_address, tv_altitude, tv_lat, tv_sensor, tv_lon, tv_speed, tv_updates = null;
    private Switch sw_gps, sw_locationsupdates = null;
    private Button btn_showWayPointLlist , btn_newWayPoint = null;


    Location currentLocation;
    List<Location> locations;
    // Location Request - Classe per configurare il GPS
    LocationRequest locationRequest = null;

    // Google API per location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallBack;
    private boolean ImTracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_address = findViewById(R.id.tv_address);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_lat = findViewById(R.id.tv_lat);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_lon = findViewById(R.id.tv_lon);
        tv_speed = findViewById(R.id.tv_speed);
        tv_updates = findViewById(R.id.tv_updates);
        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointLlist = findViewById(R.id.btn_showWayPointLlist);

        sw_gps = findViewById(R.id.sw_gps);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);

        locationRequest = LocationRequest.create()
                .setInterval(1000 * DEFAULT_UPDATE_INTERVAL)
                .setFastestInterval(1000 * FAST_UPDATE_INTERVAL)
                //.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                //.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMaxWaitTime(100);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                UpdateUIValue(location);
            }
        };


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) { // se lo switch è on
                    locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensor");

                } else {
                    locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers + WIFI");
                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_locationsupdates.isChecked()) { // se lo switch è on
                    startLocationTracking();
                } else {
                    stopLocationTracking();
                }
            }
        });

        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get gps location

                // add loc to global list
            }
        });

        UpdateGPS();
    }

    @SuppressLint("MissingPermission")
    private void startLocationTracking() {
        tv_updates.setText("You have been tracked");
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        UpdateGPS();
    }

    private void stopLocationTracking() {
        tv_updates.setText("You have NOT been tracked");
        tv_lat.setText("Not Tracking Location");
        tv_lon.setText("Not Tracking Location");
        tv_speed.setText("Not Tracking Location");
        tv_address.setText("Not Tracking Location");
        tv_accuracy.setText("Not Tracking Location");
        tv_altitude.setText("Not Tracking Location");
        tv_sensor.setText("Not Tracking Location");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }


    private void UpdateGPS(){
        // Permission from user to get GPS
        // avere la location dal client
        // update UI
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED ){
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // We got DATA
                    UpdateUIValue(location);
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },PERMISSIONS_FINE_LOCATION);
        }

    }

    // Legato a requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    UpdateGPS();
                }else {
                    Toast.makeText(this,"This App not have permission needed to work",Toast.LENGTH_LONG).show();
                    finish();  // exit program
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    private void UpdateUIValue(Location location){
        // UPDATE UI con data
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if(location.hasAltitude())
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        else
            tv_lat.setText(String.valueOf("Non Available"));

        if(location.hasSpeed())
            tv_speed.setText(String.valueOf(location.getSpeed()));
        else
            tv_speed.setText(String.valueOf("Non Available"));

        Geocoder geocoder = new Geocoder(MainActivity.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tv_address.setText(String.valueOf(addresses.get(0).getAddressLine(0)));
        } catch (IOException e) {
            tv_address.setText(String.valueOf("Non Available Address :( "));
            //e.printStackTrace();
        }
    }
}
package com.example.gpsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ShowDasveLocationsList extends AppCompatActivity {

    private ListView lv_savedlocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dasve_locations_list);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        List<Location> locations = myApplication.getMyLocations();

        lv_savedlocations = findViewById(R.id.lv_wayPoints);


        lv_savedlocations.setAdapter(new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1,locations));

    }
}
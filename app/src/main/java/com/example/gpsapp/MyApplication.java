package com.example.gpsapp;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication singleton;

    private List<Location> myLocation;

    public void setMyLocations(List<Location> myLocation) {
        this.myLocation = myLocation;
    }

    public List<Location> getMyLocations() {
        return myLocation;
    }

    public MyApplication getIstance(){
        return singleton;
    }

    public void OnCreate(){
        super.onCreate();
        singleton=this;
        myLocation = new ArrayList<>();
    }
}

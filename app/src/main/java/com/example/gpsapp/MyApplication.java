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

    public MyApplication getInstance(){//importante il nome del metodo sia quello si sovracrive
        return singleton;
    }

    public void onCreate(){ //importante il nome del metodo sia quello si sovracrive
        super.onCreate();
        singleton=this;
        myLocation = new ArrayList<>();
    }
}

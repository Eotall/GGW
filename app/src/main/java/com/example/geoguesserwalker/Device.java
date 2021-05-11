package com.example.geoguesserwalker;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class Device {
    protected GPSTracker gps;
    private Context context;

    public Device(Context context){
        this.gps = new GPSTracker(context);
        this.context = context;
        this.gps.addListener(this);
    }

    public String getCoordString(){
        String lat = Double.toString(this.gps.getLatitude());
        String lon = Double.toString(this.gps.getLongitude());

        return "Latitude: " + lat + " Longitude: " + lon;
    }


    public void resetGPS(){
        this.gps = new GPSTracker(context);
        this.gps.addListener(this);
    }
}

package com.example.geoguesserwalker;

import android.content.Context;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

public class Device {
    private GPSTracker gps;
    private Context context;
    private Compass compass;

    public Device(Context context){
        this.gps = new GPSTracker(context);
        this.context = context;
        this.gps.addListener(this);
        this.compass = new Compass(context);
    }

    /**
     * Method to get coordinates in a presentable manner
     * @return Coordinates as strings with added text
     */
    public String getCoordString(){
        String lat = Double.toString(this.gps.getLatitude());
        String lon = Double.toString(this.gps.getLongitude());

        return "Latitude: " + lat + "\nLongitude: " + lon;
    }

    public Location getLocation(){
        return this.gps.getLocation();
    }

    public void resetGPS(){
        this.gps = new GPSTracker(context);
        this.gps.addListener(this);
    }

    public float getBearingToLocation(Location location){
        return gps.location.bearingTo(location);
    }

    public void startCompass(){
        compass.start();
    }

    public void setCompassListener(Compass.CompassListener compassListener){
        compass.setListener(compassListener);
    }

    public float getBearingTo(Location location){
        if (gps.getLastLocation() == null) {
            return gps.getLocation().bearingTo(location);
        }else{
            return gps.getLastLocation().bearingTo(location);
        }
    }

    public void stopCompass(){
        compass.stop();
    }

}

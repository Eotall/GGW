package com.example.geoguesserwalker;

import android.content.Context;
import android.location.Location;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;

public class Device {
    private GPSTracker gps;
    private Context context;
    private Compass compass;
    private Vibrator vib;
    private VibratorHint hintVib;
    private Objective obj;

    public Device(Context context, Objective obj){
        this.gps = new GPSTracker(context);
        this.context = context;
        this.obj = obj;
        this.gps.addListener(this);
        this.compass = new Compass(context);
        this.vib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        this.hintVib = new VibratorHint(this);
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

    /**
     * Passes on the vibrator attribute
     * @return vib
     */
    public Vibrator getVibrator() {
        return vib;
    }

    /**
     * Called whenever the gps updates its location
     */
    public void gpsUpdated(Location loc) {
        hintVib.vibrateBasedOnDistance(obj, loc);
    }
}

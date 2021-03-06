package com.example.geoguesserwalker;

import android.location.Location;

public class Objective {
    private int image;
    private Location location;
    private Device device = null;
    private String name;
    private String panorama_file;
    /**
     * @param loc Location of the objective
     * @param img Reference to drawable
     */
    public Objective(Location loc, int img){
        this.image = img;
        this.location = loc;
    }

    /**
     *
     * @param lat Objective's latitude
     * @param lon Objective's longitude
     * @param img Reference to drawable
     */
    public Objective(double lat, double lon, int img, String name, String panorama_file){
        this.location = new Location("");
        this.location.setLatitude(lat);
        this.location.setLongitude(lon);
        this.image = img;
        this.name = name;
        this.panorama_file = panorama_file;
    }

    /**
     *
     * @param loc Location to be measured to (Device location)
     * @return Distance to given location
     */
    public float distanceTo(Location loc){
        return this.location.distanceTo(loc);
    }


    /**
     *
     * @return Image associated with location
     */
    public int getImage() {
        return this.image;
    }

    public double getLatitude(){
        return location.getLatitude();
    }
    public double getLongitude(){
        return location.getLongitude();
    }

    /**
     *
     * @param loc Location to be measured to (Device location)
     * @return Bearing to given location
     */
    public float bearingTo(Location loc){
        return this.location.bearingTo(loc);
    }

    public String getName(){return name;}

    public String getPanorama(){return panorama_file ;}

    public Location getLocation(){
        return this.location;
    }
}

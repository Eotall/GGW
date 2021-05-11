package com.example.geoguesserwalker;

import android.content.Context;

/**
 * Class that makes decisions for the game, such as given points and if a guess is close enough
 */
public class DecisionMaker {
    private final static int DISTANCE_MARGIN = 100;
    private final static float DISTANCE_POINT_FACTOR = 0.5f;
    private final static float MAX_SCORE = 1000f;
    private Device dev;

    public DecisionMaker(Context context){
         dev = new Device(context);
    }
    /**
     *
     * @param obj The current objective
     * @param dev The device connected to the application
     * @return true if closer than the set guess margin
     */
    public boolean isCloseEnough(Objective obj){
        return obj.distanceTo(dev.gps.location) <= DISTANCE_MARGIN;
    }

    /**
     *
     * @param obj The current objective
     * @param dev The device connected to the application
     * @return points given for the guess
     */
    public int pointsForGuess(Objective obj){
        int points = Math.round(MAX_SCORE - obj.distanceTo(dev.gps.location) * DISTANCE_POINT_FACTOR);
        return Math.max(points, 0);
    }

}

package com.example.geoguesserwalker;

/**
 * Class that makes decisions for the game, such as given points and if a guess is close enough
 */
public final class DecisionMaker {
    private final static int DISTANCE_MARGIN = 100;
    private final static float DISTANCE_POINT_FACTOR = 0.5f;
    private final static float MAX_SCORE = 1000f;

    private DecisionMaker(){}
    /**
     *
     * @param obj The current objective
     * @param dev The device connected to the application
     * @return true if closer than the set guess margin
     */
    public static boolean isCloseEnough(Objective obj, Device dev){
        return obj.distanceTo(dev.getLocation()) <= DISTANCE_MARGIN;
    }

    /**
     *
     * @param obj The current objective
     * @param dev The device connected to the application
     * @return points given for the guess
     */
    public static int pointsForGuess(Objective obj, Device dev){
        int points = Math.round(MAX_SCORE - obj.distanceTo(dev.getLocation()) * DISTANCE_POINT_FACTOR);
        return Math.max(points, 0);
    }

}

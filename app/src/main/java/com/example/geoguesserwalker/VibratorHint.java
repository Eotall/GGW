package com.example.geoguesserwalker;

import android.location.Location;
import android.os.Vibrator;

public class VibratorHint {
    private boolean active;
    private Device dev;
    private Vibrator vib;
    private final float DIST_NEAR = 20f;
    private final float DIST_MID = 60f;
    private final float DIST_FAR = 100f;
    private long lastVibrationTime;
    private final int TIME_BETWEEN_VIBRATIONS = 10000;

    /**
     * Creates an inactive vbrator component
     */
    public VibratorHint(Device dev) {
        active = true;
        this.dev = dev;
        this.vib = dev.getVibrator();
        lastVibrationTime = 0;
    }

    /**
     * Activates vibrations
     */
    public void activate() {
        active = true;
    }

    /**
     * Deactivates vibrations
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Generates vibrations based on distance from objective
     */
    public void vibrateBasedOnDistance(Objective obj, Location loc) {
        // only vibrate if active
        if (vibrationAllowed()) {
            // calculate distance to objective
            double distance = obj.distanceTo(loc);

            // create vibration pattern based on distance
            long[] pattern = {};
            if (distance < DIST_NEAR) {
                System.out.println("near");
                pattern = new long[] {0, 100, 500, 100, 500, 100, 500};
            } else if (distance < DIST_MID) {
                System.out.println("mid");
                pattern = new long[] {0, 100, 500, 100, 500};
            } else if (distance < DIST_FAR) {
                System.out.println("far");
                pattern = new long[] {0, 100, 500};
            }

            // vibrate according to pattern
            if (pattern.length != 0) {
                vib.vibrate(pattern, -1);
                lastVibrationTime = System.currentTimeMillis();
            }
        }
    }

    private boolean vibrationAllowed() {
        return (active && (System.currentTimeMillis() - lastVibrationTime > TIME_BETWEEN_VIBRATIONS));
    }
}

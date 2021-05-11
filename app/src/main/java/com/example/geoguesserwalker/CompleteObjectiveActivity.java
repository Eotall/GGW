package com.example.geoguesserwalker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;


//Starts when you are at the location and tasks you with putting down a pin in the ground, shake the phone up and down to acomplish this.
public class CompleteObjectiveActivity extends AppCompatActivity implements SensorEventListener {
    //CONSTANTS
    private final float ACCELERATION_ERROR_SENSITIVITY = 0.01f;
    private final int TIME_BETWEEN_SHAKES = 500; // Time in ms

    private VideoView vid;
    View view;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int shakeCount;
    ImageView img;
    PopupWindow popUp;
    boolean click;
    long timeSinceShake;
    long timeLastUpdate = 0;
    private float previousAcceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compleate_objective);
        vid = findViewById(R.id.videoView);
        playVideo();

        view = this.getWindow().getDecorView();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        previousAcceleration = 1000;
        shakeCount = 0;
        img = (ImageView) findViewById(R.id.imageView);
        click = true;


    }

    /**
     * Starts a sound based on the requested type
     * @param soundType The type of sound that should be played
     */
    public void playSound(Sound soundType){
        switch (soundType){
            case SOUND_HIT:
                MediaPlayer.create(this, R.raw.submit_hit).start();
                break;
            case SOUND_REWARD:
                MediaPlayer.create(this, R.raw.submit_reward).start();
                break;
            default:
                Log.e("Undefined Sound", "Tried to play a sound which is not defined in activity");
        }
    }


    //Plays the video of the shaking phone
    private void playVideo(){
        String path = "android.resource://com.example.geoguesserwalker/"+R.raw.complete_ani_phone;

        Uri u = Uri.parse(path);
        vid.setVideoURI(u);
        vid.setOnPreparedListener(mp -> mp.setLooping(true));
        vid.start();
    }

    //creates a popup that is displayed when the pin is down
    private void popUp(){
        popUp = new PopupWindow(this);
        LinearLayout layout = new LinearLayout(this);
        TextView tv = new TextView(this);
        Button but = new Button(this);
        but.setText("Awesome");
        but.setOnClickListener(v -> {
            if (click) {
                finish();
                click = false;
            } else {
                popUp.dismiss();
                click = true;
            }
        });

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        tv.setText("Congratulations you have received 2000 points!");
        tv.setBackgroundColor(Color.TRANSPARENT);
        layout.setBackgroundColor(Color.WHITE);
        tv.setTextSize(18);
        tv.setTextColor(Color.BLACK);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.maven_pro_medium);
        tv.setTypeface(typeface);
        but.setTypeface(typeface);
        tv.setPadding(20, 20, 20, 20);

        layout.addView(tv, params);
        layout.addView(but, params);
        popUp.setContentView(layout);
        popUp.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }

    //for the accelerometer
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    //for the accelerometer
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //for the accelerometer, detect shakes with a delay
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == accelerometer){
            handleAccelerometerEvent(event.values);
        }
    }

    /**
     * Handler for the values from a Linear Acceleration event
     * @param values The accelerations in the XYZ axes
     */
    private void handleAccelerometerEvent(float[] values){
        if (isWaitOver() && isJerkPositive(values[1]) && previousAccelerationWasNegative()){
            shake();
        }
        previousAcceleration = values[1];

    }

    /**
     * Checks if enough time has passed since the last shake was activated
     * @return True if sufficient time has passed since last shake
     */
    private boolean isWaitOver(){
        if(timeSinceShake == 0){
            timeSinceShake = System.currentTimeMillis();
        }
        return System.currentTimeMillis() - timeSinceShake > TIME_BETWEEN_SHAKES;
    }

    /**
     * Calculates the jerk of the latest acceleration and determines if it was positive
     * (acceleration change over time)
     * @param currentAcceleration The acceleration for the current update
     * @return The jerk of the current update
     */
    private boolean isJerkPositive(float currentAcceleration) {
        // Correction to remove sensor error
        if(Math.abs(currentAcceleration) < ACCELERATION_ERROR_SENSITIVITY) currentAcceleration = 0;
        if(Math.abs(previousAcceleration) < ACCELERATION_ERROR_SENSITIVITY) previousAcceleration = 0;

        float jerk = ((currentAcceleration - previousAcceleration) / (System.currentTimeMillis() - timeLastUpdate));

        if (jerk >0.001f) System.out.println("Jerk: " + Float.toString(jerk) + " Previous Acceleration: " + previousAcceleration);

        timeLastUpdate = System.currentTimeMillis();
        return jerk > 0.001f;
    }


    /**
     * Checks the acceleration of the previous update
     * @return True if the previous acceleration was negative
     */
    private boolean previousAccelerationWasNegative() {
        return previousAcceleration < 0;
    }

    /**
     * Plays a sound and changes the view based on the amount of successful shakes
     */
    private void shake() {
        timeSinceShake = System.currentTimeMillis();
        shakeCount++;
        if (shakeCount < 4) playSound(Sound.SOUND_HIT);
        if (shakeCount == 2) img.setImageResource(R.drawable.complete_pin_2);
        else if (shakeCount == 4) doVictoryScreen();
    }

    /**
     * Shows the user a popup when the submit-process is complete
     */
    private void doVictoryScreen() {
        playSound(Sound.SOUND_REWARD);
        img.setImageResource(R.drawable.complete_pin_3);
        popUp();
    }


    //for the accelerometer
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Enum that contains the names of the different types of sound for the activity
     */
    private enum Sound{
        SOUND_HIT,
        SOUND_REWARD
    }

}
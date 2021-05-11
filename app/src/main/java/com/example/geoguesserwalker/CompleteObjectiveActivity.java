package com.example.geoguesserwalker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
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
    private VideoView vid;
    View view;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastY;
    private int shake_count;
    ImageView img;
    long lastTime;
    private int accept_speed;
    PopupWindow popUp;
    boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compleate_objective);

        vid = findViewById(R.id.videoView);
        playVideo();

        view = this.getWindow().getDecorView();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        lastY = 1000;
        shake_count = 0;
        img = (ImageView) findViewById(R.id.imageView);
        lastTime = 0;
        accept_speed = 800;      //set to 80 for emulator, for real phone set to 800
        click = true;
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
            long time = System.currentTimeMillis();
            if ((time - lastTime) > 100) {
                long deltaTime = (time - lastTime);
                lastTime = time;
                float y = event.values[1];
                float speed = Math.abs(y - lastY) / deltaTime * 10000;
                if (speed > accept_speed & lastY < 0){
                    shake_count += 1;
                    if (shake_count == 2) {
                        img.setImageResource(R.drawable.complete_pin_2);
                    }else if(shake_count == 4){
                        img.setImageResource(R.drawable.complete_pin_3);
                        //vid.setVisibility(View.INVISIBLE);
                        popUp();
                    }
                }
                lastY = y;
            }
        }
    }

    //for the accelerometer
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
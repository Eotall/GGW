package com.example.geoguesserwalker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
import java.util.Objects;

class ViewPagerAdapter extends PagerAdapter{

    // Context object
    Context context;
    Location currentLocation; 
    List<Objective> objectives;
    GPSTracker gps;

    // Layout Inflater
    LayoutInflater mLayoutInflater;
    DecisionMaker decisionMaker;
    Device dev;

    // Vibrator
    Vibrator vib;
    boolean toggler = false;

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, List<Objective> objectives) {
        this.context = context;
        this.objectives = objectives;
        this.dev = new Device(context);
        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return objectives.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.objective_item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        textView.setText(String.format( objectives.get(position).getName()));
        // setting the image in the imageView
        imageView.setImageResource(objectives.get(position).getImage());


        // Temporary button for location confirmation
        Button TEMPORARYconfirmLocation = (Button) itemView.findViewById(R.id.TEMPORARYconfirmLocation);


        TextView confirmationTextView = (TextView) itemView.findViewById(R.id.confirmationTextView) ;
        confirmationTextView.setVisibility(TextView.INVISIBLE);
       // confirmationTextView.setText("Du är antagligen inte framme lul, funktionalitet saknas");

        TEMPORARYconfirmLocation.setOnClickListener(v -> {
            String text = DecisionMaker.isCloseEnough(objectives.get(position),dev)
                    ? "YOU DID IT, YOU CRAZY BASTARD"
                    : "Längre bort än 100 meter";

            confirmationTextView.setText(text);
            confirmationTextView.invalidate();
            confirmationTextView.setVisibility(TextView.VISIBLE);

            //confirmationTextView.setText(gps.getLatitude() +  " " + gps.getLongitude());
        });
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);



        // Vibration hint functionality
        // Add button
        Button vibrationHintButton = (Button) itemView.findViewById(R.id.hintVibration);
        // Set listener
        vibrationHintButton.setOnClickListener(l -> {
            if (toggler) {
                vib.cancel();
            }

            toggler = !toggler;
            // Set variables //TODO inte hardcoda? lägga som attribut?
            double near = 20;
            double mid = 40;
            double far = 60;
            int vibTime = 100;
            int pauseTime = 400;
            int intervalTime = 10000;
            long[] pattern = {0};
            double prevDis = 0; //only for debugging
            // Vibrate according to distance
            //while (toggler) { //TODO inte while här, det låser sig. Gör något asynkront?
                // Get distance to target
                double dis = decisionMaker.distanceTo(objectives.get(position), dev);
                if (prevDis != dis) { //only for debugging
                    System.out.println(dis);
                }
                prevDis = dis;
                if (dis < 0) {
                    // constant vibration
                    pattern = new long[]{ 0, vibTime, pauseTime };
                } else if (dis < near) {
                    // triple vibration pulses
                    pattern = new long[]{ 0, vibTime, pauseTime, vibTime, pauseTime, vibTime, (intervalTime - 3*vibTime - 2*pauseTime) };
                } else if (dis < mid) {
                    // double vibration pulses
                    pattern = new long[]{ 0, vibTime, pauseTime, vibTime, (intervalTime - 2*vibTime - pauseTime) };
                } else if (dis < far) {
                    // single vibration pulse
                    pattern = new long[]{ 0, vibTime, (intervalTime - vibTime) };
                } else {
                    // no vibration
                    pattern = new long[]{0};
                }
                vib.vibrate(pattern, 0);
            //}
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((ConstraintLayout) object);
    }
}

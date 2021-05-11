package com.example.geoguesserwalker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, List<Objective> objectives) {
        this.context = context;
        this.objectives = objectives;
        decisionMaker = new DecisionMaker(context);


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
            if (decisionMaker.isCloseEnough(objectives.get(position))){
                confirmationTextView.setText("YOU DID IT, YOU CRAZY BASTARD");
            }else{
                confirmationTextView.setText("mjaaaa inte riktigt");
            }

            confirmationTextView.invalidate();
            confirmationTextView.setVisibility(TextView.VISIBLE);

            //confirmationTextView.setText(gps.getLatitude() +  " " + gps.getLongitude());
        });
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((ConstraintLayout) object);
    }
}

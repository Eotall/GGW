 package com.example.geoguesserwalker;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


 /**
 * A simple {@link Fragment} subclass.
 * Use the {@link ObjectiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObjectiveFragment extends Fragment{


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String IMG = "img";
    private static final String NAME = "name";
    private static final String PANORAMA = "panorama_file";


    private Objective objective;

    private float currentAzimuth;

    Device dev;
    View itemView;
    ImageView compass_image;
    Button compass_button;
    Context context;
    public ObjectiveFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ObjectiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ObjectiveFragment newInstance(Objective objective) {
        ObjectiveFragment fragment = new ObjectiveFragment();
        // CHANGE parameter to Objective objective ?
        Bundle args = new Bundle();
        args.putDouble(LATITUDE, objective.getLatitude());
        args.putDouble(LONGITUDE, objective.getLongitude());
        args.putInt(IMG, objective.getImage());
        args.putString(NAME, objective.getName());
        args.putString(PANORAMA, objective.getPanorama());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            double lat = getArguments().getDouble(LATITUDE);
            double lon = getArguments().getDouble(LONGITUDE);
            int img = getArguments().getInt(IMG);
            String name = getArguments().getString(NAME);
            String panorama_file = getArguments().getString(PANORAMA);
            this.objective = new Objective(lat, lon, img, name, panorama_file);
            this.dev = new Device(getActivity(), this.objective);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.objective_item, container, false);
        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.imageViewMain);
        imageButton.setOnClickListener( v -> {
            Intent intent = new Intent(context, PanoramaActivity.class);
            intent.putExtra("panorama_jpg", this.objective.getPanorama());
            context.startActivity(intent);
        });

        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        textView.setText(String.format( this.objective.getName()));
        // setting the image in the imageView
        imageButton.setBackgroundResource(this.objective.getImage());
        TextView currentloc = itemView.findViewById(R.id.currentloc);
        TextView objectiveloc = itemView.findViewById(R.id.objectiveloc);
        currentloc.setText("Currentlocation:\n" + dev.getCoordString());
        objectiveloc.setText("Objectivelocation:\n Latitude: " +  objective.getLatitude() + "\nLongitude: " + objective.getLongitude());

        // Temporary button for location confirmation
        Button TEMPORARYconfirmLocation = (Button) itemView.findViewById(R.id.TEMPORARYconfirmLocation);


        TextView confirmationTextView = (TextView) itemView.findViewById(R.id.confirmationTextView) ;
        confirmationTextView.setVisibility(TextView.INVISIBLE);

        TEMPORARYconfirmLocation.setOnClickListener(v -> {
            if (DecisionMaker.isCloseEnough(this.objective, dev)){
                confirmationTextView.setText("YOU DID IT, YOU CRAZY BASTARD");
            }else{
                confirmationTextView.setText("mjaaaa inte riktigt");
            }

            confirmationTextView.invalidate();
            confirmationTextView.setVisibility(TextView.VISIBLE);
        });
        dev.startCompass();
        dev.setCompassListener(getCompassListener());

        compass_image = (ImageView) itemView.findViewById(R.id.compass_image);
        compass_button = itemView.findViewById(R.id.compass_button);
        compass_button.setOnClickListener( v -> {
            compass_button.setVisibility(View.INVISIBLE);

        });
        return itemView;
    }
    @Override
    public void onResume() {
        super.onResume();
        compass_button.setVisibility(View.VISIBLE);
        //onAnimationStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private void adjustArrow(float azimuth) {
        azimuth = -1*(dev.getBearingTo(this.objective.getLocation()) - azimuth );
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        compass_image.startAnimation(an);
    }


    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
            // UI updates only in UI thread
            // https://stackoverflow.com/q/11140285/444966
                if (getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       adjustArrow(azimuth);
                    }
                });
            }
        };
    }
}
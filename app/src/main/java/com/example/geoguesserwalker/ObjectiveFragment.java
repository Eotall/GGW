 package com.example.geoguesserwalker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private static boolean hint_open = false;
    private boolean help_open = false;

    Device dev;
    View itemView;
    ImageView compass_image;

    // Buttons for help when locating the objective.
    FloatingActionButton hint_button;
    Button compass_button;
    Button panorama_button;
    Button distance_button;

    // Image for help popup
     FloatingActionButton help_button_objective;
     ImageButton help_image_objective;

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
            this.dev = new Device(getActivity());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_objective, container, false);
        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.objective_image);

        // setting the image in the imageView
        imageButton.setBackgroundResource(this.objective.getImage());

        // Temporary button for location confirmation
        Button drop_pin_button = (Button) itemView.findViewById(R.id.drop_pin_button);

        TextView confirmationTextView = (TextView) itemView.findViewById(R.id.confirmationTextView) ;
        confirmationTextView.setVisibility(TextView.INVISIBLE);

        drop_pin_button.setOnClickListener(v -> {
            if (DecisionMaker.isCloseEnough(this.objective, dev)){
                //Initiates the components CompleatObjeciveActivity, activity_compleate_objective.
                Intent intent = new Intent(this.context, CompleteObjectiveActivity.class);
                startActivity(intent);
            }
            else {
                confirmationTextView.setText("Keep walking, you'll get there!");
            }
            confirmationTextView.invalidate();
            confirmationTextView.setVisibility(TextView.VISIBLE);
        });
        dev.startCompass();
        dev.setCompassListener(getCompassListener());

        compass_image = (ImageView) itemView.findViewById(R.id.compass_image);
        compass_image.setVisibility(View.GONE);

        initiateHints();
        initiateHelp();

        return itemView;
    }


     private void initiateHints() {
         //Connecting buttons in objective_item to local buttons.
         hint_button = itemView.findViewById(R.id.hint_button);
         compass_button = itemView.findViewById(R.id.compass_button);
         panorama_button = itemView.findViewById(R.id.panorama_button);
         distance_button = itemView.findViewById(R.id.distance_button);

         //Make helping buttons invisable
         compass_button.setVisibility(View.INVISIBLE);
         panorama_button.setVisibility(View.INVISIBLE);
         distance_button.setVisibility(View.INVISIBLE);

         hint_button.setOnClickListener(v -> {
             if(hint_open) {
                 compass_button.setVisibility(View.INVISIBLE);
                 panorama_button.setVisibility(View.INVISIBLE);
                 //distance_button.setVisibility(View.INVISIBLE);
             }
             else {
                 compass_button.setVisibility(View.VISIBLE);
                 panorama_button.setVisibility(View.VISIBLE);
                 //distance_button.setVisibility(View.VISIBLE);
             }
             hint_open = !hint_open;
         });

         compass_button.setOnClickListener( v -> {
             compass_image.setVisibility(View.VISIBLE);
         });

         panorama_button.setOnClickListener( v -> {
             Intent intent = new Intent(context, PanoramaActivity.class);
             intent.putExtra("panorama_jpg", this.objective.getPanorama());
             context.startActivity(intent);
         });

         /**
         distance_button.setOnClickListener( v -> {
         });
         */
     }

     private void initiateHelp() {
         //Connecting buttons in objective_item to local buttons.
         help_image_objective = itemView.findViewById(R.id.help_image_objective);
         help_button_objective = itemView.findViewById(R.id.help_button_objective);

         //Setting onClick functions to click up and down help view.
         help_image_objective.setOnClickListener(v -> {
             help_image_objective.setVisibility(View.GONE);
             help_open = false;
         });

         help_button_objective.setOnClickListener(v -> {
             if(help_open) {
                 help_image_objective.setVisibility(View.GONE);
             }
             else {
                 help_image_objective.setVisibility(View.VISIBLE);
             }
             help_open = !help_open;


         });

     }

     @Override
    public void onResume() {
        super.onResume();
        compass_image.setVisibility(View.GONE);
        help_image_objective.setVisibility(View.GONE);
        //onAnimationStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    //Code for direction with compass
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
package com.example.geoguesserwalker;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ObjectiveActivity extends AppCompatActivity {


    private Device device = null;
    private Objective testObjective = null;
    ViewPager mViewPager;
    private ObjectiveTracker objectiveTracker = null;
    // Creating Object of ViewPagerAdapter

    MyFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objective);

        objectiveTracker = new ObjectiveTracker();

        // Initializing the ViewPager Object
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        // Initializing the ViewPagerAdapter

        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(fragmentPagerAdapter);

    }
    @Override
    protected void onStop(){
        super.onStop();
        // stop sensors

    }

    @Override
    protected void onPause(){
        super.onPause();
        // stop sensors

    }



}
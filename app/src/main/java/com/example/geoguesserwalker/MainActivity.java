package com.example.geoguesserwalker;

import android.Manifest;
import android.content.Intent;
import android.location.Location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity {
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private Device device = null;
    private Objective testObjective = null;
    private ObjectiveTracker objectiveTracker = null;

    Button showCoordsBtn;
    TextView coordDspTextView;
    TextView distToObjTextView;
    Button objectivesActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.device = new Device(this);

        requestLocationPermission();
        this.objectiveTracker = new ObjectiveTracker();

        this.testObjective = objectiveTracker.nextObjective();
    }

    @Override
    protected void onResume(){
        super.onResume();


        showCoordsBtn = (Button) findViewById(R.id.showCoordsBtn);
        coordDspTextView = (TextView) findViewById(R.id.coordDspTextView);
        distToObjTextView = (TextView) findViewById(R.id.distToObjTextView);
        objectivesActivityBtn = (Button) findViewById(R.id.objectivesActivityBtn);

        showCoordsBtn.setText("Press to get coordinates");
        coordDspTextView.setText("Coordinates");
        showCoordsBtn.setOnClickListener(v -> {
            coordDspTextView.setText(device.getCoordString());
        });

        objectivesActivityBtn.setOnClickListener( v -> {
            Intent intent = new Intent(this, ObjectiveActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //FIXME: Om permissions ändras måste appen startas om från grunden för att fixas
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        System.out.println("RequestLocation");
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            this.device.resetGPS();
        }
        /*
        else if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(perms))) {
            new AppSettingsDialog.Builder(this).build().show();
        }
         */
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);


            /*
            Om perm ändras
                this.device.resetGPS();
             */

        }
    }

}
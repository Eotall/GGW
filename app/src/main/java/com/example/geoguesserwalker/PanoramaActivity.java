package com.example.geoguesserwalker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

public class
PanoramaActivity extends AppCompatActivity {

    private VrPanoramaView mVrPanoramaView;
    private String panorama_jpg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        Intent intent = getIntent();
        panorama_jpg = intent.getExtras().getString("panorama_jpg");
        mVrPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);
        System.out.println(panorama_jpg);
        loadPhotoSphere();
    }

    // Spara panorama bild i assets
    private void loadPhotoSphere() {
        //This could take a while. Should do on a background thread, but fine for current example
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        InputStream inputStream = null;
        AssetManager assetManager = getAssets();
        try {
            inputStream = assetManager.open(panorama_jpg);
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(inputStream), options);
            inputStream.close();
        } catch (IOException e) {
            Log.e("Tuts+", "Exception in loadPhotoSphere: " + e.getMessage() );
        }
    }
}
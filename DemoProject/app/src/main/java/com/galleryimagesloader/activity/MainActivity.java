package com.galleryimagesloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.galleryimagesloader.app.R;
import com.galleryimagesloaderlibrary.activity.CustomGalleryActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addPhotosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addPhotosButton = (Button) findViewById(R.id.add_photos_button);
        addPhotosButton.setOnClickListener(this);
    }



    

    @Override
    public void onClick(View v) {
        if(v  == addPhotosButton){
            Intent intent = new Intent(MainActivity.this, CustomGalleryActivity.class);
            startActivity(intent);
        }
    }
}

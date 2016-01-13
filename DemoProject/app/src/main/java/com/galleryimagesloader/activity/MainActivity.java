package com.galleryimagesloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.galleryimagesloader.app.R;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addPhotosButton;
    private static final int REQUEST_SELECT_PHOTOS_FROM_GALLERY = 111;

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
            startActivityForResult(intent, REQUEST_SELECT_PHOTOS_FROM_GALLERY);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SELECT_PHOTOS_FROM_GALLERY && resultCode == RESULT_OK){
            String[] originalPaths = data.getExtras().getStringArray("all_path");
            Log.i("size", originalPaths.length+"");
            Toast.makeText(this, originalPaths.length+" " + "images selected", Toast.LENGTH_LONG).show();
        }else if(requestCode == REQUEST_SELECT_PHOTOS_FROM_GALLERY && resultCode == RESULT_CANCELED){
            Toast.makeText(this,"No images selected", Toast.LENGTH_LONG).show();
        }
    }
}

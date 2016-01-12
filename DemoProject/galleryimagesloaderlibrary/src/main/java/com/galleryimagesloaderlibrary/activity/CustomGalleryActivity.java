package com.galleryimagesloaderlibrary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;


import com.galleryimagesloader.library.R;
import com.galleryimagesloaderlibrary.fragments.BaseFragment;
import com.galleryimagesloaderlibrary.fragments.PhotoGridFragment;
import com.galleryimagesloaderlibrary.interfaces.DialogClickListener;
import com.galleryimagesloaderlibrary.model.PhotoItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CustomGalleryActivity extends AppCompatActivity implements DialogClickListener, BaseFragment.OnFragmentInteractionListener {

    protected static final int MAX_IMAGE_SELECTION_LENGTH = 10;
    GridView gridGallery;
    Handler handler;
    ImageView imgNoMedia;
    String action;
    private ImageLoader imageLoader;
    PhotoGridFragment targetFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        targetFragment = PhotoGridFragment.newInstance(1);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, targetFragment)
                .commit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {

            List<PhotoItem> photos = targetFragment.selectedPhotoItems;
            if (photos.size() > 0) {
                String[] allPath = new String[photos.size()];
                int i = 0;
                for (PhotoItem photoItem : photos) {

                    allPath[i] = photoItem.getFullImageUri().toString();
                    i++;
                }

                Intent data = new Intent().putExtra("all_path", allPath);
                setResult(RESULT_OK, data);
                finish();
            } else {
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }

            return true;
        } else if (id == android.R.id.home) {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(int actionId) {

    }
}

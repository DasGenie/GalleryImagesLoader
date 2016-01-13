package com.galleryimagesloader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;

import com.galleryimagesloader.library.R;
import com.galleryimagesloaderlibrary.fragments.BaseFragment;
import com.galleryimagesloaderlibrary.fragments.PhotoGridFragment;
import com.galleryimagesloaderlibrary.interfaces.DialogClickListener;
import com.galleryimagesloaderlibrary.model.PhotoItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CustomGalleryActivity extends AppCompatActivity implements  BaseFragment.OnFragmentInteractionListener, PhotoGridFragment.ReturnSelectedImagesListener {


    PhotoGridFragment targetFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        targetFragment = PhotoGridFragment.newInstance(this);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, targetFragment)
                .commit();

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

    @Override
    public void onImagesSelected(ArrayList<PhotoItem> selectedPhotoItems) {
        List<PhotoItem> photos = selectedPhotoItems;
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
    }
}

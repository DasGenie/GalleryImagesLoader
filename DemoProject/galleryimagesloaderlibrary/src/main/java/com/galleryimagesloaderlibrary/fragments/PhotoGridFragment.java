/*
 * Copyright (c) 2014 Rex St. John on behalf of AirPair.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.galleryimagesloaderlibrary.fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.galleryimagesloader.library.R;
import com.galleryimagesloaderlibrary.adapter.PhotoAdapter;
import com.galleryimagesloaderlibrary.model.PhotoItem;
import com.galleryimagesloaderlibrary.utils.PhotoGalleryAsyncLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an example which will load all the images on your phone into a grid using a background
 * image AsyncLoader.
 * <p/>
 * Reference: http://developer.android.com/reference/android/content/AsyncTaskLoader.html
 * <p/>
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoGridFragment extends BaseFragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<PhotoItem>> {


    protected OnFragmentInteractionListener mListener;
    protected AbsListView mListView;
    protected PhotoAdapter mAdapter;
    public ArrayList<PhotoItem> mPhotoListItem;
    protected TextView mEmptyTextView;
    protected ProgressDialog mLoadingProgressDialog;
    public ArrayList<PhotoItem> selectedPhotoItems;
    private ReturnSelectedImagesListener returnSelectedImagesListener;


    /**
     * Required empty constructor
     */
    public PhotoGridFragment() {
        super();
    }

    /**
     * Static factory method
     *
     *
     * @return
     */
    public static PhotoGridFragment newInstance(ReturnSelectedImagesListener returnSelectedImagesListener) {
        PhotoGridFragment fragment = new PhotoGridFragment();
        fragment.setReturnSelectedImagesListener(returnSelectedImagesListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        // Create an empty loader and pre-initialize the photo list items as an empty list.
        Context context = getActivity().getBaseContext();

        // Set up empty mAdapter
        mPhotoListItem = new ArrayList<PhotoItem>();
        selectedPhotoItems = new ArrayList<>();
        mAdapter = new PhotoAdapter(context,
                R.layout.gallery_item,
                mPhotoListItem, false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        // Set the mAdapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mEmptyTextView = (TextView) view.findViewById(R.id.empty);

        // Show the empty text / message.
        resolveEmptyText();

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_custom_gallery, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {

            returnSelectedImagesListener.onImagesSelected(selectedPhotoItems);

            return true;
        } else if (id == android.R.id.home) {
            Intent data = new Intent();
            getActivity().setResult(getActivity().RESULT_CANCELED, data);
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Used to show a generic empty text warning. Override in inheriting classes.
     */
    protected void resolveEmptyText() {
        if (mAdapter.isEmpty()) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            setEmptyText();
        } else {
            mEmptyTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            // Show a progress dialog.
            mLoadingProgressDialog = new ProgressDialog(getActivity());
            mLoadingProgressDialog.setMessage("Photos Loading... This might few minutes depending on the number of photos in your gallery. Please wait.");
            mLoadingProgressDialog.setCancelable(false);

            mLoadingProgressDialog.show();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        cancelProgressDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelProgressDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelProgressDialog();
    }

    /**
     * This is only triggered when the user selects a single photo.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {

            PhotoItem photoListItem = (PhotoItem) this.mAdapter.getItem(position);

            Log.i("full imaGE" , photoListItem.getFullImageUri()+"");
                // Tell the share builder to add the photo to the share operation.
                if (photoListItem.isSelected())
                    photoListItem.setIsSelected(false);
                else
                    photoListItem.setIsSelected(true);

                if (selectedPhotoItems.contains(photoListItem)) {
                    selectedPhotoItems.remove(photoListItem);
                } else {
                    selectedPhotoItems.add(photoListItem);
                }

            resetFragmentState();
        }
    }

    /**
     * Used when hitting the back button to reset the mFragment UI state
     */
    protected void resetFragmentState() {
        // Clear view state
        getActivity().invalidateOptionsMenu();
        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText() {
        mEmptyTextView.setText("No Photos!");
    }

    /**
     * Loader Handlers for loading the photos in the background.
     */
    @Override
    public Loader<List<PhotoItem>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader with no arguments, so it is simple.
        return new PhotoGalleryAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<PhotoItem>> loader, List<PhotoItem> data) {

        //AppController.getInstance().setLoggerInfo("Data Loaded from Gallery and SIZE =>"+data.size());
        // Set the new data in the mAdapter.
        mPhotoListItem.clear();
        mPhotoListItem.addAll(data);
//
//        for (int i = 0; i < data.size(); i++) {
//            PhotoItem item = data.get(i);
//            mPhotoListItem.add(item);
//        }

        mAdapter.notifyDataSetChanged();
        resolveEmptyText();
        cancelProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<List<PhotoItem>> loader) {
        // Clear the data in the mAdapter.
        mPhotoListItem.clear();
        mAdapter.notifyDataSetChanged();
        resolveEmptyText();
        cancelProgressDialog();
    }

    /**
     * Save cancel for the progress loader
     */
    private void cancelProgressDialog() {
        if (mLoadingProgressDialog != null) {
            if (mLoadingProgressDialog.isShowing()) {
                mLoadingProgressDialog.cancel();
            }
        }
    }


    /**
     * call back to send list of selected photos to activity
     */

    public interface ReturnSelectedImagesListener{
        void onImagesSelected(ArrayList<PhotoItem> selectedPhotoItems);
    }

    public void setReturnSelectedImagesListener(ReturnSelectedImagesListener returnSelectedImagesListener) {
        this.returnSelectedImagesListener = returnSelectedImagesListener;
    }
}
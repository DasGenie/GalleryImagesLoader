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

package com.galleryimagesloaderlibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.galleryimagesloader.library.R;
import com.galleryimagesloaderlibrary.model.PhotoItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Photo Array Adapter to power a simple Android photo gallery.
 * <p/>
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoAdapter extends ArrayAdapter<PhotoItem> {

    // Ivars.
    private Context context;
    private int resourceId;

    com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    public PhotoAdapter(Context context, int resourceId,
                        List<PhotoItem> items, boolean useList) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;

    }

    /**
     * The "ViewHolder" pattern is used for speed.
     * <p/>
     * Reference: http://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html
     */
    private class ViewHolder {
        ImageView photoImageView;
        ImageView imgQueueMultiSelected;
    }

    /**
     * Populate the view holder with data.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PhotoItem photoItem = getItem(position);
        View viewToUse = null;

        // This block exists to inflate the photo list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            viewToUse = mInflater.inflate(resourceId, null);
            holder.photoImageView = (ImageView) viewToUse.findViewById(R.id.imgQueue);
            holder.imgQueueMultiSelected = (ImageView) viewToUse.findViewById(R.id.imgQueueMultiSelected);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }


        Picasso.with(getContext()).load(new File(photoItem.getThumbnailUri())).placeholder(R.drawable.no_media).error(R.drawable.no_media).into(holder.photoImageView);
        holder.imgQueueMultiSelected.setSelected(photoItem.isSelected());
        return viewToUse;
    }
}

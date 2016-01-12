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

package com.galleryimagesloaderlibrary.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.galleryimagesloaderlibrary.model.PhotoItem;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper utility which automatically fetches paths to full size and thumbnail sized gallery images.
 * <p/>
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoGalleryImageProvider {

    // Consts
    public static final int IMAGE_RESOLUTION = 15;

    // Buckets where we are fetching images from.
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);


    /**
     * Fetch both full sized images and thumbnails via a single query.
     * Returns all images not in the Camera Roll.
     *
     * @param context
     * @return
     */
//    public static List<PhotoItem> getAlbumThumbnails(Context context) {
//
//
//        MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//            /*
//             *   (non-Javadoc)
//             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
//             */
//            public void onScanCompleted(String path, Uri uri) {
//                Log.i("ExternalStorage", "Scanned " + path + ":");
//                Log.i("ExternalStorage", "-> uri=" + uri);
//            }
//        });
//
//
//        final String[] projection = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
//
//
//        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
//        final String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
//
//        final String orderBy = MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER;
//
//        Cursor thumbnailsCursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
//                projection, // Which columns to return
//                null,       // Return all rows
//                null,
//                null);
//
//        // Extract the proper column thumbnails
//        int thumbnailColumnIndex = thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
//        ArrayList<PhotoItem> result = new ArrayList<PhotoItem>(thumbnailsCursor.getCount());
//
//        if (thumbnailsCursor.moveToFirst()) {
//            do {
//                // Generate a tiny thumbnail version.
//                int thumbnailImageID = thumbnailsCursor.getInt(thumbnailColumnIndex);
//                String thumbnailPath = thumbnailsCursor.getString(thumbnailImageID);
//                Uri thumbnailUri = Uri.parse(thumbnailPath);
//                Uri fullImageUri = uriToFullImage(thumbnailsCursor, context);
//
//                // Create the list item.
//                PhotoItem newItem = new PhotoItem(thumbnailUri, fullImageUri);
//                result.add(newItem);
//            } while (thumbnailsCursor.moveToNext());
//        }
//        thumbnailsCursor.close();
//        return result;
//    }
    public static List<PhotoItem> getImagesPath(Context ctx) {
        Log.v("called", "called");


        Log.v("start time", String.valueOf(System.currentTimeMillis()));


        ArrayList<PhotoItem> result = null;
        try {
            String[] projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.SIZE

            };
            // Get the base URI for the People table in the Contacts content provider.
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            // Make the query.
            Cursor cur = ctx.getContentResolver().query(images,
                    projection, // Which columns to return
                    null,       // Which rows to return (all rows)
                    null,       // Selection arguments (none)
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"        // Ordering
            );


            result = new ArrayList<PhotoItem>(cur.getCount());


           if (cur.moveToFirst()) {
                String bucket;
                String date;
                int bucketColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int dateColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATE_TAKEN);


                int index = cur.getColumnIndex(MediaStore.Images.Media.DATA);


                do {

                    try {
                        String path = cur.getString(index);
                        // Get the field values
                        bucket = cur.getString(bucketColumn);
                        date = cur.getString(dateColumn);
                        long size = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.SIZE));
                        // Uri uri = Uri.parse(path);
                      //  PhotoItem newItem = new PhotoItem(getThumbnailPath(ctx, path), uriToFullImage(cur, ctx), size);
                        String thumbnailPath = getThumbnailPath(ctx, path);
                        Uri fullPath = uriToFullImage(thumbnailPath, ctx);
                        PhotoItem newItem = new PhotoItem(thumbnailPath, fullPath, size);
                        result.add(newItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (cur.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getThumbnailPath(Context context, String path) {
        String result = null;

            long imageId = -1;

            String[] projection = new String[]{MediaStore.MediaColumns._ID};
            String selection = MediaStore.MediaColumns.DATA + "=?";
            String[] selectionArgs = new String[]{path};
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
            }

            result = "";

            cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                cursor.close();
            } else {
                MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
                Cursor imageCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
                if (imageCursor != null && imageCursor.getCount() > 0) {
                    imageCursor.moveToFirst();
                    result = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                    imageCursor.close();
                }
            }


        return result;
    }


    /**
     * Get the path to the full image for a given thumbnail.
     */
    private static Uri uriToFullImage(String thumbnailPath , Context context) {


        String[] columns_to_return ={MediaStore.Images.Thumbnails.IMAGE_ID};
        String where = MediaStore.Images.Thumbnails.DATA+" LIKE ?";
        String imageId="";
        String valuesAre[]={"%"+thumbnailPath};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns_to_return, where, valuesAre, null);
        if(cursor!=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                //STEP 1 to retrieve image ID
                imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));

            }
            cursor.close();
        }
         //   String imageId = thumbnailsCursor.getString(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));

            // Request image related to this thumbnail
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor imagesCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[]{imageId}, null);

            if (imagesCursor != null && imagesCursor.moveToFirst()) {
                int columnIndex = imagesCursor.getColumnIndex(MediaStore.Images.Media.DATA);  //filePathColumn[0]
                String filePath = imagesCursor.getString(columnIndex);
                Log.i("full path" , filePath);
                imagesCursor.close();
                return Uri.parse(filePath);
            } else {
                imagesCursor.close();
                return Uri.parse("");
            }

    }

    /**
     * Render a thumbnail photo and scale it down to a smaller size.
     *
     * @param path
     * @return
     */
    private static Bitmap bitmapFromPath(String path) {
        File imgFile = new File(path);
        Bitmap imageBitmap = null;

        if (imgFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = IMAGE_RESOLUTION;
            imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        }
        return imageBitmap;
    }

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }


    public static String printStackTracceString(Exception e){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        String s = writer.toString();
        return s;
    }
}

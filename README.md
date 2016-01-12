# Gallery Images Loader

The gallery images loader component loads the images from gallery with multi selection options. The thumbnails of the images are displayed
on the grid. The selected images also has the Uri of the original image. Thus the developer need not write query to fetch the original image.


#How to use?

You can add this module as a dependency to your application.

        compile project(':galleryimagesloaderlibrary')

Through Intent call CustomGalleryActivity class from your Activity/Fragment class.

        Intent intent = new Intent(MainActivity.this, CustomGalleryActivity.class);
        startActivity(intent);

#Preview
-------
![Screenshot](https://raw.githubusercontent.com/DasGenie/GalleryImagesLoader/5b461625768377fa5dc31c4a09a16819b8737b60/Screenshots/screenshot.png)


 

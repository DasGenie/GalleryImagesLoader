# Gallery Images Loader

The gallery images loader component loads the images from gallery with multi selection options. The thumbnails of the images are displayed
on the grid. The selected images also has the Uri of the original image. Thus the developer need not write query to fetch the original image.

#Features

<ul>
  <li>Multiselection of images</li>
  <li>Returns the thumbnail and actual image path</li>
  <li>Avoids writing query to fetch the original image path</li>
</ul>


#Installation

You can add this module as a dependency to your application.

        compile project(':galleryimagesloaderlibrary')

Call Fragment using the below code

       PhotoGridFragment targetFragment = PhotoGridFragment.newInstance(this);
       getFragmentManager().beginTransaction()
       .replace(R.id.fragment_container, targetFragment)
       .commit(); 
       
Implement the call back 

       @Override
       public void onImagesSelected(ArrayList<PhotoItem> selectedPhotoItems) {
               // write your code here
        }
        
        
  ![Screenshot](https://github.com/DasGenie/GalleryImagesLoader/blob/master/Screenshots/1h4q_T.gif)      





 

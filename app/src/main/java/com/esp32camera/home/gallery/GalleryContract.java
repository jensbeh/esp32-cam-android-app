package com.esp32camera.home.gallery;

import java.util.List;

public interface GalleryContract {

    interface View { // from presenter to view

    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(GalleryFragment galleryFragment);

        List<String> loadGalleryItems();

        List<String> getGalleryItems();

        void setSelectedItem(int position);

        int getSelectedItem();
    }
}
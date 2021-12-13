package com.esp32camera.home.gallery;

import java.util.List;

public interface GalleryContract {

    interface View {
        void showDeleteButton(); // from presenter to view

        void hideDeleteButton();

        void notifyOnItemsDelete();
    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(GalleryFragment galleryFragment);

        List<String> loadGalleryItems();

        List<String> getGalleryItems();

        void setViewPagerSelectedItem(int selectedViewPagerItemPosition);

        int getViewPagerSelectedItem();

        void showDeleteButton();

        void hideDeleteButton();

        void deleteSelectedItems();

        void setSelectedItem(String item);

        List<String> getSelectedItems();

        void removeSelectedItem(String item);

        void clearSelectedItems();
    }
}
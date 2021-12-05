package com.esp32camera.home.gallery.viewPager;

public interface GalleryViewPagerContract {

    interface View { // from presenter to view

    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(GalleryViewPagerFragment galleryViewPagerFragment);
    }
}
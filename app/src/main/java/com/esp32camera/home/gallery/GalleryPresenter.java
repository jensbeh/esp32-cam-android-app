package com.esp32camera.home.gallery;

import com.esp32camera.MainActivity;

public class GalleryPresenter implements GalleryContract.Presenter {

    private final MainActivity mainActivity;
    private GalleryFragment galleryFragment;

    public GalleryPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void setView(GalleryFragment galleryFragment) {
        this.galleryFragment = galleryFragment;
    }
}
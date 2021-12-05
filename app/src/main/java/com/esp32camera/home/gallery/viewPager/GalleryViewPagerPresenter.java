package com.esp32camera.home.gallery.viewPager;

import com.esp32camera.MainActivity;

public class GalleryViewPagerPresenter implements GalleryViewPagerContract.Presenter {

    private final MainActivity mainActivity;
    private GalleryViewPagerFragment galleryViewPagerFragment;

    public GalleryViewPagerPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void setView(GalleryViewPagerFragment galleryViewPagerFragment) {
        this.galleryViewPagerFragment = galleryViewPagerFragment;
    }
}
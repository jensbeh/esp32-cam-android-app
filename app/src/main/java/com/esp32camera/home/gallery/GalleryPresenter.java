package com.esp32camera.home.gallery;

import android.os.Environment;

import com.esp32camera.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryPresenter implements GalleryContract.Presenter {

    private final MainActivity mainActivity;
    private GalleryFragment galleryFragment;
    private List<String> galleryItems;
    private int selectedItemPosition;

    public GalleryPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void setView(GalleryFragment galleryFragment) {
        this.galleryFragment = galleryFragment;
    }

    @Override
    public List<String> loadGalleryItems() {
        galleryItems = new ArrayList<>();

        File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File itemsDir = new File(dcimDir.getAbsolutePath() + "/EspCamera/");

        File[] items = itemsDir.listFiles();
        for (File item : items) {
            galleryItems.add(item.getAbsolutePath());
        }

        return galleryItems;
    }

    @Override
    public List<String> getGalleryItems() {
        return galleryItems;
    }

    @Override
    public void setSelectedItem(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    @Override
    public int getSelectedItem() {
        return selectedItemPosition;
    }
}
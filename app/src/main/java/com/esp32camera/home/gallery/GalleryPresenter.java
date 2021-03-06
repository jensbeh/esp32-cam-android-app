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
    private int selectedViewPagerItemPosition;
    private List<String> selectedItems;

    public GalleryPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        selectedItems = new ArrayList<>();
    }

    @Override
    public void setView(GalleryFragment galleryFragment) {
        this.galleryFragment = galleryFragment;
    }

    /**
     * method to load all galleryImagePaths from DCIM
     */
    @Override
    public List<String> loadGalleryItems() {
        galleryItems = new ArrayList<>();

        File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File itemsDir = new File(dcimDir.getAbsolutePath() + "/EspCamera/");

        File[] items = itemsDir.listFiles();
        if (items != null) {
            for (File item : items) {
                galleryItems.add(item.getAbsolutePath());
            }
        }

        return galleryItems;
    }

    @Override
    public List<String> getGalleryItems() {
        return galleryItems;
    }

    /**
     * method to save the current image/position to show the correct item in ViewPager and not the first item
     */
    @Override
    public void setViewPagerSelectedItem(int selectedViewPagerItemPosition) {
        this.selectedViewPagerItemPosition = selectedViewPagerItemPosition;
    }

    @Override
    public int getViewPagerSelectedItem() {
        return selectedViewPagerItemPosition;
    }

    @Override
    public void showDeleteButton() {
        galleryFragment.showDeleteButton();
    }

    @Override
    public void hideDeleteButton() {
        galleryFragment.hideDeleteButton();
    }

    /**
     * method to delete the selected images from gallery
     */
    @Override
    public void deleteSelectedItems() {
        for (String item : selectedItems) {
            File file = new File(item);
            if (file.delete()) {
                galleryItems.remove(item);
            }
        }
        selectedItems.clear();
        galleryFragment.notifyOnItemsDelete();
    }

    @Override
    public void setSelectedItem(String item) {
        selectedItems.add(item);
    }

    @Override
    public List<String> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public void removeSelectedItem(String item) {
        selectedItems.remove(item);
    }

    @Override
    public void clearSelectedItems() {
        selectedItems.clear();
    }
}
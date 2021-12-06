package com.esp32camera.home.gallery;

import android.os.Environment;
import android.widget.Toast;

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

    @Override
    public void deleteSelectedItems() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, selectedItems.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        for (String item : selectedItems) {
            File file = new File(item);
            file.delete();

            galleryItems.remove(item);
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
}
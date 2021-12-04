package com.esp32camera.home;

import com.esp32camera.MainActivity;
import com.esp32camera.model.CameraCard;

public class HomePresenter implements HomeContract.Presenter {

    private final MainActivity mainActivity;
    private HomeFragment homeFragment;

    public HomePresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void setView(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    public void addNewCameraCard(CameraCard cameraCard) {
        // add new cardView with WebView/WebServer
        homeFragment.addNewCameraCard(cameraCard);
    }

    @Override
    public HomeFragment getView() {
        return homeFragment;
    }

    @Override
    public void removeCameraCard(CameraCard cameraCard) {
        homeFragment.removeCameraCard(cameraCard);
    }
}
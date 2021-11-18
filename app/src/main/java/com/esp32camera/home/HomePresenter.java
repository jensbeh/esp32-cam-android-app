package com.esp32camera.home;

import com.esp32camera.MainActivity;

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
}
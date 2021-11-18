package com.esp32camera;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.net.WebSocketService;

public class MainPresenter implements MainContract.Presenter {

    private final WebSocketService webSocketService;
    private MainContract.View mainView;
    private CamSettingsPresenter camSettingsPresenter;

    public MainPresenter(MainContract.View mainView, CamSettingsPresenter camSettingsPresenter) {
        this.mainView = mainView;
        this.camSettingsPresenter = camSettingsPresenter;

        webSocketService = new WebSocketService(this, camSettingsPresenter);
        webSocketService.startWebSocketService();
    }

    @Override
    public void changeToSelectedFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_gallery:
                mainView.navigateToGalleryFragment();
                break;
            case R.id.nav_item_home:
                mainView.navigateToHomeFragment();
                break;
            case R.id.nav_item_notifications:
                mainView.navigateToNotificationFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void navigateToCamSettingsFragment() {
        mainView.navigateToCamSettingsFragment();
    }

    @Override
    public void navigateToHomeFragment() {
        mainView.navigateToHomeFragment();
    }

    @Override
    public void sendWebSocketMessage(String message) {
        webSocketService.sendMessage(message);
    }
}

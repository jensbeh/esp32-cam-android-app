package com.esp32camera;

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
    public void navigateToSettingsFragment() {
        mainView.navigateToSettingsFragment();
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

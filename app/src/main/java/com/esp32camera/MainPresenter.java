package com.esp32camera;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.Toast;

import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.home.HomePresenter;
import com.esp32camera.net.WebSocketService;
import com.esp32camera.net.WebSocketServiceInterface;

public class MainPresenter implements MainContract.Presenter {

    private final WebSocketService webSocketService;
    private MainActivity mainActivity;
    private MainActivity.State viewState;
    private HomePresenter homePresenter;
    private CamSettingsPresenter camSettingsPresenter;

    public MainPresenter(MainActivity mainActivity, HomePresenter homePresenter, CamSettingsPresenter camSettingsPresenter) {
        this.mainActivity = mainActivity;
        this.homePresenter = homePresenter;
        this.camSettingsPresenter = camSettingsPresenter;

        viewState = MainActivity.State.HomeFragment;

        webSocketService = new WebSocketService(this, camSettingsPresenter, new WebSocketServiceInterface() {
            @Override
            public void OnConnectionOpened(String status) {
                camSettingsPresenter.onWebSocketConnectionOpened();
            }

            @Override
            public void OnConnectionClosed(String status) {
                // here i can show error view of the specific webView
                homePresenter.onWebSocketConnectionClosed();
                camSettingsPresenter.onWebSocketConnectionClosed();
            }

            @Override
            public void OnConnectionFailed(String status) {
            }
        });
        webSocketService.startWebSocketService();
    }

    @Override
    public void changeToSelectedFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_gallery:
                viewState = MainActivity.State.GalleryFragment;
                mainActivity.navigateToGalleryFragment();
                break;
            case R.id.nav_item_home:
                viewState = MainActivity.State.HomeFragment;
                mainActivity.navigateToHomeFragment();
                break;
            case R.id.nav_item_notifications:
                viewState = MainActivity.State.NotificationFragment;
                mainActivity.navigateToNotificationFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return mainActivity;
    }

    @Override
    public boolean isWebSocketConnected() {
        return webSocketService.isWebSocketConnected();
    }

    @Override
    public void onNewEspCameraConnected(String ipAddress) {
        Toast.makeText(mainActivity, ipAddress, Toast.LENGTH_SHORT).show();

        // create new cardView

        // connect to WebServer

        // connect to WebSocket
    }

    @Override
    public void navigateToCamSettingsFragment() {
        viewState = MainActivity.State.CamSettingsFragment;
        mainActivity.navigateToCamSettingsFragment();
    }

    @Override
    public void navigateToHomeFragment() {
        viewState = MainActivity.State.HomeFragment;
        mainActivity.navigateToHomeFragment();
    }

    @Override
    public void sendWebSocketMessage(String message) {
        webSocketService.sendMessage(message);
    }

    public MainActivity.State getViewState() {
        return viewState;
    }
}

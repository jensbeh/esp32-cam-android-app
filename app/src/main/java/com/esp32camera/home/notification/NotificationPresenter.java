package com.esp32camera.home.notification;

import com.esp32camera.MainActivity;

public class NotificationPresenter implements NotificationContract.Presenter {

    private final MainActivity mainActivity;
    private NotificationFragment notificationFragment;

    public NotificationPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void setView(NotificationFragment notificationFragment) {
        this.notificationFragment = notificationFragment;
    }
}
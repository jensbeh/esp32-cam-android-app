package com.esp32camera.home.notification;

public interface NotificationContract {

    interface View { // from presenter to view

    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(NotificationFragment notificationFragment);
    }
}
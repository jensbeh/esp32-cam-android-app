package com.esp32camera.home.notification;

import com.esp32camera.model.Notification;

import java.util.List;

public interface NotificationContract {

    interface View { // from presenter to view
        void notifyOnMotionDetected();

        void scrollToTop();

        void showDeleteButton();

        void hideDeleteButton();

        void notifyOnItemsDelete();
    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(NotificationFragment notificationFragment);

        void notifyOnMotionDetected(Notification notification);

        void scrollToTop();

        void showDeleteButton();

        void hideDeleteButton();

        void deleteSelectedItems();

        void setSelectedItem(Notification item);

        List<Notification> getSelectedItems();

        void removeSelectedItem(Notification item);

        void clearSelectedItems();
    }
}
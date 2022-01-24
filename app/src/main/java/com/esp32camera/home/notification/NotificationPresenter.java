package com.esp32camera.home.notification;

import com.esp32camera.MainActivity;
import com.esp32camera.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationPresenter implements NotificationContract.Presenter {

    private final MainActivity mainActivity;
    private NotificationFragment notificationFragment;

    private List<Notification> selectedItems;

    public NotificationPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        selectedItems = new ArrayList<>();
    }

    @Override
    public void setView(NotificationFragment notificationFragment) {
        this.notificationFragment = notificationFragment;
    }

    /**
     * method to add new notification on motion detected
     */
    @Override
    public void notifyOnMotionDetected(Notification notification) {
        notificationFragment.notifyOnMotionDetected();
    }

    @Override
    public void scrollToTop() {
        notificationFragment.scrollToTop();
    }

    @Override
    public void showDeleteButton() {
        notificationFragment.showDeleteButton();
    }

    @Override
    public void hideDeleteButton() {
        notificationFragment.hideDeleteButton();
    }

    /**
     * method to remove selected items from list and refresh rv
     * notifications are deleted in mainPresenter from NotificationFragment
     */
    @Override
    public void deleteSelectedItems() {
        selectedItems.clear();
        notificationFragment.notifyOnItemsDelete();
    }

    /**
     * method to set selected items
     */
    @Override
    public void setSelectedItem(Notification item) {
        selectedItems.add(item);
    }

    @Override
    public List<Notification> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public void removeSelectedItem(Notification item) {
        selectedItems.remove(item);
    }

    @Override
    public void clearSelectedItems() {
        selectedItems.clear();
    }
}
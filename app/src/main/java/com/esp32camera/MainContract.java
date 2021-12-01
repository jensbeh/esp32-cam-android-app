package com.esp32camera;

import android.app.Activity;
import android.view.MenuItem;

public interface MainContract {

    interface View { // from presenter to view
        void navigateToHomeFragment();
        void navigateToGalleryFragment();
        void navigateToNotificationFragment();

        void navigateToCamSettingsFragment();
    }

    interface Model { // from presenter to model and back

    }

    interface Presenter {
        void navigateToCamSettingsFragment(); // from view to presenter (and back)
        void navigateToHomeFragment(); // from view to presenter (and back)

        void sendWebSocketMessage(String message);

        // from bottom navigation bar
        void changeToSelectedFragment(MenuItem item);

        Activity getActivity();

        boolean isWebSocketConnected();
    }
}

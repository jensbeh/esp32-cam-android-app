package com.example.esp32camera;

public interface MainContract {

    interface View { // from presenter to view
        void navigateToSettingsFragment();
        void navigateToHomeFragment();
    }

    interface Model { // from presenter to model and back

    }

    interface Presenter {
        void navigateToSettingsFragment(); // from view to presenter (and back)
        void navigateToHomeFragment(); // from view to presenter (and back)

        void sendWebSocketMessage(String message);
    }
}

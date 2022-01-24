package com.esp32camera.home;

import com.esp32camera.model.CameraCard;

public interface HomeContract {

    interface View { // from presenter to view
        void addNewCameraCard(CameraCard cameraCard);

        void removeCameraCard(CameraCard cameraCard);
    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(HomeFragment homeFragment);

        void addNewCameraCard(CameraCard cameraCard);

        HomeFragment getView();

        void removeCameraCard(CameraCard cameraCard);
    }
}
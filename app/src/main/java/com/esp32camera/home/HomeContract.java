package com.esp32camera.home;

import com.esp32camera.model.CameraCard;
import com.esp32camera.model.EspCamera;

public interface HomeContract {

    interface View {
        void addNewCameraCard(CameraCard cameraCard); // from presenter to view

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
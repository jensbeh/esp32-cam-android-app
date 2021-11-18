package com.esp32camera.camSettings;
public interface CamSettingsContract {

    interface View { // from presenter to view
        void setSliderCameraBrightness(int brightness);
        void setSliderCameraContrast(int contrast);

        void setCameraNameInText(String cameraName);
    }

    interface Model { // from presenter to model and back
        int getBrightness();
        void setBrightness(int brightness);

        int getContrast();
        void setContrast(int contrast);

        String getName();
        void setName(String cameraName);
    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(CamSettingsFragment camSettingsFragment);

        void setCameraBrightness(int brightness);
        int getCameraBrightness();
        void setCameraContrast(int contrast);
        int getCameraContrast();

        void setCameraName(String cameraName);
        String getCameraName();
    }
}
package com.esp32camera;

import android.app.Activity;
import android.view.MenuItem;

import com.esp32camera.model.CameraCard;
import com.esp32camera.model.EspCamera;

import java.util.Map;

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
        void navigateToCamSettingsFragment(EspCamera espCamera); // from view to presenter (and back)

        void navigateToHomeFragment(); // from view to presenter (and back)

        void sendWebSocketMessage(String message);

        // from bottom navigation bar
        void changeToSelectedFragment(MenuItem item);

        Activity getActivity();

        boolean isWebSocketConnected();

        void setupNewEspCamera(String ipAddress, String name); // from BottomSheetAddEspCamera

        void setCameraName(String newName);

        void setCameraFramesize(EspCamera espCamera, int framesize);

        void setCameraQuality(EspCamera espCamera, int quality);

        void setCameraBrightness(EspCamera espCamera, int brightness);

        void setCameraContrast(EspCamera espCamera, int contrast);

        void setCameraSaturation(EspCamera espCamera, int saturation);

        void setCameraSpecialEffect(EspCamera espCamera, int specialEffect);

        void setCameraAutoWhiteBalanceState(EspCamera espCamera, int autoWhiteBalanceState);

        void setCameraAutoWbGain(EspCamera espCamera, int autoWbGain);

        void setCameraWbMode(EspCamera espCamera, int wbMode);

        void setCameraExposureCtrlState(EspCamera espCamera, int exposureCtrlState);

        void setCameraAecValue(EspCamera espCamera, int aecValue);

        void setCameraAec2(EspCamera espCamera, int aec2);

        void setCameraAeLevel(EspCamera espCamera, int aeLevel);

        void setCameraAgcCtrlState(EspCamera espCamera, int agcCtrlState);

        void setCameraAgcGain(EspCamera espCamera, int agcGain);

        void setCameraGainCeiling(EspCamera espCamera, int gainCeiling);

        void setCameraBpc(EspCamera espCamera, int bpc);

        void setCameraWpc(EspCamera espCamera, int wpc);

        void setCameraRawGma(EspCamera espCamera, int rawGma);

        void setCameraLenc(EspCamera espCamera, int lenc);

        void setCameraHmirror(EspCamera espCamera, int hMirror);

        void setCameraVflip(EspCamera espCamera, int vFlip);

        void setCameraColorbar(EspCamera espCamera, int colorbar);

        Map<String, CameraCard> getCameraCardMap();

        boolean ifCameraExisting(String ipAddress);

        void resetCameraValues(EspCamera espCamera);

        void removeCamera(EspCamera espCamera);

        void loadEspCameras();
        void saveEspCameras();
    }
}

package com.esp32camera.camSettings;

import com.esp32camera.model.EspCamera;

public interface CamSettingsContract {

    interface View { // from presenter to view
        void setCameraNameInText(String cameraName);

        void setSpinnerCameraFramesize(int specialEffect);

        void setSliderCameraQuality(int quality);

        void setSliderCameraBrightness(int brightness);

        void setSliderCameraContrast(int contrast);

        void setSliderCameraSaturation(int saturation);

        void setSpinnerCameraSpecialEffect(int specialEffect);

        void setSwitchCameraAutoWhiteBalanceState(int autoWhiteBalanceState);

        void setSwitchCameraAutoWbGain(int autoWbGain);

        void setSpinnerCameraWbMode(int wbMode);

        void setSwitchCameraExposureCtrlState(int exposureCtrlState);

        void setSliderCameraAecValue(int aecValue);

        void setSwitchCameraAec2(int aec2);

        void setSliderCameraAeLevel(int aeLevel);

        void setSwitchCameraAgcCtrlState(int agcCtrlState);

        void setSliderCameraAgcGain(int agcGain);

        void setSliderCameraGainCeiling(int gainCeiling);

        void setSwitchCameraBpc(int bpc);

        void setSwitchCameraWpc(int wpc);

        void setSwitchCameraRawGma(int rawGma);

        void setSwitchCameraLenc(int lenc);

        void setSwitchCameraHmirror(int hMirror);

        void setSwitchCameraVflip(int vFlip);

        void setSwitchCameraColorbar(int colorbar);

        void enableCamSettings();
        void disableCamSettings();
    }

    interface Model { // from presenter to model and back

    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(CamSettingsFragment camSettingsFragment);

        void onWebSocketConnectionOpened();
        void onWebSocketConnectionClosed();

        void setCameraName(String cameraName);

        void setCameraFramesize(int framesize);

        void setCameraQuality(int quality);

        void setCameraBrightness(int brightness);

        void setCameraContrast(int contrast);

        void setCameraSaturation(int saturation);

        void setCameraSpecialEffect(int specialEffect);

        void setCameraAutoWhiteBalanceState(int autoWhiteBalanceState);

        void setCameraAutoWbGain(int autoWbGain);

        void setCameraWbMode(int wbMode);

        void setCameraExposureCtrlState(int exposureCtrlState);

        void setCameraAecValue(int aecValue);

        void setCameraAec2(int aec2);

        void setCameraAeLevel(int aeLevel);

        void setCameraAgcCtrlState(int agcCtrlState);

        void setCameraAgcGain(int agcGain);

        void setCameraGainCeiling(int gainCeiling);

        void setCameraBpc(int bpc);

        void setCameraWpc(int wpc);

        void setCameraRawGma(int rawGma);

        void setCameraLenc(int lenc);

        void setCameraHmirror(int hMirror);

        void setCameraVflip(int vFlip);

        void setCameraColorbar(int colorbar);


        void setSelectedEspCamera(EspCamera espCamera);

        EspCamera getEspCamera();
    }
}
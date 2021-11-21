package com.esp32camera.camSettings;

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
    }

    interface Model { // from presenter to model and back

        void setName(String cameraName);

        String getName();

        void setFramesize(int framesize);

        int getFramesize();

        void setQuality(int quality);

        int getQuality();

        void setBrightness(int brightness);

        int getBrightness();

        void setContrast(int contrast);

        int getContrast();

        void setSaturation(int saturation);

        int getSaturation();

        void setSpecialEffect(int specialEffect);

        int getSpecialEffect();

        void setAutoWhiteBalanceState(int autoWhiteBalanceState);

        int getAutoWhiteBalanceState();

        void setAutoWbGain(int autoWbGain);

        int getAutoWbGain();

        void setWbMode(int wbMode);

        int getWbMode();

        void setExposureCtrlState(int exposureCtrlState);

        int getExposureCtrlState();

        void setAecValue(int aecValue);

        int getAecValue();

        void setAec2(int aec2);

        int getAec2();

        void setAeLevel(int aeLevel);

        int getAeLevel();

        void setAgcCtrlState(int agcCtrlState);

        int getAgcCtrlState();

        void setAgcGain(int agcGain);

        int getAgcGain();

        void setGainCeiling(int gainCeiling);

        int getGainCeiling();

        void setBpc(int bpc);

        int getBpc();

        void setWpc(int wpc);

        int getWpc();

        void setRawGma(int rawGma);

        int getRawGma();

        void setLenc(int lenc);

        int getLenc();

        void setHmirror(int hMirror);

        int getHmirror();

        void setVflip(int vFlip);

        int getVflip();

        void setColorbar(int colorbar);

        int getColorbar();
    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(CamSettingsFragment camSettingsFragment);

        void setCameraName(String cameraName);

        String getCameraName();

        void setCameraFramesize(int framesize);

        int getCameraFramesize();

        void setCameraQuality(int quality);

        int getCameraQuality();

        void setCameraBrightness(int brightness);

        int getCameraBrightness();

        void setCameraContrast(int contrast);

        int getCameraContrast();

        void setCameraSaturation(int saturation);

        int getCameraSaturation();

        void setCameraSpecialEffect(int specialEffect);

        int getCameraSpecialEffect();

        void setCameraAutoWhiteBalanceState(int autoWhiteBalanceState);

        int getCameraAutoWhiteBalanceState();

        void setCameraAutoWbGain(int autoWbGain);

        int getCameraAutoWbGain();

        void setCameraWbMode(int wbMode);

        int getCameraWbMode();

        void setCameraExposureCtrlState(int exposureCtrlState);

        int getCameraExposureCtrlState();

        void setCameraAecValue(int aecValue);

        int getCameraAecValue();

        void setCameraAec2(int aec2);

        int getCameraAec2();

        void setCameraAeLevel(int aeLevel);

        int getCameraAeLevel();

        void setCameraAgcCtrlState(int agcCtrlState);

        int getCameraAgcCtrlState();

        void setCameraAgcGain(int agcGain);

        int getCameraAgcGain();

        void setCameraGainCeiling(int gainCeiling);

        int getCameraGainCeiling();

        void setCameraBpc(int bpc);

        int getCameraBpc();

        void setCameraWpc(int wpc);

        int getCameraWpc();

        void setCameraRawGma(int rawGma);

        int getCameraRawGma();

        void setCameraLenc(int lenc);

        int getCameraLenc();

        void setCameraHmirror(int hMirror);

        int getCameraHmirror();

        void setCameraVflip(int vFlip);

        int getCameraVflip();

        void setCameraColorbar(int colorbar);

        int getCameraColorbar();
    }
}
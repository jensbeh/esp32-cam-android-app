package com.esp32camera.camSettings;
public interface CamSettingsContract {

    interface View { // from presenter to view
        void setSliderCameraBrightness(int brightness);

        void setSliderCameraContrast(int contrast);

        void setCameraNameInText(String cameraName);

        void setSliderCameraQuality(int quality);

        void setSliderCameraSaturation(int saturation);

        void setSliderCameraSharpness(int sharpness);

        void setCameraDenoiseInView(int denoise);

        void setSpinnerCameraSpecialEffect(int specialEffect);

        void setSwitchCameraWhitebal(int whitebal);

        void setSwitchCameraAwbGain(int awbGain);

        void setSliderCameraWbMode(int wbMode);

        void setSwitchCameraExposureCtrl(int exposureCtrl);

        void setSwitchCameraAec2(int aec2);

        void setSliderCameraAeLevel(int aeLevel);

        void setSliderCameraAecValue(int aecValue);

        void setSwitchCameraGainCtrl(int gainCtrl);

        void setSliderCameraAgcGain(int agcGain);

        void setSliderCameraGainCeiling(int gainCeiling);

        void setSwitchCameraBpc(int bpc);

        void setSwitchCameraWpc(int wpc);

        void setSwitchCameraRawGma(int rawGma);

        void setSwitchCameraLenc(int lenc);

        void setSwitchCameraHmirror(int hMirror);

        void setSwitchCameraVflip(int vFlip);

        void setSwitchCameraDcw(int dcw);

        void setSwitchCameraColorbar(int colorbar);
    }

    interface Model { // from presenter to model and back
        void setBrightness(int brightness);
        int getBrightness();

        void setContrast(int contrast);
        int getContrast();

        void setName(String cameraName);
        String getName();

        void setQuality(int quality);
        int getQuality();

        void setSaturation(int saturation);
        int getSaturation();

        void setSharpness(int sharpness);
        int getSharpness();

        void setDenoise(int denoise);
        int getDenoise();

        void setSpecialEffect(int specialEffect);
        int getSpecialEffect();

        void setWhitebal(int whitebal);
        int getWhitebal();

        void setAwbGain(int awbGain);
        int getAwbGain();

        void setWbMode(int wbMode);
        int getWbMode();

        void setExposureCtrl(int exposureCtrl);
        int getExposureCtrl();

        void setAec2(int aec2);
        int getAec2();

        void setAeLevel(int aeLevel);
        int getAeLevel();

        void setAecValue(int aecValue);
        int getAecValue();

        void setGainCtrl(int gainCtrl);
        int getGainCtrl();

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

        void setDcw(int dcw);
        int getDcw();

        void setColorbar(int colorbar);
        int getColorbar();
    }

    interface Presenter { // from view/service to presenter (and back)
        void setView(CamSettingsFragment camSettingsFragment);

        void setCameraBrightness(int brightness);
        int getCameraBrightness();

        void setCameraContrast(int contrast);
        int getCameraContrast();

        void setCameraName(String cameraName);
        String getCameraName();

        void setCameraQuality(int quality);
        int getCameraQuality();

        void setCameraSaturation(int saturation);
        int getCameraSaturation();

        void setCameraSharpness(int sharpness);
        int getCameraSharpness();

        void setCameraDenoise(int denoise);
        int getCameraDenoise();

        void setCameraSpecialEffect(int specialEffect);
        int getCameraSpecialEffect();

        void setCameraWhitebal(int whitebal);
        int getCameraWhitebal();

        void setCameraAwbGain(int awbGain);
        int getCameraAwbGain();

        void setCameraWbMode(int wbMode);
        int getCameraWbMode();

        void setCameraExposureCtrl(int exposureCtrl);
        int getCameraExposureCtrl();

        void setCameraAec2(int aec2);
        int getCameraAec2();

        void setCameraAeLevel(int aeLevel);
        int getCameraAeLevel();

        void setCameraAecValue(int aecValue);
        int getCameraAecValue();

        void setCameraGainCtrl(int gainCtrl);
        int getCameraGainCtrl();

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

        void setCameraDcw(int dcw);
        int getCameraDcw();

        void setCameraColorbar(int colorbar);
        int getCameraColorbar();
    }
}
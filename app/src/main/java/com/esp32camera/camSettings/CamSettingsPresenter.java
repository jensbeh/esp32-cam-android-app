package com.esp32camera.camSettings;

import com.esp32camera.MainActivity;
import com.esp32camera.model.EspCamera;

public class CamSettingsPresenter implements CamSettingsContract.Presenter {

    private final MainActivity mainActivity;
    private CamSettingsFragment camSettingsFragment;
    private EspCamera espCamera;

    public CamSettingsPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void setView(CamSettingsFragment camSettingsFragment) {
        this.camSettingsFragment = camSettingsFragment;
    }

    @Override
    public void onWebSocketConnectionOpened() {
        camSettingsFragment.enableCamSettings();
    }

    @Override
    public void onWebSocketConnectionClosed() {
        camSettingsFragment.disableCamSettings();
    }

    @Override
    public void setCameraName(String cameraName) {
        camSettingsFragment.setCameraNameInText(cameraName);
    }

    @Override
    public void setCameraFramesize(int framesize) {
        // set to Spinner in settings if opened
        camSettingsFragment.setSpinnerCameraFramesize(framesize);
    }

    @Override
    public void setCameraQuality(int quality) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraQuality(quality);
    }

    @Override
    public void setCameraBrightness(int brightness) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraBrightness(brightness);
    }

    @Override
    public void setCameraContrast(int contrast) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraContrast(contrast);
    }

    @Override
    public void setCameraSaturation(int saturation) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraSaturation(saturation);
    }

    @Override
    public void setCameraSpecialEffect(int specialEffect) {
        // set to Spinner in settings if opened
        camSettingsFragment.setSpinnerCameraSpecialEffect(specialEffect);
    }

    @Override
    public void setCameraAutoWhiteBalanceState(int autoWhiteBalanceState) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAutoWhiteBalanceState(autoWhiteBalanceState);
    }

    @Override
    public void setCameraAutoWbGain(int autoWbGain) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAutoWbGain(autoWbGain);
    }

    @Override
    public void setCameraWbMode(int wbMode) {
        // set to Spinner in settings if opened
        camSettingsFragment.setSpinnerCameraWbMode(wbMode);
    }

    @Override
    public void setCameraExposureCtrlState(int exposureCtrlState) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraExposureCtrlState(exposureCtrlState);
    }

    @Override
    public void setCameraAecValue(int aecValue) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraAecValue(aecValue);
    }

    @Override
    public void setCameraAec2(int aec2) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAec2(aec2);
    }

    @Override
    public void setCameraAeLevel(int aeLevel) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraAeLevel(aeLevel);
    }

    @Override
    public void setCameraAgcCtrlState(int agcCtrlState) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAgcCtrlState(agcCtrlState);
    }

    @Override
    public void setCameraAgcGain(int agcGain) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraAgcGain(agcGain);
    }

    @Override
    public void setCameraGainCeiling(int gainCeiling) {
        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraGainCeiling(gainCeiling);
    }

    @Override
    public void setCameraBpc(int bpc) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraBpc(bpc);
    }

    @Override
    public void setCameraWpc(int wpc) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraWpc(wpc);
    }

    @Override
    public void setCameraRawGma(int rawGma) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraRawGma(rawGma);
    }

    @Override
    public void setCameraLenc(int lenc) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraLenc(lenc);
    }

    @Override
    public void setCameraHmirror(int hMirror) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraHmirror(hMirror);
    }

    @Override
    public void setCameraVflip(int vFlip) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraVflip(vFlip);
    }

    @Override
    public void setCameraColorbar(int colorbar) {
        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraColorbar(colorbar);
    }

    /**
     * method to set camera
     * when settings are opened we need to save the current camera to change settings
     */
    @Override
    public void setSelectedEspCamera(EspCamera espCamera) {
        this.espCamera = espCamera;
    }

    /**
     * method to get camera
     * when settings are opened we need to save the current camera to change settings
     */
    @Override
    public EspCamera getEspCamera() {
        return espCamera;
    }
}
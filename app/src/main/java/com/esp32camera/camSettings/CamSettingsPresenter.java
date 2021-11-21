package com.esp32camera.camSettings;

import com.esp32camera.MainActivity;
import com.esp32camera.model.EspCamera;

public class CamSettingsPresenter implements CamSettingsContract.Presenter {

    private final CamSettingsContract.Model espCamera;
    private final MainActivity mainActivity;
    private CamSettingsFragment camSettingsFragment;

    public CamSettingsPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.espCamera = new EspCamera();

        this.espCamera.setName("ESP Camera");
    }

    @Override
    public void setView(CamSettingsFragment camSettingsFragment) {
        this.camSettingsFragment = camSettingsFragment;
    }

    @Override
    public void setCameraName(String cameraName) {
        // set cameraName to model
        espCamera.setName(cameraName);

        // set to TextView in settings if opened
        camSettingsFragment.setCameraNameInText(cameraName);
    }

    @Override
    public String getCameraName() {
        return espCamera.getName();
    }

    @Override
    public void setCameraFramesize(int framesize) {
        // set framesize to model
        espCamera.setFramesize(framesize);
    }

    @Override
    public int getCameraFramesize() {
        return espCamera.getFramesize();
    }

    @Override
    public void setCameraQuality(int quality) {
        // set quality to model
        espCamera.setQuality(quality);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraQuality(quality);
    }

    @Override
    public int getCameraQuality() {
        return espCamera.getQuality();
    }

    @Override
    public void setCameraBrightness(int brightness) {
        // set brightness to model
        espCamera.setBrightness(brightness);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraBrightness(brightness);
    }

    @Override
    public int getCameraBrightness() {
        return espCamera.getBrightness();
    }

    @Override
    public void setCameraContrast(int contrast) {
        // set contrast to model
        espCamera.setContrast(contrast);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraContrast(contrast);
    }

    @Override
    public int getCameraContrast() {
        return espCamera.getContrast();
    }

    @Override
    public void setCameraSaturation(int saturation) {
        // set saturation to model
        espCamera.setSaturation(saturation);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraSaturation(saturation);
    }

    @Override
    public int getCameraSaturation() {
        return espCamera.getSaturation();
    }

    @Override
    public void setCameraSpecialEffect(int specialEffect) {
        // set specialEffect to model
        espCamera.setSpecialEffect(specialEffect);

        // set to Slider in settings if opened
        camSettingsFragment.setSpinnerCameraSpecialEffect(specialEffect);
    }

    @Override
    public int getCameraSpecialEffect() {
        return espCamera.getSpecialEffect();
    }

    @Override
    public void setCameraAutoWhiteBalanceState(int autoWhiteBalanceState) {
        // set autoWhiteBalanceState to model
        espCamera.setAutoWhiteBalanceState(autoWhiteBalanceState);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAutoWhiteBalanceState(autoWhiteBalanceState);
    }

    @Override
    public int getCameraAutoWhiteBalanceState() {
        return espCamera.getAutoWhiteBalanceState();
    }

    @Override
    public void setCameraAutoWbGain(int autoWbGain) {
        // set autoWbGain to model
        espCamera.setAutoWbGain(autoWbGain);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAutoWbGain(autoWbGain);
    }

    @Override
    public int getCameraAutoWbGain() {
        return espCamera.getAutoWbGain();
    }

    @Override
    public void setCameraWbMode(int wbMode) {
        // set wbMode to model
        espCamera.setWbMode(wbMode);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraWbMode(wbMode);
    }

    @Override
    public int getCameraWbMode() {
        return espCamera.getWbMode();
    }

    @Override
    public void setCameraExposureCtrlState(int exposureCtrlState) {
        // set exposureCtrlState to model
        espCamera.setExposureCtrlState(exposureCtrlState);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraExposureCtrlState(exposureCtrlState);
    }

    @Override
    public int getCameraExposureCtrlState() {
        return espCamera.getExposureCtrlState();
    }

    @Override
    public void setCameraAecValue(int aecValue) {
        // set aecValue to model
        espCamera.setAecValue(aecValue);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraAecValue(aecValue);
    }

    @Override
    public int getCameraAecValue() {
        return espCamera.getAecValue();
    }

    @Override
    public void setCameraAec2(int aec2) {
        // set aec2 to model
        espCamera.setAec2(aec2);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAec2(aec2);
    }

    @Override
    public int getCameraAec2() {
        return espCamera.getAec2();
    }

    @Override
    public void setCameraAeLevel(int aeLevel) {
        // set aeLevel to model
        espCamera.setAeLevel(aeLevel);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraAeLevel(aeLevel);
    }

    @Override
    public int getCameraAeLevel() {
        return espCamera.getAeLevel();
    }

    @Override
    public void setCameraAgcCtrlState(int agcCtrlState) {
        // set agcCtrlState to model
        espCamera.setAgcCtrlState(agcCtrlState);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraAgcCtrlState(agcCtrlState);
    }

    @Override
    public int getCameraAgcCtrlState() {
        return espCamera.getAgcCtrlState();
    }

    @Override
    public void setCameraAgcGain(int agcGain) {
        // set agcGain to model
        espCamera.setAgcGain(agcGain);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraAgcGain(agcGain);
    }

    @Override
    public int getCameraAgcGain() {
        return espCamera.getAgcGain();
    }

    @Override
    public void setCameraGainCeiling(int gainCeiling) {
        // set gainCeiling to model
        espCamera.setGainCeiling(gainCeiling);

        // set to Slider in settings if opened
        camSettingsFragment.setSliderCameraGainCeiling(gainCeiling);
    }

    @Override
    public int getCameraGainCeiling() {
        return espCamera.getGainCeiling();
    }

    @Override
    public void setCameraBpc(int bpc) {
        // set bpc to model
        espCamera.setBpc(bpc);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraBpc(bpc);
    }

    @Override
    public int getCameraBpc() {
        return espCamera.getBpc();
    }

    @Override
    public void setCameraWpc(int wpc) {
        // set wpc to model
        espCamera.setWpc(wpc);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraWpc(wpc);
    }

    @Override
    public int getCameraWpc() {
        return espCamera.getWpc();
    }

    @Override
    public void setCameraRawGma(int rawGma) {
        // set rawGma to model
        espCamera.setRawGma(rawGma);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraRawGma(rawGma);
    }

    @Override
    public int getCameraRawGma() {
        return espCamera.getRawGma();
    }

    @Override
    public void setCameraLenc(int lenc) {
        // set lenc to model
        espCamera.setLenc(lenc);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraLenc(lenc);
    }

    @Override
    public int getCameraLenc() {
        return espCamera.getLenc();
    }

    @Override
    public void setCameraHmirror(int hMirror) {
        // set hMirror to model
        espCamera.setHmirror(hMirror);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraHmirror(hMirror);
    }

    @Override
    public int getCameraHmirror() {
        return espCamera.getHmirror();
    }

    @Override
    public void setCameraVflip(int vFlip) {
        // set vFlip to model
        espCamera.setVflip(vFlip);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraVflip(vFlip);
    }

    @Override
    public int getCameraVflip() {
        return espCamera.getVflip();
    }

    @Override
    public void setCameraColorbar(int colorbar) {
        // set colorbar to model
        espCamera.setColorbar(colorbar);

        // set to Switch in settings if opened
        camSettingsFragment.setSwitchCameraColorbar(colorbar);
    }

    @Override
    public int getCameraColorbar() {
        return espCamera.getColorbar();
    }
}
package com.example.esp32camera.camSettings;

import com.example.esp32camera.MainActivity;
import com.example.esp32camera.model.EspCamera;

public class CamSettingsPresenter implements CamSettingsContract.Presenter {

    private final CamSettingsContract.Model espCamera;
    private final MainActivity mainActivity;
    private CamSettingsFragment camSettingsFragment;

    public CamSettingsPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.espCamera = new EspCamera();
    }

    @Override
    public void setView(CamSettingsFragment camSettingsFragment) {
        this.camSettingsFragment = camSettingsFragment;
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
}
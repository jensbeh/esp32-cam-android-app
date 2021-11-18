package com.esp32camera.model;

import com.esp32camera.camSettings.CamSettingsContract;

public class EspCamera implements CamSettingsContract.Model {
    int brightness;
    int contrast;
    String name;

    @Override
    public int getBrightness() {
        return brightness;
    }

    @Override
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    @Override
    public int getContrast() {
        return contrast;
    }

    @Override
    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String cameraName) {
        this.name = cameraName;
    }
}

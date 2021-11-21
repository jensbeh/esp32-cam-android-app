package com.esp32camera.model;

import com.esp32camera.camSettings.CamSettingsContract;

public class EspCamera implements CamSettingsContract.Model {
    String name;
    int framesize;
    int quality;
    int brightness;
    int contrast;
    int saturation;
    int specialEffect;
    int autoWhiteBalanceState;
    int autoWbGain;
    int wbMode;
    int exposureCtrlState;
    int aecValue;
    int aec2;
    int aeLevel;
    int agcCtrlState;
    int agcGain;
    int gainCeiling;
    int bpc;
    int wpc;
    int rawGma;
    int lenc;
    int hMirror;
    int vFlip;
    int colorbar;

    @Override
    public void setName(String cameraName) {
        this.name = cameraName;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setFramesize(int framesize) {
        this.framesize = framesize;
    }
    @Override
    public int getFramesize() {
        return framesize;
    }

    @Override
    public void setQuality(int quality) {
        this.quality = quality;
    }
    @Override
    public int getQuality() {
        return quality;
    }

    @Override
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
    @Override
    public int getBrightness() {
        return brightness;
    }

    @Override
    public void setContrast(int contrast) {
        this.contrast = contrast;
    }
    @Override
    public int getContrast() {
        return contrast;
    }

    @Override
    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
    @Override
    public int getSaturation() {
        return saturation;
    }

    @Override
    public void setSpecialEffect(int specialEffect) {
        this.specialEffect = specialEffect;
    }
    @Override
    public int getSpecialEffect() {
        return specialEffect;
    }

    @Override
    public void setAutoWhiteBalanceState(int autoWhiteBalanceState) {
        this.autoWhiteBalanceState = autoWhiteBalanceState;
    }
    @Override
    public int getAutoWhiteBalanceState() {
        return autoWhiteBalanceState;
    }

    @Override
    public void setAutoWbGain(int autoWbGain) {
        this.autoWbGain = autoWbGain;
    }
    @Override
    public int getAutoWbGain() {
        return autoWbGain;
    }

    @Override
    public void setWbMode(int wbMode) {
        this.wbMode = wbMode;
    }
    @Override
    public int getWbMode() {
        return wbMode;
    }

    @Override
    public void setExposureCtrlState(int exposureCtrlState) {
        this.exposureCtrlState = exposureCtrlState;
    }
    @Override
    public int getExposureCtrlState() {
        return exposureCtrlState;
    }

    @Override
    public void setAecValue(int aecValue) {
        this.aecValue = aecValue;
    }
    @Override
    public int getAecValue() {
        return aecValue;
    }

    @Override
    public void setAec2(int aec2) {
        this.aec2 = aec2;
    }
    @Override
    public int getAec2() {
        return aec2;
    }

    @Override
    public void setAeLevel(int aeLevel) {
        this.aeLevel = aeLevel;
    }
    @Override
    public int getAeLevel() {
        return aeLevel;
    }

    @Override
    public void setAgcCtrlState(int agcCtrlState) {
        this.agcCtrlState = agcCtrlState;
    }
    @Override
    public int getAgcCtrlState() {
        return agcCtrlState;
    }

    @Override
    public void setAgcGain(int agcGain) {
        this.agcGain = agcGain;
    }
    @Override
    public int getAgcGain() {
        return agcGain;
    }

    @Override
    public void setGainCeiling(int gainCeiling) {
        this.gainCeiling = gainCeiling;
    }
    @Override
    public int getGainCeiling() {
        return gainCeiling;
    }

    @Override
    public void setBpc(int bpc) {
        this.bpc = bpc;
    }
    @Override
    public int getBpc() {
        return bpc;
    }

    @Override
    public void setWpc(int wpc) {
        this.wpc = wpc;
    }
    @Override
    public int getWpc() {
        return wpc;
    }

    @Override
    public void setRawGma(int rawGma) {
        this.rawGma = rawGma;
    }
    @Override
    public int getRawGma() {
        return rawGma;
    }

    @Override
    public void setLenc(int lenc) {
        this.lenc = lenc;
    }
    @Override
    public int getLenc() {
        return lenc;
    }

    @Override
    public void setHmirror(int hMirror) {
        this.hMirror = hMirror;
    }
    @Override
    public int getHmirror() {
        return hMirror;
    }

    @Override
    public void setVflip(int vFlip) {
        this.vFlip = vFlip;
    }
    @Override
    public int getVflip() {
        return vFlip;
    }

    @Override
    public void setColorbar(int colorbar) {
        this.colorbar = colorbar;
    }
    @Override
    public int getColorbar() {
        return colorbar;
    }
}

package com.esp32camera.model;

import com.esp32camera.camSettings.CamSettingsContract;

public class EspCamera implements CamSettingsContract.Model {
    int brightness;
    int contrast;
    String name;
    int quality;
    int saturation;
    int sharpness;
    int denoise;
    int specialEffect;
    int whitebal;
    int awbGain;
    int wbMode;
    int exposureCtrl;
    int aec2;
    int aeLevel;
    int aecValue;
    int gainCtrl;
    int agcGain;
    int gainCeiling;
    int bpc;
    int wpc;
    int rawGma;
    int lenc;
    int hMirror;
    int vFlip;
    int dcw;
    int colorbar;

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
    public void setName(String cameraName) {
        this.name = cameraName;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setQuality(int quality) {

    }

    @Override
    public int getQuality() {
        return quality;
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
    public void setSharpness(int sharpness) {
        this.sharpness = sharpness;
    }

    @Override
    public int getSharpness() {
        return sharpness;
    }

    @Override
    public void setDenoise(int denoise) {
        this.denoise = denoise;
    }

    @Override
    public int getDenoise() {
        return denoise;
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
    public void setWhitebal(int whitebal) {
        this.whitebal = whitebal;
    }

    @Override
    public int getWhitebal() {
        return whitebal;
    }

    @Override
    public void setAwbGain(int awbGain) {
        this.awbGain = awbGain;
    }

    @Override
    public int getAwbGain() {
        return awbGain;
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
    public void setExposureCtrl(int exposureCtrl) {
        this.exposureCtrl = exposureCtrl;
    }

    @Override
    public int getExposureCtrl() {
        return exposureCtrl;
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
    public void setAecValue(int aecValue) {
        this.aecValue = aecValue;
    }

    @Override
    public int getAecValue() {
        return aecValue;
    }

    @Override
    public void setGainCtrl(int gainCtrl) {
        this.gainCtrl = gainCtrl;
    }

    @Override
    public int getGainCtrl() {
        return gainCtrl;
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
    public void setDcw(int dcw) {
        this.dcw = dcw;
    }

    @Override
    public int getDcw() {
        return dcw;
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

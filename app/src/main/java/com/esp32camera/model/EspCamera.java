package com.esp32camera.model;

public class EspCamera {
    private String ipAddress;

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
    int flashlightState;

    public EspCamera(String ipAddress, String name) {
        this.ipAddress = ipAddress;

        if (name.equals("")) {
            this.setName("ESP Camera");
        } else {
            this.setName(name);
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setName(String cameraName) {
        this.name = cameraName;
    }

    public String getName() {
        return name;
    }

    public void setFramesize(int framesize) {
        this.framesize = framesize;
    }

    public int getFramesize() {
        return framesize;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getContrast() {
        return contrast;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSpecialEffect(int specialEffect) {
        this.specialEffect = specialEffect;
    }

    public int getSpecialEffect() {
        return specialEffect;
    }

    public void setAutoWhiteBalanceState(int autoWhiteBalanceState) {
        this.autoWhiteBalanceState = autoWhiteBalanceState;
    }

    public int getAutoWhiteBalanceState() {
        return autoWhiteBalanceState;
    }

    public void setAutoWbGain(int autoWbGain) {
        this.autoWbGain = autoWbGain;
    }

    public int getAutoWbGain() {
        return autoWbGain;
    }

    public void setWbMode(int wbMode) {
        this.wbMode = wbMode;
    }

    public int getWbMode() {
        return wbMode;
    }

    public void setExposureCtrlState(int exposureCtrlState) {
        this.exposureCtrlState = exposureCtrlState;
    }

    public int getExposureCtrlState() {
        return exposureCtrlState;
    }

    public void setAecValue(int aecValue) {
        this.aecValue = aecValue;
    }

    public int getAecValue() {
        return aecValue;
    }

    public void setAec2(int aec2) {
        this.aec2 = aec2;
    }

    public int getAec2() {
        return aec2;
    }

    public void setAeLevel(int aeLevel) {
        this.aeLevel = aeLevel;
    }

    public int getAeLevel() {
        return aeLevel;
    }

    public void setAgcCtrlState(int agcCtrlState) {
        this.agcCtrlState = agcCtrlState;
    }

    public int getAgcCtrlState() {
        return agcCtrlState;
    }

    public void setAgcGain(int agcGain) {
        this.agcGain = agcGain;
    }

    public int getAgcGain() {
        return agcGain;
    }

    public void setGainCeiling(int gainCeiling) {
        this.gainCeiling = gainCeiling;
    }

    public int getGainCeiling() {
        return gainCeiling;
    }

    public void setBpc(int bpc) {
        this.bpc = bpc;
    }

    public int getBpc() {
        return bpc;
    }

    public void setWpc(int wpc) {
        this.wpc = wpc;
    }

    public int getWpc() {
        return wpc;
    }

    public void setRawGma(int rawGma) {
        this.rawGma = rawGma;
    }

    public int getRawGma() {
        return rawGma;
    }

    public void setLenc(int lenc) {
        this.lenc = lenc;
    }

    public int getLenc() {
        return lenc;
    }

    public void setHmirror(int hMirror) {
        this.hMirror = hMirror;
    }

    public int getHmirror() {
        return hMirror;
    }

    public void setVflip(int vFlip) {
        this.vFlip = vFlip;
    }

    public int getVflip() {
        return vFlip;
    }

    public void setColorbar(int colorbar) {
        this.colorbar = colorbar;
    }

    public int getColorbar() {
        return colorbar;
    }

    public void setFlashlightState(int flashlightState) {
        this.flashlightState = flashlightState;
    }

    public int getFlashlightState() {
        return flashlightState;
    }
}

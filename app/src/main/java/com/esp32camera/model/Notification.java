package com.esp32camera.model;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Date;

public class Notification {
    private String cameraName;
    private String cameraIp;
    private Date timeStamp;
    private Bitmap pictureBmp;
    private String picturePath;

    public Notification() {

    }

    public Notification(String cameraName, String cameraIp) {
        this.cameraName = cameraName;
        this.cameraIp = cameraIp;

        this.timeStamp = Calendar.getInstance().getTime();
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraIp(String cameraIp) {
        this.cameraIp = cameraIp;
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setPictureBmp(Bitmap pictureBmp) {
        this.pictureBmp = pictureBmp;
    }

    public Bitmap getPictureBmp() {
        return pictureBmp;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPicturePath() {
        return picturePath;
    }
}

package com.esp32camera.model;

import java.util.Calendar;
import java.util.Date;

public class Notification {
    private String cameraName;
    private String cameraIp;
    private Date timeStamp;

    public Notification(String cameraName, String cameraIp) {
        this.cameraName = cameraName;
        this.cameraIp = cameraIp;

        this.timeStamp = Calendar.getInstance().getTime();
    }

    public String getCameraName() {
        return cameraName;
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}

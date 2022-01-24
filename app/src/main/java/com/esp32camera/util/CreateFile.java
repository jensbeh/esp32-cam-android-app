package com.esp32camera.util;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.esp32camera.model.EspCamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class to create specific files
 */
public class CreateFile {
    /**
     * create jpeg file to save pictures with espCameraIpAddress in name
     */
    public static File createJpegFile(EspCamera espCamera) {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_NOTIFICATION_" + espCamera.getName() + "_" + espCamera.getIpAddress() + "_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File image = new File(dir, imageFileName + ".jpg");
        return image;
    }

    /**
     * create jpeg file to save pictures
     */
    public static File createJpegFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File image = new File(dir, imageFileName + ".jpg");
        return image;
    }

    /**
     * create mp4 file to save video
     */
    public static File createMp4File() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File video = new File(dir, imageFileName + ".mp4");
        return video;
    }
}

package com.esp32camera.util;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.esp32camera.model.EspCamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateFile {
    public static File createJpegFile(EspCamera espCamera) {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_NOTIFICATION_" + espCamera.getName() + "_" + espCamera.getIpAddress() + "_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File image = new File(dir, imageFileName + ".jpg");
        Log.d(TAG, "file path is " + image.getAbsolutePath());
        return image;
    }

    public static File createJpegFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File image = new File(dir, imageFileName + ".jpg");
        Log.d(TAG, "file path is " + image.getAbsolutePath());
        return image;
    }

    public static File createMp4File() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File video = new File(dir, imageFileName + ".mp4");
        Log.d(TAG, "file path is " + video.getAbsolutePath());
        return video;
    }
}

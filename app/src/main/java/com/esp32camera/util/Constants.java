package com.esp32camera.util;

public class Constants {
    // Communication
    public static final String WEBSERVER_URL = "http://192.168.188.45:80";
    public static final String WEBSOCKETS_SERVER_URL = "ws://192.168.188.45:81";

    // Paths
    public static final String STREAM_PATH = "/stream";

    // Controls
    public static final String CAM_CONTROL_PATH = "camControls/";
    public static final String BRIGHTNESS_PATH = "brightness="; // -2  to 2
    public static final String CONTRAST_PATH = "contrast="; // -2 to 2
}
package com.esp32camera.util;

public class Constants {
    // Communication
    public static final String WEBSERVER_HTTP = "http://";
    public static final String WEBSERVER_PORT = ":80";
    public static final String WEBSOCKETS_SERVER_WS = "ws://";
    public static final String WEBSOCKETS_SERVER_PORT = ":81";

    // Paths
    public static final String STREAM_PATH = "/stream";

    // Controls
    public static final String CAM_CONTROLS_PATH = "camControls/";
    public static final String UPDATE_CAMERA_PATH = "updateCamera";
    public static final String FRAMESIZE_PATH = "framesize="; // 0,3,4,5,6,7,8,9,10
    public static final String QUALITY_PATH = "quality="; //10 - 63
    public static final String BRIGHTNESS_PATH = "brightness="; // -2  to 2
    public static final String CONTRAST_PATH = "contrast="; // -2 to 2
    public static final String SATURATION_PATH = "saturation="; // -2 to 2
    public static final String SPECIAL_EFFECT_PATH = "specialEffect="; // 0 to 6 (0 - No Effect, 1 - Negative, 2 - Grayscale, 3 - Red Tint, 4 - Green Tint, 5 - Blue Tint, 6 - Sepia)
    public static final String WHITEBALANCE_STATE_PATH = "autoWhiteBalance="; // 0 = disable , 1 = enable
    public static final String AUTOWB_GAIN_PATH = "autoWbGain="; // 0 = disable , 1 = enable
    public static final String WB_MODE_PATH = "wbMode="; // 0 to 4 - if awb_gain enabled (0 - Auto, 1 - Sunny, 2 - Cloudy, 3 - Office, 4 - Home)
    public static final String EXPOSURE_CTRL_STATE_PATH = "exposureCtrl="; // 0 = disable , 1 = enable
    public static final String AEC_VALUE_PATH = "aecValue="; // 0 - 1200
    public static final String AEC2_PATH = "aec2="; // 0 = disable , 1 = enable
    public static final String AE_LEVEL_PATH = "aeLevel="; // -2 to 2
    public static final String AGC_CTRL_STATE_PATH = "agcCtrl="; // 0 = disable , 1 = enable
    public static final String AGC_GAIN_PATH = "agcGain="; // 0 - 30
    public static final String GAINCEILING_PATH = "gainceiling="; // 0 to 6
    public static final String BPC_PATH = "bpc="; // 0 = disable , 1 = enable
    public static final String WPC_PATH = "wpc="; // 0 = disable , 1 = enable
    public static final String RAW_GMA_PATH = "rawGma="; // 0 = disable , 1 = enable
    public static final String LENC_PATH = "lenc="; // 0 = disable , 1 = enable
    public static final String HMIRROR_PATH = "hmirror="; // 0 = disable , 1 = enable
    public static final String VFLIP_PATH = "vflip="; // 0 = disable , 1 = enable
    public static final String COLORBAR_PATH = "colorbar="; // 0 = disable , 1 = enable
    public static final String FLASHLIGHT_PATH = "flashlight="; // 0 = disable , 1 = enable

    // Notifications
    public static final String CAM_NOTIFICATION_PATH = "camNotification/";
    public static final String MOTION_DETECTED_PATH = "motionDetection";
}
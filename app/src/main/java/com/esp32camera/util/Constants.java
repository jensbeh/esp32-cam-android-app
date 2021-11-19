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
    public static final String QUALITY_PATH = "quality="; // -2 to 2
    public static final String SATURATION_PATH = "saturation="; // -2 to 2
    public static final String SHARPNESS_PATH = "sharpness="; // -2 to 2
    public static final String DENOISE_PATH = "denoise="; // -2 to 2
    public static final String SPECIAL_EFFECT_PATH = "specialEffect="; // -2 to 2
    public static final String WHITEBAL_PATH = "whitebal="; // -2 to 2
    public static final String AWB_GAIN_PATH = "awbGain="; // -2 to 2
    public static final String WB_MODE_PATH = "wbMode="; // -2 to 2
    public static final String EXPOSURE_CTRL_PATH = "exposureCtrl="; // -2 to 2
    public static final String AEC2_PATH = "aec2="; // -2 to 2
    public static final String AE_LEVEL_PATH = "aeLevel="; // -2 to 2
    public static final String AEC_VALUE_PATH = "aecValue="; // -2 to 2
    public static final String GAIN_CTRL_PATH = "gainCtrl="; // -2 to 2
    public static final String AGC_GAIN_PATH = "agcGain="; // -2 to 2
    public static final String GAINCEILING_PATH = "gainceiling="; // -2 to 2
    public static final String BPC_PATH = "bpc="; // -2 to 2
    public static final String WPC_PATH = "wpc="; // -2 to 2
    public static final String RAW_GMA_PATH = "rawGma="; // -2 to 2
    public static final String LENC_PATH = "lenc="; // -2 to 2
    public static final String HMIRROR_PATH = "hmirror="; // -2 to 2
    public static final String VFLIP_PATH = "vflip="; // -2 to 2
    public static final String DCW_PATH = "dcw="; // -2 to 2
    public static final String COLORBAR_PATH = "colorbar="; // -2 to 2
}
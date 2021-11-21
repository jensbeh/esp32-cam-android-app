package com.esp32camera.net;

import static com.esp32camera.util.Constants.AEC2_PATH;
import static com.esp32camera.util.Constants.AEC_VALUE_PATH;
import static com.esp32camera.util.Constants.AE_LEVEL_PATH;
import static com.esp32camera.util.Constants.AGC_CTRL_STATE_PATH;
import static com.esp32camera.util.Constants.AGC_GAIN_PATH;
import static com.esp32camera.util.Constants.AUTOWB_GAIN_PATH;
import static com.esp32camera.util.Constants.BPC_PATH;
import static com.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.COLORBAR_PATH;
import static com.esp32camera.util.Constants.CONTRAST_PATH;
import static com.esp32camera.util.Constants.EXPOSURE_CTRL_STATE_PATH;
import static com.esp32camera.util.Constants.FRAMESIZE_PATH;
import static com.esp32camera.util.Constants.GAINCEILING_PATH;
import static com.esp32camera.util.Constants.HMIRROR_PATH;
import static com.esp32camera.util.Constants.LENC_PATH;
import static com.esp32camera.util.Constants.QUALITY_PATH;
import static com.esp32camera.util.Constants.RAW_GMA_PATH;
import static com.esp32camera.util.Constants.SATURATION_PATH;
import static com.esp32camera.util.Constants.SPECIAL_EFFECT_PATH;
import static com.esp32camera.util.Constants.VFLIP_PATH;
import static com.esp32camera.util.Constants.WB_MODE_PATH;
import static com.esp32camera.util.Constants.WEBSOCKETS_SERVER_URL;
import static com.esp32camera.util.Constants.WHITEBALANCE_STATE_PATH;
import static com.esp32camera.util.Constants.WPC_PATH;

import android.os.Build;
import android.util.Log;

import com.esp32camera.MainPresenter;
import com.esp32camera.camSettings.CamSettingsPresenter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class WebSocketService {

    private WebSocketClient webSocketClient;
    private MainPresenter mainPresenter;
    private CamSettingsPresenter camSettingsPresenter;

    public WebSocketService(MainPresenter mainPresenter, CamSettingsPresenter camSettingsPresenter) {
        this.mainPresenter = mainPresenter;
        this.camSettingsPresenter = camSettingsPresenter;
    }

    public void startWebSocketService() {

        URI uri = null;
        try {
            uri = new URI(WEBSOCKETS_SERVER_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        webSocketClient = new WebSocketClient(Objects.requireNonNull(uri)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                webSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                //Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show();
                handleMessage(message);

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        webSocketClient.connect();
    }

    private void handleMessage(String message) {
        if (message.contains(CAM_CONTROLS_PATH)) {
            // set framesize
            if (message.contains(FRAMESIZE_PATH)) {
                int framesize = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraFramesize() != framesize) {
                    camSettingsPresenter.setCameraFramesize(framesize);
                }
            }

            // set quality
            else if (message.contains(QUALITY_PATH)) {
                int quality = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraQuality() != quality) {
                    camSettingsPresenter.setCameraQuality(quality);
                }
            }

            // set brightness
            else if (message.contains(BRIGHTNESS_PATH)) {
                int brightness = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraBrightness() != brightness) {
                    camSettingsPresenter.setCameraBrightness(brightness);

                }
            }

            // set contrast
            else if (message.contains(CONTRAST_PATH)) {
                int contrast = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraContrast() != contrast) {
                    camSettingsPresenter.setCameraContrast(contrast);
                }
            }
            // set saturation
            else if (message.contains(SATURATION_PATH)) {
                int saturation = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraSaturation() != saturation) {
                    camSettingsPresenter.setCameraSaturation(saturation);
                }
            }

            // set specialEffect
            else if (message.contains(SPECIAL_EFFECT_PATH)) {
                int specialEffect = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraSpecialEffect() != specialEffect) {
                    camSettingsPresenter.setCameraSpecialEffect(specialEffect);
                }
            }

            // set autoWhiteBalanceState
            else if (message.contains(WHITEBALANCE_STATE_PATH)) {
                int autoWhiteBalanceState = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAutoWhiteBalanceState() != autoWhiteBalanceState) {
                    camSettingsPresenter.setCameraAutoWhiteBalanceState(autoWhiteBalanceState);
                }
            }

            // set autoWbGain
            else if (message.contains(AUTOWB_GAIN_PATH)) {
                int autoWbGain = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAutoWbGain() != autoWbGain) {
                    camSettingsPresenter.setCameraAutoWbGain(autoWbGain);
                }
            }

            // set wbMode
            else if (message.contains(WB_MODE_PATH)) {
                int wbMode = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraWbMode() != wbMode) {
                    camSettingsPresenter.setCameraWbMode(wbMode);
                }
            }

            // set exposureCtrlState
            else if (message.contains(EXPOSURE_CTRL_STATE_PATH)) {
                int exposureCtrlState = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraExposureCtrlState() != exposureCtrlState) {
                    camSettingsPresenter.setCameraExposureCtrlState(exposureCtrlState);
                }
            }

            // set aecValue
            else if (message.contains(AEC_VALUE_PATH)) {
                int aecValue = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAecValue() != aecValue) {
                    camSettingsPresenter.setCameraAecValue(aecValue);
                }
            }

            // set aec2
            else if (message.contains(AEC2_PATH)) {
                int aec2 = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAec2() != aec2) {
                    camSettingsPresenter.setCameraAec2(aec2);
                }
            }

            // set aeLevel
            else if (message.contains(AE_LEVEL_PATH)) {
                int aeLevel = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAeLevel() != aeLevel) {
                    camSettingsPresenter.setCameraAeLevel(aeLevel);
                }
            }

            // set agcCtrlState
            else if (message.contains(AGC_CTRL_STATE_PATH)) {
                int agcCtrlState = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAgcCtrlState() != agcCtrlState) {
                    camSettingsPresenter.setCameraAgcCtrlState(agcCtrlState);
                }
            }

            // set agcGain
            else if (message.contains(AGC_GAIN_PATH)) {
                int agcGain = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAgcGain() != agcGain) {
                    camSettingsPresenter.setCameraAgcGain(agcGain);
                }
            }

            // set gainCeiling
            else if (message.contains(GAINCEILING_PATH)) {
                int gainCeiling = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraGainCeiling() != gainCeiling) {
                    camSettingsPresenter.setCameraGainCeiling(gainCeiling);
                }
            }

            // set bpc
            else if (message.contains(BPC_PATH)) {
                int bpc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraBpc() != bpc) {
                    camSettingsPresenter.setCameraBpc(bpc);
                }
            }

            // set wpc
            else if (message.contains(WPC_PATH)) {
                int wpc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraWpc() != wpc) {
                    camSettingsPresenter.setCameraWpc(wpc);
                }
            }

            // set rawGma
            else if (message.contains(RAW_GMA_PATH)) {
                int rawGma = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraRawGma() != rawGma) {
                    camSettingsPresenter.setCameraRawGma(rawGma);
                }
            }

            // set lenc
            else if (message.contains(LENC_PATH)) {
                int lenc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraLenc() != lenc) {
                    camSettingsPresenter.setCameraLenc(lenc);
                }
            }

            // set hMirror
            else if (message.contains(HMIRROR_PATH)) {
                int hMirror = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraHmirror() != hMirror) {
                    camSettingsPresenter.setCameraHmirror(hMirror);
                }
            }

            // set vFlip
            else if (message.contains(VFLIP_PATH)) {
                int vFlip = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraVflip() != vFlip) {
                    camSettingsPresenter.setCameraVflip(vFlip);
                }
            }

            // set colorbar
            else if (message.contains(COLORBAR_PATH)) {
                int colorbar = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraColorbar() != colorbar) {
                    camSettingsPresenter.setCameraColorbar(colorbar);
                }
            }
        }
    }

    public void sendMessage(String message) {
        webSocketClient.send(message);
    }
}

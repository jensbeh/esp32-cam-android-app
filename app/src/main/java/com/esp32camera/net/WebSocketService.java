package com.esp32camera.net;

import static com.esp32camera.util.Constants.*;

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
        if (message.contains(CAM_CONTROL_PATH)) {
            if (message.contains(BRIGHTNESS_PATH)) {
                int brightness = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraBrightness() != brightness) {
                    camSettingsPresenter.setCameraBrightness(brightness);

                }
            } else if (message.contains(CONTRAST_PATH)) {
                int contrast = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraContrast() != contrast) {
                    camSettingsPresenter.setCameraContrast(contrast);
                }
            }
            else if (message.contains(QUALITY_PATH)) {
                int quality = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraQuality() != quality) {
                    camSettingsPresenter.setCameraQuality(quality);
                }
            }
            else if (message.contains(SATURATION_PATH)) {
                int saturation = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraSaturation() != saturation) {
                    camSettingsPresenter.setCameraSaturation(saturation);
                }
            }
            else if (message.contains(SHARPNESS_PATH)) {
                int sharpness = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraSharpness() != sharpness) {
                    camSettingsPresenter.setCameraSharpness(sharpness);
                }
            }
            else if (message.contains(DENOISE_PATH)) {
                int denoise = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraDenoise() != denoise) {
                    camSettingsPresenter.setCameraDenoise(denoise);
                }
            }
            else if (message.contains(SPECIAL_EFFECT_PATH)) {
                int specialEffect = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraSpecialEffect() != specialEffect) {
                    camSettingsPresenter.setCameraSpecialEffect(specialEffect);
                }
            }
            else if (message.contains(WHITEBAL_PATH)) {
                int whitebal = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraWhitebal() != whitebal) {
                    camSettingsPresenter.setCameraWhitebal(whitebal);
                }
            }
            else if (message.contains(AWB_GAIN_PATH)) {
                int awbGain = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAwbGain() != awbGain) {
                    camSettingsPresenter.setCameraAwbGain(awbGain);
                }
            }
            else if (message.contains(WB_MODE_PATH)) {
                int wbMode = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraWbMode() != wbMode) {
                    camSettingsPresenter.setCameraWbMode(wbMode);
                }
            }
            else if (message.contains(EXPOSURE_CTRL_PATH)) {
                int exposureCtrl = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraExposureCtrl() != exposureCtrl) {
                    camSettingsPresenter.setCameraExposureCtrl(exposureCtrl);
                }
            }
            else if (message.contains(AEC2_PATH)) {
                int aec2 = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAec2() != aec2) {
                    camSettingsPresenter.setCameraAec2(aec2);
                }
            }
            else if (message.contains(AE_LEVEL_PATH)) {
                int aeLevel = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAeLevel() != aeLevel) {
                    camSettingsPresenter.setCameraAeLevel(aeLevel);
                }
            }
            else if (message.contains(AEC_VALUE_PATH)) {
                int aecValue = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAecValue() != aecValue) {
                    camSettingsPresenter.setCameraAecValue(aecValue);
                }
            }
            else if (message.contains(GAIN_CTRL_PATH)) {
                int gainCtrl = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraGainCtrl() != gainCtrl) {
                    camSettingsPresenter.setCameraGainCtrl(gainCtrl);
                }
            }
            else if (message.contains(AGC_GAIN_PATH)) {
                int agcGain = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraAgcGain() != agcGain) {
                    camSettingsPresenter.setCameraAgcGain(agcGain);
                }
            }
            else if (message.contains(GAINCEILING_PATH)) {
                int gainCeiling = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraGainCeiling() != gainCeiling) {
                    camSettingsPresenter.setCameraGainCeiling(gainCeiling);
                }
            }
            else if (message.contains(BPC_PATH)) {
                int bpc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraBpc() != bpc) {
                    camSettingsPresenter.setCameraBpc(bpc);
                }
            }
            else if (message.contains(WPC_PATH)) {
                int wpc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraWpc() != wpc) {
                    camSettingsPresenter.setCameraWpc(wpc);
                }
            }
            else if (message.contains(RAW_GMA_PATH)) {
                int rawGma = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraRawGma() != rawGma) {
                    camSettingsPresenter.setCameraRawGma(rawGma);
                }
            }
            else if (message.contains(LENC_PATH)) {
                int lenc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraLenc() != lenc) {
                    camSettingsPresenter.setCameraLenc(lenc);
                }
            }
            else if (message.contains(HMIRROR_PATH)) {
                int hMirror = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraHmirror() != hMirror) {
                    camSettingsPresenter.setCameraHmirror(hMirror);
                }
            }
            else if (message.contains(VFLIP_PATH)) {
                int vFlip = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraVflip() != vFlip) {
                    camSettingsPresenter.setCameraVflip(vFlip);
                }
            }
            else if (message.contains(DCW_PATH)) {
                int dcw = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraDcw() != dcw) {
                    camSettingsPresenter.setCameraDcw(dcw);
                }
            }
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

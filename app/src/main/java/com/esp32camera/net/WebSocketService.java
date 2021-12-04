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
import static com.esp32camera.util.Constants.FLASHLIGHT_PATH;
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
import static com.esp32camera.util.Constants.WEBSOCKETS_SERVER_PORT;
import static com.esp32camera.util.Constants.WEBSOCKETS_SERVER_WS;
import static com.esp32camera.util.Constants.WHITEBALANCE_STATE_PATH;
import static com.esp32camera.util.Constants.WPC_PATH;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.esp32camera.MainPresenter;
import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.model.EspCamera;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Timer;

public class WebSocketService {

    private WebSocketClient webSocketClient;
    private MainPresenter mainPresenter;
    private EspCamera espCamera;
    private CamSettingsPresenter camSettingsPresenter;
    private WebSocketServiceInterface webSocketServiceInterface;
    private Timer noopTimer;

    public WebSocketService(MainPresenter mainPresenter, EspCamera espCamera, CamSettingsPresenter camSettingsPresenter, WebSocketServiceInterface webSocketServiceInterface) {
        this.mainPresenter = mainPresenter;
        this.espCamera = espCamera;
        this.camSettingsPresenter = camSettingsPresenter;
        this.webSocketServiceInterface = webSocketServiceInterface;
        this.noopTimer = new Timer();
    }

    public void startWebSocketService() {

        URI uri = null;
        try {
            uri = new URI(WEBSOCKETS_SERVER_WS + espCamera.getIpAddress() + WEBSOCKETS_SERVER_PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        webSocketClient = new WebSocketClient(Objects.requireNonNull(uri)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                webSocketServiceInterface.OnConnectionOpened(espCamera, "WEBSOCKET OPENED");
                Log.i("WebSocket", "Opened");
                webSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);

//                noopTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // Send NOOP Message
//                        sendMessage("COM_NOOP");
//                    }
//                }, 0, 1000 * 30);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                handleMessage(message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                webSocketServiceInterface.OnConnectionClosed(espCamera, "WEBSOCKET CLOSE");
                Log.i("WebSocket", "Closed " + s);

                // tries to reconnect the webSocket after 5 sec; if not working then there will be an onClose Error again with reconnect etc.
                mainPresenter.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("WebSocket", "Try to reconnect...");
                                webSocketClient.reconnect();
                            }
                        }, 5000);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                webSocketServiceInterface.OnConnectionFailed(espCamera, "WEBSOCKET ERROR");
                Log.i("WebSocket", "Error " + e.getMessage());
            }
        };
        webSocketClient.setConnectionLostTimeout(3);
        webSocketClient.connect();
    }

    private void handleMessage(String message) {
        if (message.contains(CAM_CONTROLS_PATH)) {
            // set framesize
            if (message.contains(FRAMESIZE_PATH)) {
                int framesize = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraFramesize(espCamera, framesize);
            }

            // set quality
            else if (message.contains(QUALITY_PATH)) {
                int quality = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraQuality(espCamera, quality);
            }

            // set brightness
            else if (message.contains(BRIGHTNESS_PATH)) {
                int brightness = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraBrightness(espCamera, brightness);
            }

            // set contrast
            else if (message.contains(CONTRAST_PATH)) {
                int contrast = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraContrast(espCamera, contrast);
            }
            // set saturation
            else if (message.contains(SATURATION_PATH)) {
                int saturation = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraSaturation(espCamera, saturation);
            }

            // set specialEffect
            else if (message.contains(SPECIAL_EFFECT_PATH)) {
                int specialEffect = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraSpecialEffect(espCamera, specialEffect);
            }

            // set autoWhiteBalanceState
            else if (message.contains(WHITEBALANCE_STATE_PATH)) {
                int autoWhiteBalanceState = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAutoWhiteBalanceState(espCamera, autoWhiteBalanceState);
            }

            // set autoWbGain
            else if (message.contains(AUTOWB_GAIN_PATH)) {
                int autoWbGain = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAutoWbGain(espCamera, autoWbGain);
            }

            // set wbMode
            else if (message.contains(WB_MODE_PATH)) {
                int wbMode = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraWbMode(espCamera, wbMode);
            }

            // set exposureCtrlState
            else if (message.contains(EXPOSURE_CTRL_STATE_PATH)) {
                int exposureCtrlState = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraExposureCtrlState(espCamera, exposureCtrlState);
            }

            // set aecValue
            else if (message.contains(AEC_VALUE_PATH)) {
                int aecValue = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAecValue(espCamera, aecValue);
            }

            // set aec2
            else if (message.contains(AEC2_PATH)) {
                int aec2 = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAec2(espCamera, aec2);
            }

            // set aeLevel
            else if (message.contains(AE_LEVEL_PATH)) {
                int aeLevel = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAeLevel(espCamera, aeLevel);
            }

            // set agcCtrlState
            else if (message.contains(AGC_CTRL_STATE_PATH)) {
                int agcCtrlState = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAgcCtrlState(espCamera, agcCtrlState);
            }

            // set agcGain
            else if (message.contains(AGC_GAIN_PATH)) {
                int agcGain = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraAgcGain(espCamera, agcGain);
            }

            // set gainCeiling
            else if (message.contains(GAINCEILING_PATH)) {
                int gainCeiling = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraGainCeiling(espCamera, gainCeiling);
            }

            // set bpc
            else if (message.contains(BPC_PATH)) {
                int bpc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraBpc(espCamera, bpc);
            }

            // set wpc
            else if (message.contains(WPC_PATH)) {
                int wpc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraWpc(espCamera, wpc);
            }

            // set rawGma
            else if (message.contains(RAW_GMA_PATH)) {
                int rawGma = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraRawGma(espCamera, rawGma);
            }

            // set lenc
            else if (message.contains(LENC_PATH)) {
                int lenc = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraLenc(espCamera, lenc);
            }

            // set hMirror
            else if (message.contains(HMIRROR_PATH)) {
                int hMirror = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraHmirror(espCamera, hMirror);
            }

            // set vFlip
            else if (message.contains(VFLIP_PATH)) {
                int vFlip = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraVflip(espCamera, vFlip);
            }

            // set colorbar
            else if (message.contains(COLORBAR_PATH)) {
                int colorbar = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraColorbar(espCamera, colorbar);
            }
            // set flashlight
            else if (message.contains(FLASHLIGHT_PATH)) {
                int flashlight = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                mainPresenter.setCameraFlashlight(espCamera, flashlight);
            }
        }
    }

    public void sendMessage(String message) {
        webSocketClient.send(message);
    }

    public boolean isWebSocketConnected() {
        return webSocketClient.isOpen();
    }

    public void close() {
        webSocketClient.close();
    }
}

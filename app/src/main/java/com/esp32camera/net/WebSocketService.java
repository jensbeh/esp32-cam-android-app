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
import static com.esp32camera.util.Constants.CAM_NOTIFICATION_PATH;
import static com.esp32camera.util.Constants.COLORBAR_PATH;
import static com.esp32camera.util.Constants.CONTRAST_PATH;
import static com.esp32camera.util.Constants.EXPOSURE_CTRL_STATE_PATH;
import static com.esp32camera.util.Constants.FLASHLIGHT_PATH;
import static com.esp32camera.util.Constants.FRAMESIZE_PATH;
import static com.esp32camera.util.Constants.GAINCEILING_PATH;
import static com.esp32camera.util.Constants.HMIRROR_PATH;
import static com.esp32camera.util.Constants.LENC_PATH;
import static com.esp32camera.util.Constants.MOTION_DETECTED_PATH;
import static com.esp32camera.util.Constants.QUALITY_PATH;
import static com.esp32camera.util.Constants.RAW_GMA_PATH;
import static com.esp32camera.util.Constants.SATURATION_PATH;
import static com.esp32camera.util.Constants.SPECIAL_EFFECT_PATH;
import static com.esp32camera.util.Constants.VFLIP_PATH;
import static com.esp32camera.util.Constants.WB_MODE_PATH;
import static com.esp32camera.util.Constants.WHITEBALANCE_STATE_PATH;
import static com.esp32camera.util.Constants.WPC_PATH;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.esp32camera.MainPresenter;
import com.esp32camera.model.EspCamera;

import java.nio.ByteBuffer;

public class WebSocketService implements WebSocketForegroundService.Callbacks {

    private Intent serviceIntent;
    private WebSocketForegroundService myService;

    private MainPresenter mainPresenter;
    private WebSocketServiceInterface webSocketServiceInterface;

    /**
     * constructor to reference the interface and set init objects and start the service and/or bind it
     */
    public WebSocketService(MainPresenter mainPresenter, WebSocketServiceInterface webSocketServiceInterface) {
        this.mainPresenter = mainPresenter;
        this.webSocketServiceInterface = webSocketServiceInterface;

        serviceIntent = new Intent(mainPresenter.getActivity(), WebSocketForegroundService.class);
        if (!isMyServiceRunning(WebSocketForegroundService.class)) {
            startService();
        } else {
            mainPresenter.getActivity().getApplication().bindService(serviceIntent, mConnection, 0); //Binding to the service!
        }
    }

    /**
     * ServiceConnection to connect to the service and bind it to an object and register this class to it to communicate
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        /**
         * method is called when the service connects -> happens with startService()-method
         */
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Toast.makeText(mainPresenter.getActivity(), "Service Connected", Toast.LENGTH_SHORT).show();
            // Here we bind the WebSocketForegroundService to this class to communicate with the service (this class -> service)
            WebSocketForegroundService.LocalBinder binder = (WebSocketForegroundService.LocalBinder) service;
            myService = binder.getService(); // Get the instance of WebSocketForegroundService
            myService.registerClient(getInstance()); // to register in WebSocketForegroundService as client for callbacks to communicate with this class (service -> this class)

            webSocketServiceInterface.OnServiceConnected();
        }

        /**
         * method is called when the service disconnects
         */
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Toast.makeText(mainPresenter.getActivity(), "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * method to bind and start the service
     */
    public void startService() {
        mainPresenter.getActivity().getApplication().startService(serviceIntent); //Starting the service
        mainPresenter.getActivity().getApplication().bindService(serviceIntent, mConnection, 0); //Binding to the service!
        Toast.makeText(mainPresenter.getActivity(), "Service started", Toast.LENGTH_SHORT).show();
    }

    /**
     * method to unbind and stop the service
     */
    public void stopService() {
        mainPresenter.getActivity().unbindService(mConnection);
        mainPresenter.getActivity().stopService(serviceIntent);
        Toast.makeText(mainPresenter.getActivity(), "Service stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * method to create/connect a new webSocket for this espCamera
     */
    public void createWebSocket(EspCamera espCamera, MainPresenter mainPresenter) {
        myService.createWebSocket(espCamera, mainPresenter, webSocketServiceInterface);
    }

    /**
     * method to handle incoming webSocket message strings like commands
     */
    @Override
    public void handleMessage(EspCamera espCamera, String message) {
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
        } else if (message.contains(CAM_NOTIFICATION_PATH)) {
            if (message.contains(MOTION_DETECTED_PATH)) {
                mainPresenter.notifyOnMotionDetected(espCamera);
            }
        }
    }

    /**
     * method to handle incoming webSocket message byteBuffer like picture data
     */
    @Override
    public void handleByteBuffer(EspCamera espCamera, ByteBuffer bytes) {
        mainPresenter.notifyOnMotionDetectedPictureData(espCamera, bytes.array());
    }

    /**
     * method to send a message with the webSocket of the espCamera
     */
    public void sendMessage(EspCamera espCamera, String message) {
        myService.sendMessage(espCamera, message);
    }

    /**
     * method to close the webSocket of the espCamera
     */
    public void closeWebSocket(EspCamera espCamera) {
        myService.closeWebSocket(espCamera);
    }

    /**
     * method to check if the WebSocketForegroundService is running or not
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mainPresenter.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * method checks if the webSocket of this espCamera is connected and active
     */
    public boolean isWebSocketConnected(EspCamera espCamera) {
        return myService.isWebSocketConnected(espCamera);
    }

    /**
     * method checks if a webSocket object of this espCamera is already existing
     */
    public boolean webSocketAlreadyExisting(EspCamera espCamera) {
        return myService.webSocketAlreadyExisting(espCamera);
    }

    /**
     * method to update the service with new/fresh objects after app restart
     */
    public void updateWebSocketForegroundService(EspCamera newEspCamera, MainPresenter mainPresenter) {
        myService.updateWebSocketForegroundService(newEspCamera, mainPresenter, this, webSocketServiceInterface);
    }

    public WebSocketService getInstance() {
        return this;
    }
}

package com.example.esp32camera.net;

import static com.example.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.example.esp32camera.util.Constants.CAM_CONTROL_PATH;
import static com.example.esp32camera.util.Constants.CONTRAST_PATH;
import static com.example.esp32camera.util.Constants.WEBSOCKETS_SERVER_URL;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.esp32camera.MainActivity;
import com.example.esp32camera.MainPresenter;
import com.example.esp32camera.camSettings.CamSettingsContract;
import com.example.esp32camera.camSettings.CamSettingsPresenter;
import com.example.esp32camera.model.EspCamera;

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
                    //espCamera.setBrightness(brightness);
                    camSettingsPresenter.setCameraBrightness(brightness);

                }
            }
            else if (message.contains(CONTRAST_PATH)) {
                int contrast = Integer.parseInt(message.substring(message.indexOf("=") + 1));
                if (camSettingsPresenter.getCameraContrast() != contrast) {
                    //espCamera.setContrast(contrast);
                    camSettingsPresenter.setCameraContrast(contrast);
                }
            }
        }
    }

    public void sendMessage(String message) {
        webSocketClient.send(message);
    }
}

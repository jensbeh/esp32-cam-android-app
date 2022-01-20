package com.esp32camera.net;

import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.UPDATE_CAMERA_PATH;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.esp32camera.MainPresenter;
import com.esp32camera.model.EspCamera;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class CustomWebSocketClient extends WebSocketClient {
    private WebSocketForegroundService.Callbacks webSocketService;
    private WebSocketForegroundService webSocketForegroundService;
    private EspCamera espCamera;
    private MainPresenter mainPresenter;
    private WebSocketServiceInterface webSocketServiceInterface;

    private boolean previousStateWasOpen;

    public CustomWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public void init(WebSocketForegroundService.Callbacks webSocketService, WebSocketForegroundService webSocketForegroundService, EspCamera espCamera, MainPresenter mainPresenter, WebSocketServiceInterface webSocketServiceInterface) {
        this.webSocketService = webSocketService;
        this.webSocketForegroundService = webSocketForegroundService;
        this.espCamera = espCamera;
        this.mainPresenter = mainPresenter;
        this.webSocketServiceInterface = webSocketServiceInterface;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        webSocketServiceInterface.OnConnectionOpened(espCamera, "WEBSOCKET OPENED");
        Log.i("WebSocket", "Opened");

        mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() + 1);

        this.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);

        webSocketForegroundService.updateNotification(mainPresenter);

        previousStateWasOpen = true;
    }

    @Override
    public void onMessage(String s) {
        final String message = s;
        webSocketService.handleMessage(espCamera, message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        webSocketService.handleByteBuffer(espCamera, bytes);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        webSocketServiceInterface.OnConnectionClosed(espCamera, "WEBSOCKET CLOSE");
        Log.i("WebSocket", "Closed " + reason);

        if (mainPresenter.getOpenedWebSocketCount() != 0) {
            mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() - 1);
        }

        // tries to reconnect the webSocket after 5 sec; if not working then there will be an onClose Error again with reconnect etc.
        mainPresenter.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("WebSocket", "Try to reconnect...");
                        getThisCustomWebSocketClient().reconnect();
                    }
                }, 5000);
            }
        });

        if (previousStateWasOpen) {
            webSocketForegroundService.updateNotification(mainPresenter);
        }

        previousStateWasOpen = false;
    }

    @Override
    public void onError(Exception e) {
        webSocketServiceInterface.OnConnectionFailed(espCamera, "WEBSOCKET ERROR");
        Log.i("WebSocket", "Error " + e.getMessage());
    }

    /**
     * This method is needed because when the app restarts it sets new instances of objects and otherwise the webSockets would operate with old objects
     */
    public void updateObjects(EspCamera newEspCamera, MainPresenter mainPresenter, WebSocketService webSocketService, WebSocketServiceInterface webSocketServiceInterface) {
        this.espCamera = newEspCamera;
        this.mainPresenter = mainPresenter;
        this.webSocketService = webSocketService;
        this.webSocketServiceInterface = webSocketServiceInterface;

        // send the update command when webSocket is open(in case the esp is offline)
        if (this.isOpen()) {
            this.send(CAM_CONTROLS_PATH + UPDATE_CAMERA_PATH);
        }
    }

    private CustomWebSocketClient getThisCustomWebSocketClient() {
        return this;
    }
}

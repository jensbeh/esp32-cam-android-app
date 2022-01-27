package com.esp32camera.net;

import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.UPDATE_CAMERA_PATH;

import android.os.Build;
import android.os.Handler;

import com.esp32camera.MainPresenter;
import com.esp32camera.model.EspCamera;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

// WebSocketLibrary https://github.com/TooTallNate/Java-WebSocket
// WebSocket basics - https://stackoverflow.com/questions/26452903/javax-websocket-client-simple-example

public class CustomWebSocketClient extends WebSocketClient {
    private WebSocketForegroundService.Callbacks webSocketService;
    private WebSocketForegroundService webSocketForegroundService;
    private EspCamera espCamera;
    private MainPresenter mainPresenter;
    private WebSocketServiceInterface webSocketServiceInterface;

    private boolean previousStateWasOpen;

    /**
     * @param serverUri uri of the esp camera to connect to
     */
    public CustomWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    /**
     * method to save all important objects
     */
    public void init(WebSocketForegroundService.Callbacks webSocketService, WebSocketForegroundService webSocketForegroundService, EspCamera espCamera, MainPresenter mainPresenter, WebSocketServiceInterface webSocketServiceInterface) {
        this.webSocketService = webSocketService;
        this.webSocketForegroundService = webSocketForegroundService;
        this.espCamera = espCamera;
        this.mainPresenter = mainPresenter;
        this.webSocketServiceInterface = webSocketServiceInterface;
    }

    /**
     * method is called when webSocketConnection is opened
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        webSocketServiceInterface.OnConnectionOpened(espCamera, "WEBSOCKET OPENED");

        // add one more to webSocketCount
        mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() + 1);

        this.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);

        // to update the notification with e.g. "1 of 2 cameras are online!"
        webSocketForegroundService.updateNotification(mainPresenter);

        previousStateWasOpen = true;
    }

    /**
     * method is called when webSocket receives a string message from esp camera
     */
    @Override
    public void onMessage(String s) {
        final String message = s;
        webSocketService.handleMessage(espCamera, message);
    }

    /**
     * method is called when webSocket receives a byteBuffer message from esp camera
     */
    @Override
    public void onMessage(ByteBuffer bytes) {
        webSocketService.handleByteBuffer(espCamera, bytes);
    }

    /**
     * method is called when webSocketConnection is closed
     * runnable to reconnect
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        webSocketServiceInterface.OnConnectionClosed(espCamera, "WEBSOCKET CLOSE");

        // removes one from webSocketCount
        if (mainPresenter.getOpenedWebSocketCount() != 0) {
            mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() - 1);
        }

        // tries to reconnect the webSocket after 5 sec; if not working then there will be an onClose Error again with reconnect etc.
        mainPresenter.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getThisCustomWebSocketClient().reconnect();
                    }
                }, 5000);
            }
        });

        if (previousStateWasOpen) {
            // to update the notification with e.g. "1 of 2 cameras are online!"
            webSocketForegroundService.updateNotification(mainPresenter);
        }

        previousStateWasOpen = false;
    }

    @Override
    public void onError(Exception e) {
        webSocketServiceInterface.OnConnectionFailed(espCamera, "WEBSOCKET ERROR");
    }

    /**
     * This method is needed because when the app restarts it sets new instances of objects and otherwise the webSockets would operate with old objects
     */
    public void updateObjects(EspCamera newEspCamera, MainPresenter mainPresenter, WebSocketService webSocketService, WebSocketServiceInterface webSocketServiceInterface) {
        this.espCamera = newEspCamera;
        this.mainPresenter = mainPresenter;
        this.webSocketService = webSocketService;
        this.webSocketServiceInterface = webSocketServiceInterface;

        // send the update command when webSocket is open(in case the esp is online)
        if (this.isOpen()) {
            this.send(CAM_CONTROLS_PATH + UPDATE_CAMERA_PATH);
        }
    }

    private CustomWebSocketClient getThisCustomWebSocketClient() {
        return this;
    }
}

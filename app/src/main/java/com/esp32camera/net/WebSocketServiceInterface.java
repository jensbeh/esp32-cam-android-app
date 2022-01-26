package com.esp32camera.net;

import com.esp32camera.model.EspCamera;

public interface WebSocketServiceInterface {
    void OnConnectionOpened(EspCamera espCamera, String status);
    void OnConnectionClosed(EspCamera espCamera, String status);
    void OnConnectionFailed(EspCamera espCamera, String status);

    void OnServiceConnected();

    interface OnRunningInterface {
        void onServiceRunning();
    }
}

package com.esp32camera.net;

public interface WebSocketServiceInterface {
    void OnConnectionOpened(String status);
    void OnConnectionClosed(String status);
    void OnConnectionFailed(String status);
}

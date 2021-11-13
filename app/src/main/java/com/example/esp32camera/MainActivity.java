package com.example.esp32camera;

import static com.example.esp32camera.util.Constants.STREAM_PATH;
import static com.example.esp32camera.util.Constants.WEBSERVER_URL;
import static com.example.esp32camera.util.Constants.WEBSOCKETS_SERVER_URL;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private WebSocketClient webSocketClient;
    private WebView webview;
    private EditText et_webSocketInput;
    private Button button_send;
    private Slider sliderBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = findViewById(R.id.webViewStream);
        et_webSocketInput = findViewById(R.id.et_webSocketInput);
        button_send = findViewById(R.id.button_send);
        sliderBrightness = findViewById(R.id.sliderBrightness);

        setupCameraStream();

        connectWebSocket();

        button_send.setOnClickListener(v -> {
            if (!et_webSocketInput.getText().toString().equals("")) {
                /*
                Brightness -2 to 2: camControls/brightness=1
                 */
                String message = et_webSocketInput.getText().toString();
                webSocketClient.send(message);
                et_webSocketInput.setText("");
            }
        });

        // set brightness
        sliderBrightness.addOnChangeListener((slider, value, fromUser) -> {
            int intValue = (int) value;
            webSocketClient.send("camControls/brightness=" + intValue);
        });
    }

    private void setupCameraStream() {
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        // because desktop mode is needed:
        // https://stackoverflow.com/questions/67849381/esp32-httpd-header-fields-are-too-long-for-the-server-to-interpret
        String newUserAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webview.getSettings().setUserAgentString(newUserAgent);
        webview.loadUrl(WEBSERVER_URL + STREAM_PATH); // start webView
    }

    private void connectWebSocket() {

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
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
}
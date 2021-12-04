package com.esp32camera.model;

import static com.esp32camera.util.Constants.STREAM_PATH;
import static com.esp32camera.util.Constants.WEBSERVER_HTTP;
import static com.esp32camera.util.Constants.WEBSERVER_PORT;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;

public class CameraCard {
    private final View view;
    private MainPresenter mainPresenter;
    private EspCamera espCamera;
    private WebView webViewStream;
    private Button button_camSettings;
    private TextView tv_camera_name;
    private ConstraintLayout streamLayout;
    private LinearLayout errorLayout;
    private Button errorReloadButton;
    private CardView cardView;
    private LinearLayout loadingLayout;

    public CameraCard(MainPresenter mainPresenter, EspCamera espCamera) {
        this.view = LayoutInflater.from(mainPresenter.getActivity()).inflate(R.layout.raw_camera_card_home, null);
        this.mainPresenter = mainPresenter;
        this.espCamera = espCamera;

        cardView = view.findViewById(R.id.cardView);
        webViewStream = view.findViewById(R.id.webViewStream);
        button_camSettings = view.findViewById(R.id.button_camSettings);

        tv_camera_name = view.findViewById(R.id.tv_camera_name);
        tv_camera_name.setText(espCamera.getName());

        streamLayout = view.findViewById(R.id.streamLayout);
        errorLayout = view.findViewById(R.id.errorLayout);
        errorReloadButton = view.findViewById(R.id.errorReloadButton);

        loadingLayout = view.findViewById(R.id.loadingLayout);
        errorReloadButton = view.findViewById(R.id.errorReloadButton);

        setupOnListener();

        // create WebView and connect to WebServer
        setupCameraStreamWebView();
    }

    private void setupOnListener() {
        button_camSettings.setOnClickListener(v -> {
            mainPresenter.navigateToCamSettingsFragment(espCamera);
        });
    }

    public void setCameraName(String newName) {
        tv_camera_name.setText(newName);
    }

    private void setupCameraStreamWebView() {
        loadingLayout.setVisibility(View.VISIBLE);

        webViewStream.getSettings().setLoadWithOverviewMode(true);
        webViewStream.getSettings().setUseWideViewPort(true);

        webViewStream.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // when page is loading
                Log.i("HomeFragment", "onPageStarted");

                streamLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("HomeFragment", "onPageFinished");

                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i("HomeFragment", "onReceivedError");

                streamLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        webViewStream.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    streamLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        errorReloadButton.setOnClickListener(v -> {
            loadingLayout.setVisibility(View.VISIBLE);

            webViewStream.reload();
        });

        String html = "<html><body><img src=\"" + WEBSERVER_HTTP + espCamera.getIpAddress() + WEBSERVER_PORT + STREAM_PATH + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
        webViewStream.loadData(html, "text/html", null);
    }

    public View getView() {
        return view;
    }

    /**
     * This method will be called when webSocketConnection get lost / is closed
     * ErrorLayout will be shown if it isn't already there, because in most cases the stream is also killed
     */
    public void onWebSocketConnectionClosed() {
        mainPresenter.getActivity().runOnUiThread(() -> {
            if (errorLayout.getVisibility() != View.VISIBLE) {
                Log.i("CameraCard", "Show ERROR Layout on WebSocket Closed...");

                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void stop() {
        // kill webView
        webViewStream.destroy();
    }
}

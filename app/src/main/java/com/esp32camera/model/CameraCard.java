package com.esp32camera.model;

import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.FLASHLIGHT_PATH;
import static com.esp32camera.util.Constants.STREAM_PATH;
import static com.esp32camera.util.Constants.WEBSERVER_HTTP;
import static com.esp32camera.util.Constants.WEBSERVER_PORT;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.util.BitmapToVideoEncoder;
import com.esp32camera.util.CreateFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraCard {
    private final View view;
    private final ImageView iv_recording_circle;
    private final ImageView iv_capture_white;
    private MainPresenter mainPresenter;
    private EspCamera espCamera;
    private WebView webViewStream;
    private ImageButton button_camSettings;
    private ToggleButton button_flashlight;
    private ToggleButton button_recordVideo;
    private final ImageButton button_recordPicture;
    private TextView tv_camera_name;
    private ConstraintLayout streamLayout;
    private LinearLayout errorLayout;
    private Button errorReloadButton;
    private CardView cardView;
    private LinearLayout loadingLayout;
    private final LinearLayout ll_actionButtonCam;

    private boolean record = false;

    public CameraCard(MainPresenter mainPresenter, EspCamera espCamera) {
        this.view = LayoutInflater.from(mainPresenter.getActivity()).inflate(R.layout.raw_camera_card_home, null);
        this.mainPresenter = mainPresenter;
        this.espCamera = espCamera;

        cardView = view.findViewById(R.id.cardView);
        webViewStream = view.findViewById(R.id.webViewStream);
        button_camSettings = view.findViewById(R.id.button_camSettings);
        button_flashlight = view.findViewById(R.id.button_flashlight);
        button_flashlight.setChecked(espCamera.flashlightState == 1);

        button_recordVideo = view.findViewById(R.id.button_recordVideo);
        button_recordVideo.setChecked(false);

        button_recordPicture = view.findViewById(R.id.button_recordPicture);

        tv_camera_name = view.findViewById(R.id.tv_camera_name);
        tv_camera_name.setText(espCamera.getName());

        streamLayout = view.findViewById(R.id.streamLayout);
        errorLayout = view.findViewById(R.id.errorLayout);
        errorReloadButton = view.findViewById(R.id.errorReloadButton);

        loadingLayout = view.findViewById(R.id.loadingLayout);
        errorReloadButton = view.findViewById(R.id.errorReloadButton);

        ll_actionButtonCam = view.findViewById(R.id.ll_actionButtonCam);

        iv_recording_circle = view.findViewById(R.id.iv_recording_circle);
        iv_recording_circle.setVisibility(View.GONE);

        iv_capture_white = view.findViewById(R.id.iv_capture_white);

        setupOnListener();

        // create WebView and connect to WebServer
        setupCameraStreamWebView();
    }

    private void setupOnListener() {
        button_camSettings.setOnClickListener(v -> {
            // navigate to SettingsFragment
            mainPresenter.navigateToCamSettingsFragment(espCamera);
        });

        button_flashlight.setOnClickListener(v -> {
            // toggle flashlight with webSocketAction
            if (button_flashlight.isChecked()) {
                // make off
                mainPresenter.sendWebSocketMessage(espCamera, CAM_CONTROLS_PATH + FLASHLIGHT_PATH + 1);
            } else {
                // make on
                mainPresenter.sendWebSocketMessage(espCamera, CAM_CONTROLS_PATH + FLASHLIGHT_PATH + 0);
            }
        });

        button_recordVideo.setOnClickListener(v -> {
            // toggle recordVideo
            if (record) {
                iv_recording_circle.setVisibility(View.GONE);
                iv_recording_circle.setAnimation(null);
                // stop recording
                record = false;
            } else {
                iv_recording_circle.setVisibility(View.VISIBLE);
                // set pulsing animation for record circle
                Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(800);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                iv_recording_circle.startAnimation(anim);

                // start recording
                record = true;
                new BackgroundVideo().execute();
            }
        });
        button_recordPicture.setOnClickListener(v -> {
            // make picture
            Animation anim = new AlphaAnimation(0.8f, 0.0f);
            anim.setDuration(500);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iv_capture_white.setAnimation(null);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            iv_capture_white.startAnimation(anim);

            // start picture capture
            new BackgroundPng().execute();
        });
    }

    /**
     * AsyncTask class to record a single picture from webView to file in DCIM
     */
    class BackgroundPng extends AsyncTask<Void, Void, Bitmap> {
        // gets webView bitmap in background thread
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = Bitmap.createBitmap(webViewStream.getWidth(), webViewStream.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webViewStream.draw(canvas);
            return bitmap;
        }

        // saves bitmap after backgroundThread is finished
        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {
            String jpegFilePath = CreateFile.createJpegFile().getAbsolutePath();

            try {
                FileOutputStream fOut = new FileOutputStream(jpegFilePath);
                result.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                Toast.makeText(mainPresenter.getActivity(), "Made a picture!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * AsyncTask class to record a video picture from webView to file in DCIM
     */
    class BackgroundVideo extends AsyncTask<Void, Void, List<Bitmap>> {
        // gets webView bitmaps in background thread while record
        @Override
        protected List<Bitmap> doInBackground(Void... params) {
            List<Bitmap> bitmapList = new ArrayList<>();
            while (record) {
                Bitmap bitmap = Bitmap.createBitmap(webViewStream.getWidth(), webViewStream.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                webViewStream.draw(canvas);

                bitmapList.add(bitmap);
            }
            // when stopped recording
            mainPresenter.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainPresenter.getActivity(), "Recorded it! Please wait for saving File!", Toast.LENGTH_LONG).show();
                }
            });
            return bitmapList;
        }

        // create video from bitmapList with BitmapToVideoEncoder after backgroundThread is finished
        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(List<Bitmap> result) {
            File mp4File = CreateFile.createMp4File();

            BitmapToVideoEncoder bitmapToVideoEncoder = new BitmapToVideoEncoder(new BitmapToVideoEncoder.IBitmapToVideoEncoderCallback() {
                @Override
                public void onEncodingComplete(File outputFile) {
                    mainPresenter.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainPresenter.getActivity(), "Saved the video!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            bitmapToVideoEncoder.startEncoding(webViewStream.getWidth(), webViewStream.getHeight(), mp4File);
            for (Bitmap bitmap : result) {
                bitmapToVideoEncoder.queueFrame(bitmap);
            }
            bitmapToVideoEncoder.stopEncoding();

        }

    }

    public void setCameraName(String newName) {
        tv_camera_name.setText(newName);
    }

    public void setCameraFlashlight(int flashlightState) {
        button_flashlight.setChecked(flashlightState == 1);
    }

    /**
     * method to setup the webView with camera stream
     * complex because opportunity to reload the webView on Connection error with custom error Label
     */
    private void setupCameraStreamWebView() {
        loadingLayout.setVisibility(View.VISIBLE);

        webViewStream.getSettings().setLoadWithOverviewMode(true);
        webViewStream.getSettings().setUseWideViewPort(true);

        webViewStream.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // when page is loading
                // hide errorLayout
                streamLayout.setVisibility(View.VISIBLE);
                ll_actionButtonCam.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // when page is loaded
                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // when page could not load -> error
                // show errorLayout
                streamLayout.setVisibility(View.GONE);
                ll_actionButtonCam.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        // webChromeClient to get the progressChange
        // to make stream and actions visible when page is fully loaded
        webViewStream.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    streamLayout.setVisibility(View.VISIBLE);
                    ll_actionButtonCam.setVisibility(View.VISIBLE);
                }
            }
        });

        // button to reload the webView and show loadingLayout
        errorReloadButton.setOnClickListener(v -> {
            loadingLayout.setVisibility(View.VISIBLE);

            webViewStream.reload();
        });

        // webViewStream url
        String html = "<html><body><img src=\"" + WEBSERVER_HTTP + espCamera.getIpAddress() + WEBSERVER_PORT + STREAM_PATH + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
        // load webViewContent
        webViewStream.loadData(html, "text/html", null);
    }

    public View getView() {
        return view;
    }

    /**
     * method to enable flashlight when webSocket is available
     */
    public void onWebSocketConnectionOpened() {
        mainPresenter.getActivity().runOnUiThread(() -> {
            button_flashlight.setEnabled(true);
        });
    }

    /**
     * This method will be called when webSocketConnection get lost / is closed
     * ErrorLayout will be shown if it isn't already there, because in most cases the stream is also killed
     */
    public void onWebSocketConnectionClosed() {
        mainPresenter.getActivity().runOnUiThread(() -> {
            button_flashlight.setEnabled(false);

            if (errorLayout.getVisibility() != View.VISIBLE) {
                // show error layout
                errorLayout.setVisibility(View.VISIBLE);
                ll_actionButtonCam.setVisibility(View.GONE);
            }
        });
    }

    public void stop() {
        // kill webView
        webViewStream.destroy();
    }
}

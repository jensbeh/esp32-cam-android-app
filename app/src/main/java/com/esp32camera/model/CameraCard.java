package com.esp32camera.model;

import static android.content.ContentValues.TAG;
import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.FLASHLIGHT_PATH;
import static com.esp32camera.util.Constants.STREAM_PATH;
import static com.esp32camera.util.Constants.WEBSERVER_HTTP;
import static com.esp32camera.util.Constants.WEBSERVER_PORT;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            mainPresenter.navigateToCamSettingsFragment(espCamera);
        });

        button_flashlight.setOnClickListener(v -> {
            if (button_flashlight.isChecked()) {
                // make off
                mainPresenter.sendWebSocketMessage(espCamera, CAM_CONTROLS_PATH + FLASHLIGHT_PATH + 1);
            } else {
                // make on
                mainPresenter.sendWebSocketMessage(espCamera, CAM_CONTROLS_PATH + FLASHLIGHT_PATH + 0);
            }
        });

        button_recordVideo.setOnClickListener(v -> {
            if (record) {
                iv_recording_circle.setVisibility(View.GONE);
                iv_recording_circle.setAnimation(null);
                record = false;
            } else {
                iv_recording_circle.setVisibility(View.VISIBLE);
                // set pulsing animation for record circle
                Animation anim = new AlphaAnimation(0.3f, 1.0f);
                anim.setDuration(800);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                iv_recording_circle.startAnimation(anim);

                record = true;
                new BackgroundVideo().execute();
            }
        });
        button_recordPicture.setOnClickListener(v -> {
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

            new BackgroundPng().execute();
        });
    }

    class BackgroundPng extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = Bitmap.createBitmap(webViewStream.getWidth(), webViewStream.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            webViewStream.draw(canvas);
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {
            String jpegFilePath = createJpegFile().getAbsolutePath();

            try {
                FileOutputStream fOut = new FileOutputStream(jpegFilePath);
                result.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                Toast.makeText(mainPresenter.getActivity(), "READY", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    class BackgroundVideo extends AsyncTask<Void, Void, List<Bitmap>> {
        @Override
        protected List<Bitmap> doInBackground(Void... params) {
            List<Bitmap> bitmapList = new ArrayList<>();
            while (record) {
                Bitmap bitmap = Bitmap.createBitmap(webViewStream.getWidth(), webViewStream.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                webViewStream.draw(canvas);

                bitmapList.add(bitmap);
            }
            mainPresenter.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainPresenter.getActivity(), "CAPTURE READY!", Toast.LENGTH_LONG).show();
                }
            });
            return bitmapList;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(List<Bitmap> result) {
            File mp4File = createMp4File();

            BitmapToVideoEncoder bitmapToVideoEncoder = new BitmapToVideoEncoder(new BitmapToVideoEncoder.IBitmapToVideoEncoderCallback() {
                @Override
                public void onEncodingComplete(File outputFile) {
                    mainPresenter.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mainPresenter.getActivity(), "Encoding complete!", Toast.LENGTH_LONG).show();
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

    private File createJpegFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File image = new File(dir, imageFileName + ".jpg");
        Log.d(TAG, "file path is " + image.getAbsolutePath());
        return image;
    }

    private File createMp4File() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MP4_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(storageDir.getAbsolutePath() + "/EspCamera/");
        dir.mkdirs();
        File video = new File(dir, imageFileName + ".mp4");
        Log.d(TAG, "file path is " + video.getAbsolutePath());
        return video;
    }

    public void setCameraName(String newName) {
        tv_camera_name.setText(newName);
    }

    public void setCameraFlashlight(int flashlightState) {
        button_flashlight.setChecked(flashlightState == 1);
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
                ll_actionButtonCam.setVisibility(View.GONE);
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
                ll_actionButtonCam.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

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
                Log.i("CameraCard", "Show ERROR Layout on WebSocket Closed...");

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

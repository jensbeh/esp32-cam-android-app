package com.esp32camera.home;

import static com.esp32camera.util.Constants.STREAM_PATH;
import static com.esp32camera.util.Constants.WEBSERVER_URL;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.camSettings.CamSettingsPresenter;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private View view;

    private WebView webViewStream;
    private Button button_camSettings;
    private final MainPresenter mainPresenter;
    private final HomePresenter homePresenter;
    private CamSettingsPresenter camSettingsPresenter;
    private TextView tv_camera_name;
    private int webViewWidth;
    private int webViewHeight;
    private ConstraintLayout streamLayout;
    private LinearLayout errorLayout;
    private Button errorReloadButton;
    private CardView cardView;
    private LinearLayout loadingLayout;

    public HomeFragment(MainPresenter mainPresenter, HomePresenter homePresenter, CamSettingsPresenter camSettingsPresenter) {
        this.mainPresenter = mainPresenter;
        this.homePresenter = homePresenter;
        this.camSettingsPresenter = camSettingsPresenter;

        this.homePresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    /**
     * Second method where the view is ready to use
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView = view.findViewById(R.id.cardView);
        webViewStream = view.findViewById(R.id.webViewStream);
        button_camSettings = view.findViewById(R.id.button_camSettings);

        tv_camera_name = view.findViewById(R.id.tv_camera_name);

        tv_camera_name.setText(camSettingsPresenter.getCameraName());

        streamLayout = view.findViewById(R.id.streamLayout);
        errorLayout = view.findViewById(R.id.errorLayout);
        errorReloadButton = view.findViewById(R.id.errorReloadButton);

        loadingLayout = view.findViewById(R.id.loadingLayout);
        errorReloadButton = view.findViewById(R.id.errorReloadButton);

        setupOnListener();

        setupCameraStreamWebView();
    }

    private void setupOnListener() {

        button_camSettings.setOnClickListener(v -> {
            mainPresenter.navigateToCamSettingsFragment();
        });
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
                System.err.println("onPageStarted");
                streamLayout.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.err.println("onPageFinished");
                loadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                System.err.println("onReceivedError");
                streamLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        webViewStream.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //System.err.println("44444444444");
                if (newProgress == 100) {
                    streamLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        errorReloadButton.setOnClickListener(v -> {
            loadingLayout.setVisibility(View.VISIBLE);

            webViewStream.reload();
        });

//        Timer repeatTask = new Timer();
//        repeatTask.scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        if (!pd.isShowing() && errorLayout.getVisibility() != View.VISIBLE) {
////                            System.err.println("RELOAD");
////                            webViewStream.reload();
////                        }
//                    }
//                });
//            }
//        }, 0, 5000);

        String html = "<html><body><img src=\"" + WEBSERVER_URL + STREAM_PATH + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
        webViewStream.loadData(html, "text/html", null);
    }

    @Override
    public void onStop() {
        super.onStop();

        // kill webView to kill the stream
        webViewStream.destroy();
    }
}
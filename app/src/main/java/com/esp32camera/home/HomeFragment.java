package com.esp32camera.home;

import static com.esp32camera.util.Constants.STREAM_PATH;
import static com.esp32camera.util.Constants.WEBSERVER_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;

public class HomeFragment extends Fragment {

    private View view;

    private WebView webViewStream;
    private EditText et_webSocketInput;
    private Button button_send;
    private Button button_camSettings;
    private final MainPresenter mainPresenter;
    private final HomePresenter homePresenter;

    public HomeFragment(MainPresenter mainPresenter, HomePresenter homePresenter) {
        this.mainPresenter = mainPresenter;
        this.homePresenter = homePresenter;

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

        webViewStream = view.findViewById(R.id.webViewStream);
        et_webSocketInput = view.findViewById(R.id.et_webSocketInput);
        button_send = view.findViewById(R.id.button_send);
        button_camSettings = view.findViewById(R.id.button_camSettings);

        setupOnListener();

        setupCameraStreamWebView();
    }

    private void setupOnListener() {
        button_send.setOnClickListener(v -> {
            if (!et_webSocketInput.getText().toString().equals("")) {
                /*
                Brightness -2 to 2: camControls/brightness=1
                 */
                String message = et_webSocketInput.getText().toString();
                mainPresenter.sendWebSocketMessage(message);

                et_webSocketInput.setText("");
            }
        });

        button_camSettings.setOnClickListener(v -> {
            // create bottomSheet for camera settings with all actions
            //BottomSheetCamSettings bottomSheetCamSettings = new BottomSheetCamSettings(this, R.style.BottomSheetDialogTheme, webSocketClient, espCamera);
            //bottomSheetCamSettings.show();

            mainPresenter.navigateToCamSettingsFragment();
        });
    }

    private void setupCameraStreamWebView() {
        webViewStream.getSettings().setLoadWithOverviewMode(true);
        webViewStream.getSettings().setUseWideViewPort(true);
        // desktop mode is needed
        String newUserAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webViewStream.getSettings().setUserAgentString(newUserAgent);
        webViewStream.loadUrl(WEBSERVER_URL + STREAM_PATH); // start webView
    }

    @Override
    public void onStop() {
        super.onStop();

        // kill webView to kill the stream
        webViewStream.destroy();
    }
}
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

    private WebView webview;
    private EditText et_webSocketInput;
    private Button button_send;
    private Button button_camSettings;
    private MainPresenter mainPresenter;

    public HomeFragment(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
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

        webview = view.findViewById(R.id.webViewStream);
        et_webSocketInput = view.findViewById(R.id.et_webSocketInput);
        button_send = view.findViewById(R.id.button_send);
        button_camSettings = view.findViewById(R.id.button_camSettings);

        setupCameraStream();

        button_send.setOnClickListener(v -> {
            if (!et_webSocketInput.getText().toString().equals("")) {
                /*
                Brightness -2 to 2: camControls/brightness=1
                 */
                String message = et_webSocketInput.getText().toString();
                //((MainActivity) requireActivity()).getWebSocketService().sendMessage(message);
                et_webSocketInput.setText("");
            }
        });

        button_camSettings.setOnClickListener(v -> {
            // create bottomSheet for camera settings with all actions
            //BottomSheetCamSettings bottomSheetCamSettings = new BottomSheetCamSettings(this, R.style.BottomSheetDialogTheme, webSocketClient, espCamera);
            //bottomSheetCamSettings.show();

            mainPresenter.navigateToSettingsFragment();

            //navController.navigate(R.id.navigateToSettingsFragment, new Bundle());
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

    @Override
    public void onStop() {
        super.onStop();

        // kill webView to kill the stream
        webview.destroy();
    }
}
package com.example.esp32camera.bottomSheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.esp32camera.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import org.java_websocket.client.WebSocketClient;

public class BottomSheetCamSettings extends BottomSheetDialog {
    private final View bottomSheetView;
    private final Context context;

    private final Slider sliderBrightness;

    private final WebSocketClient webSocketClient;

    public BottomSheetCamSettings(@NonNull Context context, int theme, WebSocketClient webSocketClient) {
        super(context, theme);

        this.context = context;
        this.bottomSheetView = LayoutInflater.from(context).inflate(R.layout.sheet_cam_settings, findViewById(R.id.bottomSheetContainer_createChannel));
        this.webSocketClient = webSocketClient;

        sliderBrightness = bottomSheetView.findViewById(R.id.sliderBrightness);

        setupOnListener();

        this.setContentView(bottomSheetView);
    }

    private void setupOnListener() {
        // set brightness
        sliderBrightness.addOnChangeListener((slider, value, fromUser) -> {
            int intValue = (int) value;
            webSocketClient.send("camControls/brightness=" + intValue);
        });
    }

    private void closeBottomSheet() {
        this.dismiss();
    }
}

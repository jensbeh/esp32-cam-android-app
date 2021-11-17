package com.example.esp32camera.bottomSheets;

import static com.example.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.example.esp32camera.util.Constants.CAM_CONTROL_PATH;
import static com.example.esp32camera.util.Constants.CONTRAST_PATH;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.esp32camera.R;
import com.example.esp32camera.model.EspCamera;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import org.java_websocket.client.WebSocketClient;

public class BottomSheetCamSettings extends BottomSheetDialog {
    private final View bottomSheetView;
    private final Context context;

    private final WebSocketClient webSocketClient;
    private final EspCamera espCamera;

    public BottomSheetCamSettings(@NonNull Context context, int theme, WebSocketClient webSocketClient, EspCamera espCamera) {
        super(context, theme);

        this.context = context;
        this.bottomSheetView = LayoutInflater.from(context).inflate(R.layout.sheet_cam_settings, findViewById(R.id.bottomSheetContainer_createChannel));
        this.webSocketClient = webSocketClient;
        this.espCamera = espCamera;



        this.setContentView(bottomSheetView);
    }



    private void closeBottomSheet() {
        this.dismiss();
    }
}

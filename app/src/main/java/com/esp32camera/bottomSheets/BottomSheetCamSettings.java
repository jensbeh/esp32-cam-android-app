package com.esp32camera.bottomSheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.esp32camera.R;
import com.esp32camera.model.EspCamera;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.java_websocket.client.WebSocketClient;

public class BottomSheetCamSettings extends BottomSheetDialog {
    private final View bottomSheetView;
    private final Context context;

    private final WebSocketClient webSocketClient;
    private final EspCamera espCamera;

    public BottomSheetCamSettings(@NonNull Context context, int theme, WebSocketClient webSocketClient, EspCamera espCamera) {
        super(context, theme);

        this.context = context;
        this.bottomSheetView = LayoutInflater.from(context).inflate(R.layout.sheet_cam_settings, findViewById(R.id.bottomSheetContainer_camSettings));
        this.webSocketClient = webSocketClient;
        this.espCamera = espCamera;


        this.setContentView(bottomSheetView);
    }


    private void closeBottomSheet() {
        this.dismiss();
    }
}

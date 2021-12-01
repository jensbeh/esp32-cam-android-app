package com.esp32camera.bottomSheets;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.adapter.AddEspCameraRecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class BottomSheetAddEspCamera extends BottomSheetDialog {
    private final Context context;
    private final View bottomSheetView;
    private MainPresenter mainPresenter;
    private RecyclerView rv_add_espCameras;
    private LinearLayout loadingLayout;

    private List<String> espCamerasIp;

    public BottomSheetAddEspCamera(@NonNull Context context, int theme, MainPresenter mainPresenter) {
        super(context, theme);

        this.context = context;
        this.bottomSheetView = LayoutInflater.from(context).inflate(R.layout.sheet_add_espcamera, findViewById(R.id.bottomSheetContainer_addEspCamera));
        this.mainPresenter = mainPresenter;

        espCamerasIp = new ArrayList<>();

        loadingLayout = (LinearLayout) bottomSheetView.findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);


        loadAllEspCameras(new ReadyCallback() {
            @Override
            public void onReady() {
                mainPresenter.getActivity().runOnUiThread(() -> {
                    loadingLayout.setVisibility(View.GONE);
                    rv_add_espCameras = (RecyclerView) bottomSheetView.findViewById(R.id.rv_add_espCameras);
                    setupAddEspCamerasRecyclerView();
                });
            }
        });

        this.setContentView(bottomSheetView);
    }

    private interface ReadyCallback {
        void onReady();
    }

    /**
     * loads all esp-cameras in an asyncTask
     *
     * @param readyCallback to setup RV of cameras and set loadingScreen to Gone
     */
    private void loadAllEspCameras(ReadyCallback readyCallback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int timeout = 100;
                for (int i = 1; i < 50; i++) {
                    String host = "192.168.188" + "." + i;
                    try {
                        if (InetAddress.getByName(host).isReachable(timeout)) {
                            InetAddress addr = InetAddress.getByName(host);
                            String hostName = addr.getHostName();
                            if (hostName.contains("ESP-Camera")) {
                                System.out.println(host + "isreachable");
                                System.out.println(hostName);
                                espCamerasIp.add(host);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                readyCallback.onReady();
            }
        });
    }

    /**
     * shows all available esp-cameras and setup handler
     */
    private void setupAddEspCamerasRecyclerView() {
        rv_add_espCameras.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        AddEspCameraRecyclerViewAdapter addEspCameraRecyclerViewAdapter = new AddEspCameraRecyclerViewAdapter(context, mainPresenter, espCamerasIp);

        rv_add_espCameras.setLayoutManager(layoutManager);
        rv_add_espCameras.setAdapter(addEspCameraRecyclerViewAdapter);

        addEspCameraRecyclerViewAdapter.setOnItemClickListener(new AddEspCameraRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String ipAddress) {
                if (ipAddress != null) {
                    Toast.makeText(context, "short", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, String ipAddress) {

                if (ipAddress != null) {
                    Toast.makeText(context, "long", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void closeBottomSheet() {
        this.dismiss();
    }
}

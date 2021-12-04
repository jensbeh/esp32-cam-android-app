package com.esp32camera.bottomSheets;

import static android.content.Context.WIFI_SERVICE;

import static java.lang.Math.round;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private final ProgressBar progressBarLoading;
    private final TextView tv_searching;
    private MainPresenter mainPresenter;
    private RecyclerView rv_add_espCameras;
    private ConstraintLayout loadingLayout;

    private List<String> espCamerasIp;

    public BottomSheetAddEspCamera(@NonNull Context context, int theme, MainPresenter mainPresenter) {
        super(context, theme);

        this.context = context;
        this.bottomSheetView = LayoutInflater.from(context).inflate(R.layout.sheet_add_espcamera, findViewById(R.id.bottomSheetContainer_addEspCamera));
        this.setContentView(bottomSheetView);
        this.mainPresenter = mainPresenter;

        espCamerasIp = new ArrayList<>();

        loadingLayout = (ConstraintLayout) bottomSheetView.findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);
        progressBarLoading = (ProgressBar) bottomSheetView.findViewById(R.id.progressBarLoading);
        progressBarLoading.setProgress(0);
        tv_searching = (TextView) bottomSheetView.findViewById(R.id.tv_searching);

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
        Animation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(100);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_searching.startAnimation(anim);

        AsyncTask.execute(() -> {
            WifiManager wifiMgr = (WifiManager) mainPresenter.getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);

            String currentSubIpAddress = ipAddress.substring(0, ipAddress.lastIndexOf("."));
            int timeout = 100;
            int ipAddressMaxCount = 255;
            for (int i = 1; i < ipAddressMaxCount; i++) {
                String host = currentSubIpAddress + "." + i;
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)) {
                        InetAddress addr = InetAddress.getByName(host);
                        String hostName = addr.getHostName();
                        if (hostName.contains("ESP-Camera") && !mainPresenter.ifCameraExisting(host)) {
                            System.out.println(host + "isreachable");
                            System.out.println(hostName);
                            espCamerasIp.add(host);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressBarLoading.setProgress((int)(round(i * (100.0 / ipAddressMaxCount))));
            }
            readyCallback.onReady();
        });
    }

    /**
     * shows all available esp-cameras and setup handler
     */
    private void setupAddEspCamerasRecyclerView() {
        rv_add_espCameras.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        AddEspCameraRecyclerViewAdapter addEspCameraRecyclerViewAdapter = new AddEspCameraRecyclerViewAdapter(context, mainPresenter, espCamerasIp, this);

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

    public void closeBottomSheet() {
        this.dismiss();
    }
}

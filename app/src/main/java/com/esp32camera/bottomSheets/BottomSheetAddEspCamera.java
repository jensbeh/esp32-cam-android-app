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
import android.widget.EditText;
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
    private LinearLayout ll_manuel_ip_input;
    private EditText et_selectIpAddress;
    private EditText et_selectName;
    private Button button_rv_manuelConnectCamera;
    private TextView tv_errorIpAddress;

    private List<String> espCamerasIp;

    /**
     * constructor where view is init and method is called to load all available EspCameras
     */
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
        rv_add_espCameras = (RecyclerView) bottomSheetView.findViewById(R.id.rv_add_espCameras);
        rv_add_espCameras.setVisibility(View.GONE);

        ll_manuel_ip_input = (LinearLayout) bottomSheetView.findViewById(R.id.ll_manuel_ip_input);
        ll_manuel_ip_input.setVisibility(View.GONE);
        et_selectIpAddress = (EditText) bottomSheetView.findViewById(R.id.et_selectIpAddress);
        et_selectName = (EditText) bottomSheetView.findViewById(R.id.et_selectName);
        button_rv_manuelConnectCamera = (Button) bottomSheetView.findViewById(R.id.button_rv_manuelConnectCamera);
        tv_errorIpAddress = (TextView) bottomSheetView.findViewById(R.id.tv_errorIpAddress);
        tv_errorIpAddress.setVisibility(View.GONE);

        loadAllEspCameras(new ReadyCallback() {
            @Override
            public void onReady() {
                mainPresenter.getActivity().runOnUiThread(() -> {
                    loadingLayout.setVisibility(View.GONE);
                    if (!espCamerasIp.isEmpty()) {
                        rv_add_espCameras.setVisibility(View.VISIBLE);
                    }
                    ll_manuel_ip_input.setVisibility(View.VISIBLE);

                    setupAddEspCamerasRecyclerView();
                });
            }
        });

        setupOnListener();
    }

    private void setupOnListener() {
        button_rv_manuelConnectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_selectIpAddress.getText().toString().equals("") && !et_selectName.getText().toString().equals("")) {
                    String ipAddress = et_selectIpAddress.getText().toString();
                    String name = et_selectName.getText().toString();
                    if (checkForCorrectIpAddress(ipAddress)) {
                        // if ip is correct or not already saved
                        mainPresenter.setupNewEspCamera(ipAddress, name);
                        mainPresenter.saveEspCameras();
                        closeBottomSheet();

                    } else {
                        // if ip is not correct or already connected - show error
                        tv_errorIpAddress.setVisibility(View.VISIBLE);
                        final Animation out = new AlphaAnimation(1.0f, 0.0f);
                        out.setDuration(500);
                        out.setStartOffset(3000);
                        out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                tv_errorIpAddress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        tv_errorIpAddress.setAnimation(out);
                    }
                }
            }
        });
    }

    /**
     * method to ping the ipAddress and check if correct ipAddress is already connected
     * returns bool
     */
    private boolean checkForCorrectIpAddress(String ipAddress) {
        int timeout = 100;

        try {
            // catch every error -> wrong ip address or some letters instead of numbers
            InetAddress.getByName(ipAddress);
        } catch (Exception e) {
            return false;
        }

        try {
            if (InetAddress.getByName(ipAddress).isReachable(timeout)) {
                InetAddress addr = InetAddress.getByName(ipAddress);
                String hostName = addr.getHostName();
                if (hostName.contains("ESP-Camera") && !mainPresenter.ifCameraExisting(ipAddress)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
        // set pulsing animation for searching text
        Animation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(100);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_searching.startAnimation(anim);

        // load all available espCameras
        AsyncTask.execute(() -> {
            WifiManager wifiMgr = (WifiManager) mainPresenter.getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);

            // ping all ipAddresses in network from 1 to 255
            // TODO change ipAddressMaxCount to 255
            String currentSubIpAddress = ipAddress.substring(0, ipAddress.lastIndexOf("."));
            int timeout = 100;
            int ipAddressMaxCount = 46;
            for (int i = 1; i < ipAddressMaxCount; i++) {
                String host = currentSubIpAddress + "." + i;
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)) {
                        InetAddress addr = InetAddress.getByName(host);
                        String hostName = addr.getHostName();
                        if (hostName.contains("ESP-Camera") && !mainPresenter.ifCameraExisting(host)) {
                            // add ipAddress if its a camera ipAddress and not already connected
                            espCamerasIp.add(host);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // set progress to progressBar
                progressBarLoading.setProgress((int) (round(i * (100.0 / ipAddressMaxCount))));
            }
            // call callback after read all available espCameras
            readyCallback.onReady();
        });
    }

    /**
     * shows all available esp-cameras and setup the handler
     * gets all ipAddresses of cameras to make them visible and selectable
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

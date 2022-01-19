package com.esp32camera.camSettings;

import static com.esp32camera.util.Constants.AEC2_PATH;
import static com.esp32camera.util.Constants.AEC_VALUE_PATH;
import static com.esp32camera.util.Constants.AE_LEVEL_PATH;
import static com.esp32camera.util.Constants.AGC_CTRL_STATE_PATH;
import static com.esp32camera.util.Constants.AGC_GAIN_PATH;
import static com.esp32camera.util.Constants.AUTOWB_GAIN_PATH;
import static com.esp32camera.util.Constants.BPC_PATH;
import static com.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.COLORBAR_PATH;
import static com.esp32camera.util.Constants.CONTRAST_PATH;
import static com.esp32camera.util.Constants.EXPOSURE_CTRL_STATE_PATH;
import static com.esp32camera.util.Constants.FRAMESIZE_PATH;
import static com.esp32camera.util.Constants.GAINCEILING_PATH;
import static com.esp32camera.util.Constants.HMIRROR_PATH;
import static com.esp32camera.util.Constants.LENC_PATH;
import static com.esp32camera.util.Constants.QUALITY_PATH;
import static com.esp32camera.util.Constants.RAW_GMA_PATH;
import static com.esp32camera.util.Constants.SATURATION_PATH;
import static com.esp32camera.util.Constants.SPECIAL_EFFECT_PATH;
import static com.esp32camera.util.Constants.VFLIP_PATH;
import static com.esp32camera.util.Constants.WB_MODE_PATH;
import static com.esp32camera.util.Constants.WHITEBALANCE_STATE_PATH;
import static com.esp32camera.util.Constants.WPC_PATH;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.google.android.material.slider.Slider;

public class CamSettingsFragment extends Fragment implements CamSettingsContract.View {

    private View view;
    private MainPresenter mainPresenter;
    private CamSettingsPresenter camSettingsPresenter;

    private EditText et_new_camera_name;
    private Button button_change_camera_name;
    private TextView tv_camera_name_settings;
    private Spinner spinner_FrameSize;
    private Slider sliderQuality;
    private Slider sliderBrightness;
    private Slider sliderContrast;
    private Slider sliderSaturation;
    private Spinner spinner_SpecialEffect;
    private SwitchCompat switch_AutoWhiteBalanceState;
    private SwitchCompat switch_AutoWbGain;
    private LinearLayout ll_WbMode;
    private Spinner spinner_WbMode;
    private SwitchCompat switch_ExposureCtrlState;
    private Slider sliderAecValue;
    private LinearLayout ll_AecValue;
    private SwitchCompat switch_Aec2;
    private Slider sliderAeLevel;
    private SwitchCompat switch_AgcCtrlState;
    private Slider sliderAgcGain;
    private LinearLayout ll_AgcGain;
    private Slider sliderGainCeiling;
    private LinearLayout ll_GainCeiling;
    private SwitchCompat switch_Bpc;
    private SwitchCompat switch_Wpc;
    private SwitchCompat switch_RawGma;
    private SwitchCompat switch_Lenc;
    private SwitchCompat switch_Hmirror;
    private SwitchCompat switch_Vflip;
    private SwitchCompat switch_Colorbar;
    private ArrayAdapter<CharSequence> frameSizeAdapter;
    private Toolbar toolbar_camSettings;

    public CamSettingsFragment(MainPresenter mainPresenter, CamSettingsPresenter camSettingsPresenter) {
        this.mainPresenter = mainPresenter;
        this.camSettingsPresenter = camSettingsPresenter;

        this.camSettingsPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cam_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init ui
        toolbar_camSettings = view.findViewById(R.id.toolbar_camSettings);

        et_new_camera_name = view.findViewById(R.id.et_new_camera_name);
        button_change_camera_name = view.findViewById(R.id.button_change_camera_name);
        tv_camera_name_settings = view.findViewById(R.id.tv_camera_name_settings);

        spinner_FrameSize = view.findViewById(R.id.spinner_FrameSize);
        frameSizeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.framesize_array, android.R.layout.simple_spinner_item);
        frameSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_FrameSize.setAdapter(frameSizeAdapter);

        sliderQuality = view.findViewById(R.id.sliderQuality);
        sliderBrightness = view.findViewById(R.id.sliderBrightness);
        sliderContrast = view.findViewById(R.id.sliderContrast);
        sliderSaturation = view.findViewById(R.id.sliderSaturation);

        spinner_SpecialEffect = view.findViewById(R.id.spinner_SpecialEffect);
        ArrayAdapter<CharSequence> specialEffectAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.specialEffect_array, android.R.layout.simple_spinner_item);
        specialEffectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SpecialEffect.setAdapter(specialEffectAdapter);

        switch_AutoWhiteBalanceState = view.findViewById(R.id.switch_AutoWhiteBalanceState);
        switch_AutoWbGain = view.findViewById(R.id.switch_AutoWbGain);

        ll_WbMode = view.findViewById(R.id.ll_WbMode);
        spinner_WbMode = view.findViewById(R.id.spinner_WbMode);
        ArrayAdapter<CharSequence> wbModeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.wbMode_array, android.R.layout.simple_spinner_item);
        wbModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_WbMode.setAdapter(wbModeAdapter);

        switch_ExposureCtrlState = view.findViewById(R.id.switch_ExposureCtrlState);
        sliderAecValue = view.findViewById(R.id.sliderAecValue);
        ll_AecValue = view.findViewById(R.id.ll_AecValue);
        switch_Aec2 = view.findViewById(R.id.switch_Aec2);
        sliderAeLevel = view.findViewById(R.id.sliderAeLevel);
        switch_AgcCtrlState = view.findViewById(R.id.switch_AgcCtrlState);
        sliderAgcGain = view.findViewById(R.id.sliderAgcGain);
        ll_AgcGain = view.findViewById(R.id.ll_AgcGain);
        sliderGainCeiling = view.findViewById(R.id.sliderGainCeiling);
        ll_GainCeiling = view.findViewById(R.id.ll_GainCeiling);
        switch_Bpc = view.findViewById(R.id.switch_Bpc);
        switch_Wpc = view.findViewById(R.id.switch_Wpc);
        switch_RawGma = view.findViewById(R.id.switch_RawGma);
        switch_Lenc = view.findViewById(R.id.switch_Lenc);
        switch_Hmirror = view.findViewById(R.id.switch_Hmirror);
        switch_Vflip = view.findViewById(R.id.switch_Vflip);
        switch_Colorbar = view.findViewById(R.id.switch_Colorbar);

        // set values
        tv_camera_name_settings.setText(camSettingsPresenter.getEspCamera().getName());
    }

    @Override
    public void onStart() {
        super.onStart();

        // init ui - need to set here because otherwise the slider wont be updated by values
        initUi();
        if (!mainPresenter.isWebSocketConnected()) {
            disableCamSettings();
        }

        setupOnListener();
    }

    private void initUi() {
        setFrameSizeToSpinner(camSettingsPresenter.getEspCamera().getFramesize());

        if (camSettingsPresenter.getEspCamera().getQuality() == 0) {
            sliderQuality.setValue(10);
        } else {
            sliderQuality.setValue(camSettingsPresenter.getEspCamera().getQuality());
        }
        sliderBrightness.setValue(camSettingsPresenter.getEspCamera().getBrightness());
        sliderContrast.setValue(camSettingsPresenter.getEspCamera().getContrast());
        sliderSaturation.setValue(camSettingsPresenter.getEspCamera().getSaturation());

        spinner_SpecialEffect.setSelection(camSettingsPresenter.getEspCamera().getSpecialEffect(), false);

        switch_AutoWhiteBalanceState.setChecked(camSettingsPresenter.getEspCamera().getAutoWhiteBalanceState() == 1);
        if (camSettingsPresenter.getEspCamera().getAutoWbGain() == 1) {
            switch_AutoWbGain.setChecked(true);
            ll_WbMode.setVisibility(View.VISIBLE);
        } else {
            switch_AutoWbGain.setChecked(false);
            ll_WbMode.setVisibility(View.GONE);
        }

        spinner_WbMode.setSelection(camSettingsPresenter.getEspCamera().getWbMode(), false);

        if (camSettingsPresenter.getEspCamera().getExposureCtrlState() == 1) {
            switch_ExposureCtrlState.setChecked(true);
            ll_AecValue.setVisibility(View.GONE);
        } else {
            switch_ExposureCtrlState.setChecked(false);
            ll_AecValue.setVisibility(View.VISIBLE);
        }
        sliderAecValue.setValue(camSettingsPresenter.getEspCamera().getAecValue());
        switch_Aec2.setChecked(camSettingsPresenter.getEspCamera().getAec2() == 1);
        sliderAeLevel.setValue(camSettingsPresenter.getEspCamera().getAeLevel());
        if (camSettingsPresenter.getEspCamera().getAgcCtrlState() == 1) {
            switch_AgcCtrlState.setChecked(true);
            ll_GainCeiling.setVisibility(View.VISIBLE);
            ll_AgcGain.setVisibility(View.GONE);
        } else {
            switch_AgcCtrlState.setChecked(false);
            ll_GainCeiling.setVisibility(View.GONE);
            ll_AgcGain.setVisibility(View.VISIBLE);
        }
        sliderAgcGain.setValue(camSettingsPresenter.getEspCamera().getAgcGain());
        sliderGainCeiling.setValue(camSettingsPresenter.getEspCamera().getGainCeiling());
        switch_Bpc.setChecked(camSettingsPresenter.getEspCamera().getBpc() == 1);
        switch_Wpc.setChecked(camSettingsPresenter.getEspCamera().getWpc() == 1);
        switch_RawGma.setChecked(camSettingsPresenter.getEspCamera().getRawGma() == 1);
        switch_Lenc.setChecked(camSettingsPresenter.getEspCamera().getLenc() == 1);
        switch_Hmirror.setChecked(camSettingsPresenter.getEspCamera().getHmirror() == 1);
        switch_Vflip.setChecked(camSettingsPresenter.getEspCamera().getVflip() == 1);
        switch_Colorbar.setChecked(camSettingsPresenter.getEspCamera().getColorbar() == 1);
    }

    private void setupOnListener() {
        toolbar_camSettings.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_camera_settings_reset_values) {
                Toast.makeText(mainPresenter.getActivity(), "Reset Values", Toast.LENGTH_LONG).show();

                mainPresenter.resetCameraValues(camSettingsPresenter.getEspCamera());
            } else if (item.getItemId() == R.id.nav_camera_settings_remove_camera) {
                Toast.makeText(mainPresenter.getActivity(), "Remove Camera", Toast.LENGTH_LONG).show();

                mainPresenter.removeCamera(camSettingsPresenter.getEspCamera());
                mainPresenter.navigateToHomeFragment();
            }
            return true;
        });

        // navigates back to home
        toolbar_camSettings.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.navigateToHomeFragment();
            }
        });

        // set name
        button_change_camera_name.setOnClickListener(v -> {
            if (!et_new_camera_name.getText().toString().equals("")) {
                mainPresenter.setCameraName(et_new_camera_name.getText().toString());
                et_new_camera_name.setText("");
            }
        });

        // set framesize
        spinner_FrameSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();

                if (value.equals(getString(R.string.framesize_UXGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 10);
                } else if (value.equals(getString(R.string.framesize_SXGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 9);
                } else if (value.equals(getString(R.string.framesize_XGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 8);
                } else if (value.equals(getString(R.string.framesize_SVGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 7);
                } else if (value.equals(getString(R.string.framesize_VGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 6);
                } else if (value.equals(getString(R.string.framesize_CIF))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 5);
                } else if (value.equals(getString(R.string.framesize_QVGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 4);
                } else if (value.equals(getString(R.string.framesize_HQVGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 3);
                } else if (value.equals(getString(R.string.framesize_QQVGA))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + FRAMESIZE_PATH + 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set quality
        sliderQuality.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + QUALITY_PATH + intValue);
            }
        });

        // set brightness
        sliderBrightness.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + BRIGHTNESS_PATH + intValue);
            }
        });

        // set contrast
        sliderContrast.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + CONTRAST_PATH + intValue);
            }
        });

        // set saturation
        sliderSaturation.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SATURATION_PATH + intValue);
            }
        });

        // set special effect
        spinner_SpecialEffect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();

                if (value.equals(getString(R.string.effect_no_effect))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 0);
                } else if (value.equals(getString(R.string.effect_negative))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 1);
                } else if (value.equals(getString(R.string.effect_grayscale))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 2);
                } else if (value.equals(getString(R.string.effect_red_tint))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 3);
                } else if (value.equals(getString(R.string.effect_green_tint))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 4);
                } else if (value.equals(getString(R.string.effect_blue_tint))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 5);
                } else if (value.equals(getString(R.string.effect_sepia))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + SPECIAL_EFFECT_PATH + 6);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set autoWhiteBalanceState
        switch_AutoWhiteBalanceState.setOnClickListener(v -> {
            if (switch_AutoWhiteBalanceState.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WHITEBALANCE_STATE_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WHITEBALANCE_STATE_PATH + 0);
            }
        });

        // set autoWbGain
        switch_AutoWbGain.setOnClickListener(v -> {
            if (switch_AutoWbGain.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AUTOWB_GAIN_PATH + 1);
                ll_WbMode.setVisibility(View.VISIBLE);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AUTOWB_GAIN_PATH + 0);
                ll_WbMode.setVisibility(View.GONE);
            }
        });

        // set wbMode
        spinner_WbMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();

                if (value.equals(getString(R.string.wbMode_auto))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WB_MODE_PATH + 0);
                } else if (value.equals(getString(R.string.wbMode_sunny))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WB_MODE_PATH + 1);
                } else if (value.equals(getString(R.string.wbMode_cloudy))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WB_MODE_PATH + 2);
                } else if (value.equals(getString(R.string.wbMode_office))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WB_MODE_PATH + 3);
                } else if (value.equals(getString(R.string.wbMode_home))) {
                    mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WB_MODE_PATH + 4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set exposureCtrlState
        switch_ExposureCtrlState.setOnClickListener(v -> {
            if (switch_ExposureCtrlState.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + EXPOSURE_CTRL_STATE_PATH + 1);
                ll_AecValue.setVisibility(View.GONE);

            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + EXPOSURE_CTRL_STATE_PATH + 0);
                ll_AecValue.setVisibility(View.VISIBLE);
            }
        });

        // set aecValue
        sliderAecValue.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AEC_VALUE_PATH + intValue);
            }
        });

        // set aec2
        switch_Aec2.setOnClickListener(v -> {
            if (switch_Aec2.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AEC2_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AEC2_PATH + 0);
            }
        });

        // set aeLevel
        sliderAeLevel.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AE_LEVEL_PATH + intValue);
            }
        });

        // set agcCtrlState
        switch_AgcCtrlState.setOnClickListener(v -> {
            if (switch_AgcCtrlState.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AGC_CTRL_STATE_PATH + 1);
                ll_AgcGain.setVisibility(View.GONE);
                ll_GainCeiling.setVisibility(View.VISIBLE);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AGC_CTRL_STATE_PATH + 0);
                ll_GainCeiling.setVisibility(View.GONE);
                ll_AgcGain.setVisibility(View.VISIBLE);
            }
        });

        // set agcGain
        sliderAgcGain.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + AGC_GAIN_PATH + intValue);
            }
        });

        // set gainCeiling
        sliderGainCeiling.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + GAINCEILING_PATH + intValue);
            }
        });

        // set bpc
        switch_Bpc.setOnClickListener(v -> {
            if (switch_Bpc.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + BPC_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + BPC_PATH + 0);
            }
        });

        // set wpc
        switch_Wpc.setOnClickListener(v -> {
            if (switch_Wpc.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WPC_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + WPC_PATH + 0);
            }
        });

        // set rawGma
        switch_RawGma.setOnClickListener(v -> {
            if (switch_RawGma.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + RAW_GMA_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + RAW_GMA_PATH + 0);
            }
        });

        // set lenc
        switch_Lenc.setOnClickListener(v -> {
            if (switch_Lenc.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + LENC_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + LENC_PATH + 0);
            }
        });

        // set hMirror
        switch_Hmirror.setOnClickListener(v -> {
            if (switch_Hmirror.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + HMIRROR_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + HMIRROR_PATH + 0);
            }
        });

        // set vFlip
        switch_Vflip.setOnClickListener(v -> {
            if (switch_Vflip.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + VFLIP_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + VFLIP_PATH + 0);
            }
        });

        // set colorbar
        switch_Colorbar.setOnClickListener(v -> {
            if (switch_Colorbar.isChecked()) {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + COLORBAR_PATH + 1);
            } else {
                mainPresenter.sendWebSocketMessage(camSettingsPresenter.getEspCamera(), CAM_CONTROLS_PATH + COLORBAR_PATH + 0);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        sliderBrightness.addOnChangeListener(null);
        sliderContrast.addOnChangeListener(null);

        camSettingsPresenter.setSelectedEspCamera(null);
    }

    @Override
    public void setCameraNameInText(String cameraName) {
        tv_camera_name_settings.setText(cameraName);
    }

    @Override
    public void setSpinnerCameraFramesize(int framesize) {
        setFrameSizeToSpinner(framesize);
    }

    @Override
    public void setSliderCameraQuality(int quality) {
        sliderQuality.setValue(quality);
    }

    @Override
    public void setSliderCameraBrightness(int brightness) {
        sliderBrightness.setValue(brightness);
    }

    @Override
    public void setSliderCameraContrast(int contrast) {
        sliderContrast.setValue(contrast);
    }

    @Override
    public void setSliderCameraSaturation(int saturation) {
        sliderSaturation.setValue(saturation);
    }

    @Override
    public void setSpinnerCameraSpecialEffect(int specialEffect) {
        spinner_SpecialEffect.setSelection(camSettingsPresenter.getEspCamera().getSpecialEffect(), false);
    }

    @Override
    public void setSwitchCameraAutoWhiteBalanceState(int whiteBalanceState) {
        getActivity().runOnUiThread(() -> {
            switch_AutoWhiteBalanceState.setChecked(whiteBalanceState == 1);
        });
    }

    @Override
    public void setSwitchCameraAutoWbGain(int awbGain) {
        getActivity().runOnUiThread(() -> {
            if (awbGain == 1) {
                switch_AutoWbGain.setChecked(true);
                ll_WbMode.setVisibility(View.VISIBLE);
            } else {
                switch_AutoWbGain.setChecked(false);
                ll_WbMode.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setSpinnerCameraWbMode(int wbMode) {
        spinner_WbMode.setSelection(wbMode, false);
    }

    @Override
    public void setSwitchCameraExposureCtrlState(int exposureCtrlState) {
        getActivity().runOnUiThread(() -> {
            if (exposureCtrlState == 1) {
                switch_ExposureCtrlState.setChecked(true);
                ll_AecValue.setVisibility(View.GONE);
            } else {
                switch_ExposureCtrlState.setChecked(false);
                ll_AecValue.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void setSliderCameraAecValue(int aecValue) {
        sliderAecValue.setValue(aecValue);
    }

    @Override
    public void setSwitchCameraAec2(int aec2) {
        getActivity().runOnUiThread(() -> {
            switch_Aec2.setChecked(aec2 == 1);
        });
    }

    @Override
    public void setSliderCameraAeLevel(int aeLevel) {
        sliderAeLevel.setValue(aeLevel);
    }

    @Override
    public void setSwitchCameraAgcCtrlState(int agcCtrlState) {
        getActivity().runOnUiThread(() -> {
            if (agcCtrlState == 1) {
                switch_AgcCtrlState.setChecked(true);
                ll_GainCeiling.setVisibility(View.VISIBLE);
                ll_AgcGain.setVisibility(View.GONE);
            } else {
                switch_AgcCtrlState.setChecked(false);
                ll_GainCeiling.setVisibility(View.GONE);
                ll_AgcGain.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void setSliderCameraAgcGain(int agcGain) {
        sliderAgcGain.setValue(agcGain);
    }

    @Override
    public void setSliderCameraGainCeiling(int gainCeiling) {
        sliderGainCeiling.setValue(gainCeiling);
    }

    @Override
    public void setSwitchCameraBpc(int bpc) {
        getActivity().runOnUiThread(() -> {
            switch_Bpc.setChecked(bpc == 1);
        });
    }

    @Override
    public void setSwitchCameraWpc(int wpc) {
        getActivity().runOnUiThread(() -> {
            switch_Wpc.setChecked(wpc == 1);
        });
    }

    @Override
    public void setSwitchCameraRawGma(int rawGma) {
        getActivity().runOnUiThread(() -> {
            switch_RawGma.setChecked(rawGma == 1);
        });
    }

    @Override
    public void setSwitchCameraLenc(int lenc) {
        getActivity().runOnUiThread(() -> {
            switch_Lenc.setChecked(lenc == 1);
        });
    }

    @Override
    public void setSwitchCameraHmirror(int hMirror) {
        getActivity().runOnUiThread(() -> {
            switch_Hmirror.setChecked(hMirror == 1);
        });
    }

    @Override
    public void setSwitchCameraVflip(int vFlip) {
        getActivity().runOnUiThread(() -> {
            switch_Vflip.setChecked(vFlip == 1);
        });
    }

    @Override
    public void setSwitchCameraColorbar(int colorbar) {
        getActivity().runOnUiThread(() -> {
            switch_Colorbar.setChecked(colorbar == 1);
        });
    }

    private void setFrameSizeToSpinner(int frameSize) {
        switch (frameSize) {
            case 10:
                spinner_FrameSize.setSelection(0, false);
                break;
            case 9:
                spinner_FrameSize.setSelection(1, false);
                break;
            case 8:
                spinner_FrameSize.setSelection(2, false);
                break;
            case 7:
                spinner_FrameSize.setSelection(3, false);
                break;
            case 6:
                spinner_FrameSize.setSelection(4, false);
                break;
            case 5:
                spinner_FrameSize.setSelection(5, false);
                break;
            case 4:
                spinner_FrameSize.setSelection(6, false);
                break;
            case 3:
                spinner_FrameSize.setSelection(7, false);
                break;
            case 0:
                spinner_FrameSize.setSelection(8, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void enableCamSettings() {
        if (this.isVisible()) {
            getActivity().runOnUiThread(() -> initUi());

            spinner_FrameSize.setEnabled(true);

            sliderQuality.setEnabled(true);
            sliderBrightness.setEnabled(true);
            sliderContrast.setEnabled(true);
            sliderSaturation.setEnabled(true);

            spinner_SpecialEffect.setEnabled(true);

            switch_AutoWhiteBalanceState.setEnabled(true);
            switch_AutoWbGain.setEnabled(true);
            switch_AutoWbGain.setEnabled(true);

            spinner_WbMode.setEnabled(true);

            switch_ExposureCtrlState.setEnabled(true);
            switch_ExposureCtrlState.setEnabled(true);

            sliderAecValue.setEnabled(true);
            switch_Aec2.setEnabled(true);
            sliderAeLevel.setEnabled(true);

            switch_AgcCtrlState.setEnabled(true);
            switch_AgcCtrlState.setEnabled(true);

            sliderAgcGain.setEnabled(true);
            sliderGainCeiling.setEnabled(true);
            switch_Bpc.setEnabled(true);
            switch_Wpc.setEnabled(true);
            switch_RawGma.setEnabled(true);
            switch_Lenc.setEnabled(true);
            switch_Hmirror.setEnabled(true);
            switch_Vflip.setEnabled(true);
            switch_Colorbar.setEnabled(true);
        }
    }

    @Override
    public void disableCamSettings() {
        if (this.isVisible()) {
            spinner_FrameSize.setEnabled(false);

            sliderQuality.setEnabled(false);
            sliderBrightness.setEnabled(false);
            sliderContrast.setEnabled(false);
            sliderSaturation.setEnabled(false);

            spinner_SpecialEffect.setEnabled(false);

            switch_AutoWhiteBalanceState.setEnabled(false);
            switch_AutoWbGain.setEnabled(false);
            switch_AutoWbGain.setEnabled(false);

            spinner_WbMode.setEnabled(false);

            switch_ExposureCtrlState.setEnabled(false);
            switch_ExposureCtrlState.setEnabled(false);

            sliderAecValue.setEnabled(false);
            switch_Aec2.setEnabled(false);
            sliderAeLevel.setEnabled(false);

            switch_AgcCtrlState.setEnabled(false);
            switch_AgcCtrlState.setEnabled(false);

            sliderAgcGain.setEnabled(false);
            sliderGainCeiling.setEnabled(false);
            switch_Bpc.setEnabled(false);
            switch_Wpc.setEnabled(false);
            switch_RawGma.setEnabled(false);
            switch_Lenc.setEnabled(false);
            switch_Hmirror.setEnabled(false);
            switch_Vflip.setEnabled(false);
            switch_Colorbar.setEnabled(false);
        }
    }
}
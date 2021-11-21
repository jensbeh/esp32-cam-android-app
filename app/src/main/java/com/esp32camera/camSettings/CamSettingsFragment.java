package com.esp32camera.camSettings;

import static com.esp32camera.util.Constants.AEC_VALUE_PATH;
import static com.esp32camera.util.Constants.AE_LEVEL_PATH;
import static com.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.esp32camera.util.Constants.CAM_CONTROLS_PATH;
import static com.esp32camera.util.Constants.CONTRAST_PATH;
import static com.esp32camera.util.Constants.QUALITY_PATH;
import static com.esp32camera.util.Constants.SATURATION_PATH;
import static com.esp32camera.util.Constants.WB_MODE_PATH;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.google.android.material.slider.Slider;

public class CamSettingsFragment extends Fragment implements CamSettingsContract.View {

    private View view;
    private MainPresenter mainPresenter;
    private CamSettingsPresenter camSettingsPresenter;

    private View button_toHome;
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
    private Slider sliderWbMode;
    private SwitchCompat switch_ExposureCtrlState;
    private Slider sliderAecValue;
    private SwitchCompat switch_Aec2;
    private Slider sliderAeLevel;
    private SwitchCompat switch_AgcCtrlState;
    private Slider sliderAgcGain;
    private Slider sliderGainCeiling;
    private SwitchCompat switch_Bpc;
    private SwitchCompat switch_Wpc;
    private SwitchCompat switch_RawGma;
    private SwitchCompat switch_Lenc;
    private SwitchCompat switch_Hmirror;
    private SwitchCompat switch_Vflip;
    private SwitchCompat switch_Colorbar;

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
        button_toHome = view.findViewById(R.id.button_toHome);

        et_new_camera_name = view.findViewById(R.id.et_new_camera_name);
        button_change_camera_name = view.findViewById(R.id.button_change_camera_name);
        tv_camera_name_settings = view.findViewById(R.id.tv_camera_name_settings);

        spinner_FrameSize = view.findViewById(R.id.spinner_FrameSize);
        ArrayAdapter<CharSequence> frameSizeAdapter = ArrayAdapter.createFromResource(getActivity(),
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
        sliderWbMode = view.findViewById(R.id.sliderWbMode);
        switch_ExposureCtrlState = view.findViewById(R.id.switch_ExposureCtrlState);
        sliderAecValue = view.findViewById(R.id.sliderAecValue);
        switch_Aec2 = view.findViewById(R.id.switch_Aec2);
        sliderAeLevel = view.findViewById(R.id.sliderAeLevel);
        switch_AgcCtrlState = view.findViewById(R.id.switch_AgcCtrlState);
        sliderAgcGain = view.findViewById(R.id.sliderAgcGain);
        sliderGainCeiling = view.findViewById(R.id.sliderGainCeiling);
        switch_Bpc = view.findViewById(R.id.switch_Bpc);
        switch_Wpc = view.findViewById(R.id.switch_Wpc);
        switch_RawGma = view.findViewById(R.id.switch_RawGma);
        switch_Lenc = view.findViewById(R.id.switch_Lenc);
        switch_Hmirror = view.findViewById(R.id.switch_Hmirror);
        switch_Vflip = view.findViewById(R.id.switch_Vflip);
        switch_Colorbar = view.findViewById(R.id.switch_Colorbar);

        // set values
        tv_camera_name_settings.setText(camSettingsPresenter.getCameraName());
    }

    @Override
    public void onStart() {
        super.onStart();

        // need to set here because otherwise the slider wont be updated by values
        int brightnessValue = camSettingsPresenter.getCameraBrightness();
        int contrastValue = camSettingsPresenter.getCameraContrast();
        sliderBrightness.setValue(brightnessValue);
        sliderContrast.setValue(contrastValue);

        setupOnListener();
    }

    private void setupOnListener() {
        // navigates back to home
        button_toHome.setOnClickListener(v -> {
            mainPresenter.navigateToHomeFragment();
        });

        // set name
        button_change_camera_name.setOnClickListener(v -> {
            if (!et_new_camera_name.getText().toString().equals("")) {
                camSettingsPresenter.setCameraName(et_new_camera_name.getText().toString());
                et_new_camera_name.setText("");
            }
        });

        // set framesize
        spinner_FrameSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set quality
        sliderQuality.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + QUALITY_PATH + intValue);
            }
        });

        // set brightness
        sliderBrightness.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + BRIGHTNESS_PATH + intValue);
            }
        });

        // set contrast
        sliderContrast.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + CONTRAST_PATH + intValue);
            }
        });

        // set saturation
        sliderSaturation.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + SATURATION_PATH + intValue);
            }
        });

        // set special effect
        spinner_SpecialEffect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set autoWhiteBalanceState
        switch_AutoWhiteBalanceState.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set autoWbGain
        switch_AutoWbGain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set wbMode
        sliderWbMode.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + WB_MODE_PATH + intValue);
            }
        });

        // set exposureCtrlState
        switch_ExposureCtrlState.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set aecValue
        sliderAecValue.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + AEC_VALUE_PATH + intValue);
            }
        });

        // set aec2
        switch_Aec2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set aeLevel
        sliderAeLevel.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + AE_LEVEL_PATH + intValue);
            }
        });

        // set agcCtrlState
        switch_AgcCtrlState.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set agcGain
        sliderAgcGain.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + AEC_VALUE_PATH + intValue);
            }
        });

        // set gainCeiling
        sliderGainCeiling.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROLS_PATH + AEC_VALUE_PATH + intValue);
            }
        });

        // set bpc
        switch_Bpc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set wpc
        switch_Wpc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set rawGma
        switch_RawGma.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set lenc
        switch_Lenc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set hMirror
        switch_Hmirror.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set vFlip
        switch_Vflip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });

        // set colorbar
        switch_Colorbar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        sliderBrightness.addOnChangeListener(null);
        sliderContrast.addOnChangeListener(null);
    }

    @Override
    public void setCameraNameInText(String cameraName) {
        tv_camera_name_settings.setText(cameraName);
    }

    @Override
    public void setSpinnerCameraFramesize(int specialEffect) {

    }

    @Override
    public void setSliderCameraQuality(int quality) {

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

    }

    @Override
    public void setSpinnerCameraSpecialEffect(int specialEffect) {

    }

    @Override
    public void setSwitchCameraAutoWhiteBalanceState(int whitebal) {

    }

    @Override
    public void setSwitchCameraAutoWbGain(int awbGain) {

    }

    @Override
    public void setSliderCameraWbMode(int wbMode) {

    }

    @Override
    public void setSwitchCameraExposureCtrlState(int exposureCtrl) {

    }

    @Override
    public void setSliderCameraAecValue(int aecValue) {

    }

    @Override
    public void setSwitchCameraAec2(int aec2) {

    }

    @Override
    public void setSliderCameraAeLevel(int aeLevel) {

    }

    @Override
    public void setSwitchCameraAgcCtrlState(int gainCtrl) {

    }

    @Override
    public void setSliderCameraAgcGain(int agcGain) {

    }

    @Override
    public void setSliderCameraGainCeiling(int gainCeiling) {

    }

    @Override
    public void setSwitchCameraBpc(int bpc) {

    }

    @Override
    public void setSwitchCameraWpc(int wpc) {

    }

    @Override
    public void setSwitchCameraRawGma(int rawGma) {

    }

    @Override
    public void setSwitchCameraLenc(int lenc) {

    }

    @Override
    public void setSwitchCameraHmirror(int hMirror) {

    }

    @Override
    public void setSwitchCameraVflip(int vFlip) {

    }

    @Override
    public void setSwitchCameraColorbar(int colorbar) {

    }
}
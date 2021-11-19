package com.esp32camera.camSettings;

import static com.esp32camera.util.Constants.AEC_VALUE_PATH;
import static com.esp32camera.util.Constants.AE_LEVEL_PATH;
import static com.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.esp32camera.util.Constants.CAM_CONTROL_PATH;
import static com.esp32camera.util.Constants.CONTRAST_PATH;
import static com.esp32camera.util.Constants.QUALITY_PATH;
import static com.esp32camera.util.Constants.SATURATION_PATH;
import static com.esp32camera.util.Constants.SHARPNESS_PATH;
import static com.esp32camera.util.Constants.WB_MODE_PATH;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    private Slider sliderBrightness;
    private Slider sliderContrast;
    private View button_toHome;
    private EditText et_new_camera_name;
    private Button button_change_camera_name;
    private TextView tv_camera_name_settings;
    private Slider sliderQuality;
    private Slider sliderSaturation;
    private Slider sliderSharpness;
    private Spinner spinner_SpecialEffect;
    private SwitchCompat switch_Whitebal;
    private SwitchCompat switch_AwbGain;
    private Slider sliderWbMode;
    private SwitchCompat switch_ExposureCtrl;
    private SwitchCompat switch_Aec2;
    private Slider sliderAeLevel;
    private Slider sliderAecValue;
    private SwitchCompat switch_GainCtrl;
    private Slider sliderAgcGain;
    private Slider sliderGainCeiling;
    private SwitchCompat switch_Bpc;
    private SwitchCompat switch_Wpc;
    private SwitchCompat switch_RawGma;
    private SwitchCompat switch_Lenc;
    private SwitchCompat switch_Hmirror;
    private SwitchCompat switch_Vflip;
    private SwitchCompat switch_Dcw;
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
        // button
        button_toHome = view.findViewById(R.id.button_toHome);

        sliderBrightness = view.findViewById(R.id.sliderBrightness);
        sliderContrast = view.findViewById(R.id.sliderContrast);

        et_new_camera_name = view.findViewById(R.id.et_new_camera_name);
        button_change_camera_name = view.findViewById(R.id.button_change_camera_name);
        tv_camera_name_settings = view.findViewById(R.id.tv_camera_name_settings);

        sliderQuality = view.findViewById(R.id.sliderQuality);
        sliderSaturation = view.findViewById(R.id.sliderSaturation);
        sliderSharpness = view.findViewById(R.id.sliderSharpness);

        // Denoise

        spinner_SpecialEffect = view.findViewById(R.id.spinner_SpecialEffect);
        switch_Whitebal = view.findViewById(R.id.switch_Whitebal);
        switch_AwbGain = view.findViewById(R.id.switch_AwbGain);

        sliderWbMode = view.findViewById(R.id.sliderWbMode);

        switch_ExposureCtrl = view.findViewById(R.id.switch_ExposureCtrl);
        switch_Aec2 = view.findViewById(R.id.switch_Aec2);

        sliderAeLevel = view.findViewById(R.id.sliderAeLevel);
        sliderAecValue = view.findViewById(R.id.sliderAecValue);

        switch_GainCtrl = view.findViewById(R.id.switch_GainCtrl);

        sliderAgcGain = view.findViewById(R.id.sliderAgcGain);
        sliderGainCeiling = view.findViewById(R.id.sliderGainCeiling);

        switch_Bpc = view.findViewById(R.id.switch_Bpc);
        switch_Wpc = view.findViewById(R.id.switch_Wpc);
        switch_RawGma = view.findViewById(R.id.switch_RawGma);
        switch_Lenc = view.findViewById(R.id.switch_Lenc);
        switch_Hmirror = view.findViewById(R.id.switch_Hmirror);
        switch_Vflip = view.findViewById(R.id.switch_Vflip);
        switch_Dcw = view.findViewById(R.id.switch_Dcw);
        switch_Colorbar = view.findViewById(R.id.switch_Colorbar);

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

        // set brightness
        sliderBrightness.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + BRIGHTNESS_PATH + intValue);
            }
        });
        // set contrast
        sliderContrast.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + CONTRAST_PATH + intValue);
            }
        });
        // set name
        button_change_camera_name.setOnClickListener(v -> {
            if (!et_new_camera_name.getText().toString().equals("")) {
                camSettingsPresenter.setCameraName(et_new_camera_name.getText().toString());
                et_new_camera_name.setText("");
            }
        });
        // set quality
        sliderQuality.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + QUALITY_PATH + intValue);
            }
        });
        // set saturation
        sliderSaturation.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + SATURATION_PATH + intValue);
            }
        });
        // set sharpness
        sliderSharpness.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + SHARPNESS_PATH + intValue);
            }
        });
        // here denoise
        // set special effect
        //spinner_SpecialEffect.setOnItemSelectedListener();
        // set whitebal
        switch_Whitebal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });
        // set awbGain
        switch_AwbGain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });
        // set sharpness
        sliderWbMode.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + WB_MODE_PATH + intValue);
            }
        });
        // set exposureCtrl
        switch_ExposureCtrl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });
        // set aec2
        switch_Aec2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });
        // set aeLevel
        sliderAeLevel.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + AE_LEVEL_PATH + intValue);
            }
        });
        // set aecValue
        sliderAecValue.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + AEC_VALUE_PATH + intValue);
            }
        });
        // set gainCtrl
        switch_GainCtrl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // stuff
        });
        // set agcGain
        sliderAgcGain.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + AEC_VALUE_PATH + intValue);
            }
        });
        // set gainCeiling
        sliderGainCeiling.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                int intValue = (int) value;
                mainPresenter.sendWebSocketMessage(CAM_CONTROL_PATH + AEC_VALUE_PATH + intValue);
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
        // set dcw
        switch_Dcw.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
    public void setSliderCameraBrightness(int brightness) {
        sliderBrightness.setValue(brightness);
    }

    @Override
    public void setSliderCameraContrast(int contrast) {
        sliderContrast.setValue(contrast);
    }

    @Override
    public void setCameraNameInText(String cameraName) {
        tv_camera_name_settings.setText(cameraName);
    }

    @Override
    public void setSliderCameraQuality(int quality) {

    }

    @Override
    public void setSliderCameraSaturation(int saturation) {

    }

    @Override
    public void setSliderCameraSharpness(int sharpness) {

    }

    @Override
    public void setCameraDenoiseInView(int denoise) {

    }

    @Override
    public void setSpinnerCameraSpecialEffect(int specialEffect) {

    }

    @Override
    public void setSwitchCameraWhitebal(int whitebal) {

    }

    @Override
    public void setSwitchCameraAwbGain(int awbGain) {

    }

    @Override
    public void setSliderCameraWbMode(int wbMode) {

    }

    @Override
    public void setSwitchCameraExposureCtrl(int exposureCtrl) {

    }

    @Override
    public void setSwitchCameraAec2(int aec2) {

    }

    @Override
    public void setSliderCameraAeLevel(int aeLevel) {

    }

    @Override
    public void setSliderCameraAecValue(int aecValue) {

    }

    @Override
    public void setSwitchCameraGainCtrl(int gainCtrl) {

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
    public void setSwitchCameraDcw(int dcw) {

    }

    @Override
    public void setSwitchCameraColorbar(int colorbar) {

    }
}
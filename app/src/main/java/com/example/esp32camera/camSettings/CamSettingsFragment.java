package com.example.esp32camera.camSettings;

import static com.example.esp32camera.util.Constants.BRIGHTNESS_PATH;
import static com.example.esp32camera.util.Constants.CAM_CONTROL_PATH;
import static com.example.esp32camera.util.Constants.CONTRAST_PATH;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.esp32camera.MainPresenter;
import com.example.esp32camera.R;
import com.google.android.material.slider.Slider;

public class CamSettingsFragment extends Fragment implements CamSettingsContract.View {

    private View view;
    private MainPresenter mainPresenter;
    private CamSettingsPresenter camSettingsPresenter;

    private Slider sliderBrightness;
    private Slider sliderContrast;
    private View button_toHome;

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

        sliderBrightness = view.findViewById(R.id.sliderBrightness);
        sliderContrast = view.findViewById(R.id.sliderContrast);
        button_toHome = view.findViewById(R.id.button_toHome);
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

        // navigates back to home
        button_toHome.setOnClickListener(v -> {
            mainPresenter.navigateToHomeFragment();
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
}
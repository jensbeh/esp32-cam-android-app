package com.esp32camera.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.esp32camera.MainActivity;
import com.esp32camera.R;
import com.esp32camera.onBoarding.OnBoardingActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Check if we need to display our OnBoardingFragments or directly start the mainActivity
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("FIRST-TIME-USER", true)) {
            new Handler().postDelayed(() -> {
                // The user hasn't seen the OnBoardingFragments yet, so show it
                Intent i = new Intent(SplashActivity.this, OnBoardingActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }, 600);
        } else {
            // show mainActivity
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }, 800);
        }
    }
}
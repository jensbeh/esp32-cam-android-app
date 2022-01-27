package com.esp32camera.onBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.esp32camera.MainActivity;
import com.esp32camera.R;
import com.esp32camera.adapter.OnBoardingViewPagerAdapter;

/**
 * activity with viewPager which is shown when you are a first time user
 * https://www.youtube.com/watch?v=eUqi8YcSchI
 */
public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager vp_onBoardingSlider;
    private OnBoardingPresenter onBoardingPresenter;
    private Button btn_nextOnBoarding;
    private ConstraintLayout cl_onBoarding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        cl_onBoarding = (ConstraintLayout) findViewById(R.id.cl_onBoarding);

        vp_onBoardingSlider = (ViewPager) findViewById(R.id.vp_onBoardingSlider);
        btn_nextOnBoarding = (Button) findViewById(R.id.btn_nextOnBoarding);

        onBoardingPresenter = new OnBoardingPresenter(this);

        AnimationDrawable animationDrawable = (AnimationDrawable) cl_onBoarding.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        setupViewPager();

        setupOnListener();
    }

    private void setupOnListener() {
        // change button texts on next click or start application
        btn_nextOnBoarding.setOnClickListener(v -> {
            int position = vp_onBoardingSlider.getCurrentItem();

            // change page
            if (position < onBoardingPresenter.getFragmentList().size()) {
                position++;
                vp_onBoardingSlider.setCurrentItem(position);
                btn_nextOnBoarding.setText("Next");
            }

            // change button to start
            if (position == onBoardingPresenter.getFragmentList().size() - 1) {
                btn_nextOnBoarding.setText("Start");
            }

            // start application
            if (position == onBoardingPresenter.getFragmentList().size()) {
                // save sharedPreferences for FIRST-TIME-USER with false
                SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("FIRST-TIME-USER", false);
                editor.apply();

                Intent i = new Intent(OnBoardingActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });

        // change button texts on slide
        vp_onBoardingSlider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < onBoardingPresenter.getFragmentList().size()) {
                    btn_nextOnBoarding.setText("Next");
                }

                // change button to start
                if (position == onBoardingPresenter.getFragmentList().size() - 1) {
                    btn_nextOnBoarding.setText("Start");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * method to setup the viewPager with its adapter
     */
    private void setupViewPager() {
        OnBoardingViewPagerAdapter onBoardingViewPagerAdapter = new OnBoardingViewPagerAdapter(onBoardingPresenter);
        vp_onBoardingSlider.setAdapter(onBoardingViewPagerAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

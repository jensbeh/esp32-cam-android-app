package com.esp32camera.onBoarding;

import android.app.Activity;

import com.esp32camera.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingPresenter implements OnBoardingContract.Presenter {

    private OnBoardingActivity onBoardingActivity;

    private List<Integer> fragmentList;

    public OnBoardingPresenter(OnBoardingActivity onBoardingActivity) {
        this.onBoardingActivity = onBoardingActivity;

        fragmentList = new ArrayList<>();

        // add views
        fragmentList.add(R.layout.fragment_onboarding_1);
        fragmentList.add(R.layout.fragment_onboarding_2);
        fragmentList.add(R.layout.fragment_onboarding_3);
    }

    @Override
    public Activity getActivity() {
        return onBoardingActivity;
    }

    @Override
    public int getViewId(int position) {
        return fragmentList.get(position);
    }

    @Override
    public List<Integer> getFragmentList() {
        return fragmentList;
    }

}

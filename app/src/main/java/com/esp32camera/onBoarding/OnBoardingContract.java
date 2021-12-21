package com.esp32camera.onBoarding;

import android.app.Activity;

import java.util.List;

public interface OnBoardingContract {

    interface View { // from presenter to view

    }

    interface Model { // from presenter to model and back

    }

    interface Presenter {
        Activity getActivity();

        int getViewId(int position);

        List<Integer> getFragmentList();
    }
}

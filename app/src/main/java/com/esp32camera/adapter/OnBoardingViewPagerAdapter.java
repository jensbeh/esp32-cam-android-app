package com.esp32camera.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.esp32camera.onBoarding.OnBoardingPresenter;

public class OnBoardingViewPagerAdapter extends PagerAdapter {
    private View view;
    private OnBoardingPresenter onBoardingPresenter;

    public OnBoardingViewPagerAdapter(OnBoardingPresenter onBoardingPresenter) {
        this.onBoardingPresenter = onBoardingPresenter;
    }

    @Override
    public int getCount() {
        return onBoardingPresenter.getFragmentList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        this.view = LayoutInflater.from(onBoardingPresenter.getActivity()).inflate(onBoardingPresenter.getViewId(position), null);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}

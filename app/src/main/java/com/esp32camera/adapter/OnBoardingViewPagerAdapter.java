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

    /**
     * method gets the object as key and checks if the view/page to be shown is equals to the object, if yes then show the page
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * method loads the view and sets the view as a key for a page at specific position
     * here the view content can be load and set like pictures or texts
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        this.view = LayoutInflater.from(onBoardingPresenter.getActivity()).inflate(onBoardingPresenter.getViewId(position), null);

        container.addView(view, 0);
        return view;
    }

    /**
     * method to remove the item/view from the container where all views from the pages are saved
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}

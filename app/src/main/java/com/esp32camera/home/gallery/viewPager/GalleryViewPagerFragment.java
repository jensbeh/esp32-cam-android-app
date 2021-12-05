package com.esp32camera.home.gallery.viewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.adapter.ViewPagerAdapter;
import com.esp32camera.home.gallery.GalleryPresenter;

public class GalleryViewPagerFragment extends Fragment implements GalleryViewPagerContract.View {

    private View view;
    private MainPresenter mainPresenter;
    private GalleryViewPagerPresenter galleryViewPagerPresenter;
    private GalleryPresenter galleryPresenter;
    private ViewPager vp_gallerySlider;

    public GalleryViewPagerFragment(MainPresenter mainPresenter, GalleryViewPagerPresenter galleryViewPagerPresenter, GalleryPresenter galleryPresenter) {
        this.mainPresenter = mainPresenter;
        this.galleryViewPagerPresenter = galleryViewPagerPresenter;
        this.galleryPresenter = galleryPresenter;

        galleryViewPagerPresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery_image_slider, container, false);
        return view;
    }

    /**
     * Second method where the view is ready to use
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vp_gallerySlider = (ViewPager) view.findViewById(R.id.vp_gallerySlider);

        setupViewPager();

        setupOnListener();
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mainPresenter, galleryPresenter.getGalleryItems());
        vp_gallerySlider.setAdapter(viewPagerAdapter);
        vp_gallerySlider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerAdapter.hideMediaController();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_gallerySlider.postDelayed(new Runnable() {
            @Override
            public void run() {
                // need to run with delay, else the view position is incorrect; smoothScroll = false, because else there will be an annoying animation to that position
                vp_gallerySlider.setCurrentItem(galleryPresenter.getSelectedItem(), false);
            }
        }, 10);
    }

    private void setupOnListener() {

    }

    @Override
    public void onStop() {
        super.onStop();
        vp_gallerySlider.setAdapter(null);
        vp_gallerySlider = null;
    }
}
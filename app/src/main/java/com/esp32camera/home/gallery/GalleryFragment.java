package com.esp32camera.home.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;

public class GalleryFragment extends Fragment {

    private View view;
    private MainPresenter mainPresenter;
    private GalleryPresenter galleryPresenter;

    public GalleryFragment(MainPresenter mainPresenter, GalleryPresenter galleryPresenter) {
        this.mainPresenter = mainPresenter;
        this.galleryPresenter = galleryPresenter;

        galleryPresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        return view;
    }

    /**
     * Second method where the view is ready to use
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOnListener();
    }

    private void setupOnListener() {

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
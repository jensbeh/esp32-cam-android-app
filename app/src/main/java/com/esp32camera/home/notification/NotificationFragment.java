package com.esp32camera.home.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;

public class NotificationFragment extends Fragment {

    private View view;
    private MainPresenter mainPresenter;
    private NotificationPresenter notificationPresenter;

    public NotificationFragment(MainPresenter mainPresenter, NotificationPresenter notificationPresenter) {
        this.mainPresenter = mainPresenter;
        this.notificationPresenter = notificationPresenter;

        notificationPresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
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
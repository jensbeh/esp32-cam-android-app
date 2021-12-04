package com.esp32camera;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.esp32camera.camSettings.CamSettingsFragment;
import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.home.HomeFragment;
import com.esp32camera.home.HomePresenter;
import com.esp32camera.home.gallery.GalleryFragment;
import com.esp32camera.home.gallery.GalleryPresenter;
import com.esp32camera.home.notification.NotificationFragment;
import com.esp32camera.home.notification.NotificationPresenter;
import com.esp32camera.model.CameraCard;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView bottomNavigationView_home;

    private MainPresenter mainPresenter;
    private HomePresenter homePresenter;
    private GalleryPresenter galleryPresenter;
    private NotificationPresenter notificationPresenter;
    private CamSettingsPresenter camSettingsPresenter;

    private HomeFragment homeFragment;
    private GalleryFragment galleryFragment;
    private NotificationFragment notificationFragment;
    private CamSettingsFragment camSettingsFragment;

    public enum State {
        HomeFragment,
        GalleryFragment,
        NotificationFragment,
        CamSettingsFragment
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation View
        bottomNavigationView_home = findViewById(R.id.bottomNavigationView_home);
        bottomNavigationView_home.setSelectedItemId(R.id.nav_item_home);
        setupNavigationViewListener();

        // Setup Presenter
        homePresenter = new HomePresenter(this);
        galleryPresenter = new GalleryPresenter(this);
        notificationPresenter = new NotificationPresenter(this);
        camSettingsPresenter = new CamSettingsPresenter(this);
        mainPresenter = new MainPresenter(this, homePresenter, camSettingsPresenter);

        // Setup Fragments
        homeFragment = new HomeFragment(mainPresenter, homePresenter);
        galleryFragment = new GalleryFragment(mainPresenter, galleryPresenter);
        notificationFragment = new NotificationFragment(mainPresenter, notificationPresenter);
        camSettingsFragment = new CamSettingsFragment(mainPresenter, camSettingsPresenter);

        // set homeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homeFragment).commit();
        }
    }

    private void setupNavigationViewListener() {
        bottomNavigationView_home.setOnNavigationItemSelectedListener(item -> {
            mainPresenter.changeToSelectedFragment(item);
            return true;
        });
    }

    @Override
    public void navigateToCamSettingsFragment() {
        bottomNavigationView_home.setVisibility(View.GONE);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, camSettingsFragment)
                .commit();
    }

    @Override
    public void navigateToHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToGalleryFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, galleryFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToNotificationFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, notificationFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    /**
     * handle the android back pressed button
     */
    @Override
    public void onBackPressed() {
        if (mainPresenter.getViewState().equals(State.HomeFragment)) {
            // Close App

        } else if (mainPresenter.getViewState().equals(State.GalleryFragment)) {
            // go back to HomeFragment
            mainPresenter.navigateToHomeFragment();
        } else if (mainPresenter.getViewState().equals(State.NotificationFragment)) {
            // go back to HomeFragment
            mainPresenter.navigateToHomeFragment();
        } else if (mainPresenter.getViewState().equals(State.CamSettingsFragment)) {
            // go back to HomeFragment
            mainPresenter.navigateToHomeFragment();
        } else {
            super.onBackPressed();
        }
    }
}
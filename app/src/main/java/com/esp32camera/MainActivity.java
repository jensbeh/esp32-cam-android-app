package com.esp32camera;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.esp32camera.camSettings.CamSettingsFragment;
import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.home.HomeFragment;
import com.esp32camera.home.HomePresenter;
import com.esp32camera.home.gallery.GalleryFragment;
import com.esp32camera.home.gallery.GalleryPresenter;
import com.esp32camera.home.gallery.viewPager.GalleryViewPagerFragment;
import com.esp32camera.home.gallery.viewPager.GalleryViewPagerPresenter;
import com.esp32camera.home.notification.NotificationFragment;
import com.esp32camera.home.notification.NotificationPresenter;
import com.esp32camera.util.NotificationHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// bottom navigation view - https://www.youtube.com/watch?v=UETiPd3zR2E&list=PL3m9bHmTF9ihvR-VcvAGSaAxjhZC_PZpP
// animation between fragments - https://gist.github.com/codinginflow/a2b08fb50b0971923176a4e0c062971a && https://developer.android.com/guide/fragments/animate

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private BottomNavigationView bottomNavigationView_home;

    private MainPresenter mainPresenter;
    private HomePresenter homePresenter;
    private GalleryPresenter galleryPresenter;
    private GalleryViewPagerPresenter galleryViewPagerPresenter;
    private NotificationPresenter notificationPresenter;
    private CamSettingsPresenter camSettingsPresenter;

    private HomeFragment homeFragment;
    private GalleryFragment galleryFragment;
    private GalleryViewPagerFragment galleryViewPagerFragment;
    private NotificationFragment notificationFragment;
    private CamSettingsFragment camSettingsFragment;

    public enum State {
        StartUp,
        HomeFragment,
        GalleryFragment,
        GalleryViewPagerFragment,
        NotificationFragment,
        CamSettingsFragment;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String fragmentActionFromIntent = intent.getStringExtra("FRAGMENT");

        if (fragmentActionFromIntent == null) {
            // set homeFragment
            bottomNavigationView_home.setSelectedItemId(R.id.nav_item_home);
        } else if (fragmentActionFromIntent.equals("NOTIFICATION")) {
            bottomNavigationView_home.setSelectedItemId(R.id.nav_item_notifications);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String fragmentActionFromIntent = getIntent().getStringExtra("FRAGMENT");

        // set network policy, because of some "android.os.NetworkOnMainThreadException" error
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // request permissions - https://stackoverflow.com/questions/33666071/android-marshmallow-request-permission
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);

        // Bottom Navigation View
        bottomNavigationView_home = findViewById(R.id.bottomNavigationView_home);
        bottomNavigationView_home.setSelectedItemId(R.id.nav_item_home);
        setupNavigationViewListener();

        // Setup Notification Handler
        NotificationHandler notificationHandler = new NotificationHandler(this);

        // Setup Presenter
        homePresenter = new HomePresenter(this);
        galleryPresenter = new GalleryPresenter(this);
        galleryViewPagerPresenter = new GalleryViewPagerPresenter(this);
        notificationPresenter = new NotificationPresenter(this);
        camSettingsPresenter = new CamSettingsPresenter(this);
        mainPresenter = new MainPresenter(this, homePresenter, camSettingsPresenter, notificationPresenter, notificationHandler);

        // Setup Fragments
        homeFragment = new HomeFragment(mainPresenter, homePresenter);
        galleryFragment = new GalleryFragment(mainPresenter, galleryPresenter);
        galleryViewPagerFragment = new GalleryViewPagerFragment(mainPresenter, galleryViewPagerPresenter, galleryPresenter);
        notificationFragment = new NotificationFragment(mainPresenter, notificationPresenter);
        camSettingsFragment = new CamSettingsFragment(mainPresenter, camSettingsPresenter);


        // load all stored EspCameras
        mainPresenter.loadEspCameras();

        // load all stored Notifications
        mainPresenter.loadNotifications();

        // set start-fragment
        if (fragmentActionFromIntent == null) {
            // default: set homeFragment on normal app start
            bottomNavigationView_home.setSelectedItemId(R.id.nav_item_home);
        } else if (fragmentActionFromIntent.equals("NOTIFICATION")) {
            // when clicked on notification: set notificationFragment on app start
            bottomNavigationView_home.setSelectedItemId(R.id.nav_item_notifications);
        }
    }

    /**
     * method/listener to listen to bottomNavigationView changes and change fragments
     * listener called with .setSelectedItemId(...)
     */
    private void setupNavigationViewListener() {
        bottomNavigationView_home.setOnNavigationItemSelectedListener(item -> {
            mainPresenter.changeToSelectedFragment(item);
            return true;
        });
    }

    /**
     * method to navigate to CamSettingsFragment
     */
    @Override
    public void navigateToCamSettingsFragment() {
        bottomNavigationView_home.setVisibility(View.GONE);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, camSettingsFragment)
                .commit();
    }

    /**
     * method to navigate to GalleryViewPagerFragment
     */
    @Override
    public void navigateToGalleryViewPagerFragment() {
        bottomNavigationView_home.setVisibility(View.GONE);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, galleryViewPagerFragment)
                .commit();
    }

    /**
     * method to navigate to HomeFragment
     */
    @Override
    public void navigateToHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    /**
     * method to navigate to HomeFragment with custom animation
     */
    @Override
    public void navigateToHomeFragmentWithAnim(int enter, int exit) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .setCustomAnimations(enter, exit)
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    /**
     * method to navigate to GalleryFragment
     */
    @Override
    public void navigateToGalleryFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, galleryFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    /**
     * method to navigate to GalleryFragment with custom animation
     */
    @Override
    public void navigateToGalleryFragmentWithAnim(int enter, int exit) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .setCustomAnimations(enter, exit)
                .replace(R.id.fragment_container, galleryFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    /**
     * method to navigate to NotificationFragment
     */
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
     * method to navigate to NotificationFragment with custom animation
     */
    @Override
    public void navigateToNotificationFragmentWithAnim(int enter, int exit) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .setCustomAnimations(enter, exit)
                .replace(R.id.fragment_container, notificationFragment)
                .commit();

        bottomNavigationView_home.setVisibility(View.VISIBLE);
    }

    /**
     * handle the android back pressed button
     */
    @Override
    public void onBackPressed() {
        // GalleryViewPagerFragment -> GalleryFragment
        if (mainPresenter.getViewState().equals(State.GalleryViewPagerFragment)) {
            // go back to GalleryFragment
            mainPresenter.navigateToGalleryFragment();
        }
        // CamSettingsFragment -> HomeFragment
        else if (mainPresenter.getViewState().equals(State.CamSettingsFragment)) {
            // go back to HomeFragment
            mainPresenter.navigateToHomeFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }
}
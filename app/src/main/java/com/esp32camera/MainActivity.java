package com.esp32camera;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.esp32camera.camSettings.CamSettingsFragment;
import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.home.HomeFragment;
import com.esp32camera.home.HomePresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private HomeFragment homeFragment;
    private CamSettingsFragment camSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Presenter
        HomePresenter homePresenter = new HomePresenter(this);
        CamSettingsPresenter camSettingsPresenter = new CamSettingsPresenter(this);
        MainPresenter mainPresenter = new MainPresenter(this, camSettingsPresenter);

        // Setup Fragments
        homeFragment = new HomeFragment(mainPresenter, homePresenter);
        camSettingsFragment = new CamSettingsFragment(mainPresenter, camSettingsPresenter);

        // set homeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    homeFragment).commit();
        }
    }

    @Override
    public void navigateToSettingsFragment() {
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
    }
}
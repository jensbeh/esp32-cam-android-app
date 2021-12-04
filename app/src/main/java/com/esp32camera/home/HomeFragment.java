package com.esp32camera.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.bottomSheets.BottomSheetAddEspCamera;
import com.esp32camera.model.CameraCard;

public class HomeFragment extends Fragment implements HomeContract.View {

    private View view;

    private final MainPresenter mainPresenter;
    private final HomePresenter homePresenter;

    private LinearLayout ll_scroll_camera_cards;
    private Toolbar toolbarHome;

    public HomeFragment(MainPresenter mainPresenter, HomePresenter homePresenter) {
        this.mainPresenter = mainPresenter;
        this.homePresenter = homePresenter;

        this.homePresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    /**
     * Second method where the view is ready to use
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ll_scroll_camera_cards = view.findViewById(R.id.ll_scroll_camera_cards);
        toolbarHome = view.findViewById(R.id.toolbar_home);

        setupOnListener();

        loadCameraCards();
    }

    private void loadCameraCards() {
        for (CameraCard cameraCard : mainPresenter.getCameraCardMap().values()) {
            homePresenter.addNewCameraCard(cameraCard);
        }
    }

    private void setupOnListener() {
        toolbarHome.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_add_camera) {
                BottomSheetAddEspCamera bottomSheetAddEspCamera = new BottomSheetAddEspCamera(mainPresenter.getActivity(), R.style.BottomSheetDialogTheme, mainPresenter);
                bottomSheetAddEspCamera.show();
            }
            return true;
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        // kill webView to kill the stream
//        webViewStream.destroy();
    }

    @Override
    public void addNewCameraCard(CameraCard cameraCard) {
        if (cameraCard.getView().getParent() != null) {
            ((LinearLayout) cameraCard.getView().getParent()).removeView(cameraCard.getView());
        }
        ll_scroll_camera_cards.addView(cameraCard.getView());
    }

    @Override
    public void removeCameraCard(CameraCard cameraCard) {
        ll_scroll_camera_cards.removeView(cameraCard.getView());
    }
}
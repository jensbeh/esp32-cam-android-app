package com.esp32camera;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.widget.Toast;

import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.home.HomePresenter;
import com.esp32camera.model.CameraCard;
import com.esp32camera.model.EspCamera;
import com.esp32camera.net.WebSocketService;
import com.esp32camera.net.WebSocketServiceInterface;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPresenter implements MainContract.Presenter {

    private MainActivity mainActivity;
    private MainActivity.State viewState;
    private HomePresenter homePresenter;
    private CamSettingsPresenter camSettingsPresenter;
    private Map<String, WebSocketService> webSocketServiceMap;
    private Map<String, EspCamera> espCameraMap;
    private Map<String, CameraCard> cameraCardMap;

    public MainPresenter(MainActivity mainActivity, HomePresenter homePresenter, CamSettingsPresenter camSettingsPresenter) {
        this.mainActivity = mainActivity;
        this.homePresenter = homePresenter;
        this.camSettingsPresenter = camSettingsPresenter;

        viewState = MainActivity.State.StartUp;
        webSocketServiceMap = new HashMap<>();
        espCameraMap = new HashMap<>();
        cameraCardMap = new HashMap<>();
    }

    @Override
    public void changeToSelectedFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_gallery:
                viewState = MainActivity.State.GalleryFragment;
                mainActivity.navigateToGalleryFragment();
                break;
            case R.id.nav_item_home:
                viewState = MainActivity.State.HomeFragment;
                mainActivity.navigateToHomeFragment();
                break;
            case R.id.nav_item_notifications:
                viewState = MainActivity.State.NotificationFragment;
                mainActivity.navigateToNotificationFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void setupNewEspCamera(String ipAddress, String name) {
        Toast.makeText(mainActivity, ipAddress, Toast.LENGTH_SHORT).show();

        // create new EspCamera
        EspCamera newEspCamera = new EspCamera(ipAddress, name);
        espCameraMap.put(ipAddress, newEspCamera);

        // create and add new cardView
        CameraCard cameraCard = new CameraCard(this, newEspCamera);
        if (viewState != MainActivity.State.StartUp) { // if app is started view is not visible - only when a new espCamera is manually creating it will be directly added
            homePresenter.addNewCameraCard(cameraCard);
        }
        cameraCardMap.put(ipAddress, cameraCard);

        // connect to WebSocket
        // here i can show error view of the specific webView
        WebSocketService webSocketService = new WebSocketService(this, newEspCamera, camSettingsPresenter, new WebSocketServiceInterface() {
            @Override
            public void OnConnectionOpened(EspCamera espCamera, String status) {
                if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) {
                    camSettingsPresenter.onWebSocketConnectionOpened();
                }
            }

            @Override
            public void OnConnectionClosed(EspCamera espCamera, String status) {
                // here i can show error view of the specific webView
                if (viewState.equals(MainActivity.State.HomeFragment)) {
                    cameraCardMap.get(espCamera.getIpAddress()).onWebSocketConnectionClosed();
                } else if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) {
                    camSettingsPresenter.onWebSocketConnectionClosed();
                }
            }

            @Override
            public void OnConnectionFailed(EspCamera espCamera, String status) {
            }
        });
        webSocketService.startWebSocketService();
        webSocketServiceMap.put(ipAddress, webSocketService);
    }

    @Override
    public void setCameraName(String newName) {
        if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() != null) {
            // set cameraName to model
            camSettingsPresenter.getEspCamera().setName(newName);

            cameraCardMap.get(camSettingsPresenter.getEspCamera().getIpAddress()).setCameraName(newName);

            camSettingsPresenter.setCameraName(newName);
        }
    }

    @Override
    public void setCameraFramesize(EspCamera espCamera, int framesize) {
        if (espCamera.getFramesize() != framesize) {
            espCamera.setFramesize(framesize); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraFramesize(framesize);
            }
        }
    }

    @Override
    public void setCameraQuality(EspCamera espCamera, int quality) {
        if (espCamera.getQuality() != quality) {
            espCamera.setQuality(quality); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraQuality(quality);
            }
        }
    }

    @Override
    public void setCameraBrightness(EspCamera espCamera, int brightness) {
        if (espCamera.getBrightness() != brightness) {
            espCamera.setBrightness(brightness); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraBrightness(brightness);
            }
        }
    }

    @Override
    public void setCameraContrast(EspCamera espCamera, int contrast) {
        if (espCamera.getContrast() != contrast) {
            espCamera.setContrast(contrast); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraContrast(contrast);
            }
        }
    }

    @Override
    public void setCameraSaturation(EspCamera espCamera, int saturation) {
        if (espCamera.getSaturation() != saturation) {
            espCamera.setSaturation(saturation); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraSaturation(saturation);
            }
        }
    }

    @Override
    public void setCameraSpecialEffect(EspCamera espCamera, int specialEffect) {
        if (espCamera.getSpecialEffect() != specialEffect) {
            espCamera.setSpecialEffect(specialEffect); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraSpecialEffect(specialEffect);
            }
        }
    }

    @Override
    public void setCameraAutoWhiteBalanceState(EspCamera espCamera, int autoWhiteBalanceState) {
        if (espCamera.getAutoWhiteBalanceState() != autoWhiteBalanceState) {
            espCamera.setAutoWhiteBalanceState(autoWhiteBalanceState); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAutoWhiteBalanceState(autoWhiteBalanceState);
            }
        }
    }

    @Override
    public void setCameraAutoWbGain(EspCamera espCamera, int autoWbGain) {
        if (espCamera.getAutoWbGain() != autoWbGain) {
            espCamera.setAutoWbGain(autoWbGain); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAutoWbGain(autoWbGain);
            }
        }
    }

    @Override
    public void setCameraWbMode(EspCamera espCamera, int wbMode) {
        if (espCamera.getWbMode() != wbMode) {
            espCamera.setWbMode(wbMode); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraWbMode(wbMode);
            }
        }
    }

    @Override
    public void setCameraExposureCtrlState(EspCamera espCamera, int exposureCtrlState) {
        if (espCamera.getExposureCtrlState() != exposureCtrlState) {
            espCamera.setExposureCtrlState(exposureCtrlState); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraExposureCtrlState(exposureCtrlState);
            }
        }
    }

    @Override
    public void setCameraAecValue(EspCamera espCamera, int aecValue) {
        if (espCamera.getAecValue() != aecValue) {
            espCamera.setAecValue(aecValue); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAecValue(aecValue);
            }
        }
    }

    @Override
    public void setCameraAec2(EspCamera espCamera, int aec2) {
        if (espCamera.getAec2() != aec2) {
            espCamera.setAec2(aec2); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAec2(aec2);
            }
        }
    }

    @Override
    public void setCameraAeLevel(EspCamera espCamera, int aeLevel) {
        if (espCamera.getAeLevel() != aeLevel) {
            espCamera.setAeLevel(aeLevel); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAeLevel(aeLevel);
            }
        }
    }

    @Override
    public void setCameraAgcCtrlState(EspCamera espCamera, int agcCtrlState) {
        if (espCamera.getAgcCtrlState() != agcCtrlState) {
            espCamera.setAgcCtrlState(agcCtrlState); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAgcCtrlState(agcCtrlState);
            }
        }
    }

    @Override
    public void setCameraAgcGain(EspCamera espCamera, int agcGain) {
        if (espCamera.getAgcGain() != agcGain) {
            espCamera.setAgcGain(agcGain); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAgcGain(agcGain);
            }
        }
    }

    @Override
    public void setCameraGainCeiling(EspCamera espCamera, int gainCeiling) {
        if (espCamera.getGainCeiling() != gainCeiling) {
            espCamera.setGainCeiling(gainCeiling); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraGainCeiling(gainCeiling);
            }
        }
    }

    @Override
    public void setCameraBpc(EspCamera espCamera, int bpc) {
        if (espCamera.getBpc() != bpc) {
            espCamera.setBpc(bpc); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraBpc(bpc);
            }
        }
    }

    @Override
    public void setCameraWpc(EspCamera espCamera, int wpc) {
        if (espCamera.getWpc() != wpc) {
            espCamera.setWpc(wpc); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraWpc(wpc);
            }
        }
    }

    @Override
    public void setCameraRawGma(EspCamera espCamera, int rawGma) {
        if (espCamera.getRawGma() != rawGma) {
            espCamera.setRawGma(rawGma); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraRawGma(rawGma);
            }
        }
    }

    @Override
    public void setCameraLenc(EspCamera espCamera, int lenc) {
        if (espCamera.getLenc() != lenc) {
            espCamera.setLenc(lenc); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraLenc(lenc);
            }
        }
    }

    @Override
    public void setCameraHmirror(EspCamera espCamera, int hMirror) {
        if (espCamera.getHmirror() != hMirror) {
            espCamera.setHmirror(hMirror); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraHmirror(hMirror);
            }
        }
    }

    @Override
    public void setCameraVflip(EspCamera espCamera, int vFlip) {
        if (espCamera.getVflip() != vFlip) {
            espCamera.setVflip(vFlip); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraVflip(vFlip);
            }
        }
    }

    @Override
    public void setCameraColorbar(EspCamera espCamera, int colorbar) {
        if (espCamera.getColorbar() != colorbar) {
            espCamera.setColorbar(colorbar); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraColorbar(colorbar);
            }
        }
    }

    @Override
    public Map<String, CameraCard> getCameraCardMap() {
        return cameraCardMap;
    }

    @Override
    public boolean ifCameraExisting(String ipAddress) {
        return espCameraMap.containsKey(ipAddress);
    }

    @Override
    public void resetCameraValues(EspCamera espCamera) {

    }

    @Override
    public void removeCamera(EspCamera espCamera) {
        // close and remove webSocket
        webSocketServiceMap.get(espCamera.getIpAddress()).close();
        webSocketServiceMap.remove(espCamera.getIpAddress());

        // stop and remove cameraCard
        if (viewState == MainActivity.State.HomeFragment) {
            cameraCardMap.get(espCamera.getIpAddress()).stop();
            homePresenter.removeCameraCard(cameraCardMap.get(espCamera.getIpAddress()));
        }
        cameraCardMap.remove(espCamera.getIpAddress());

        // remove espCamera
        espCameraMap.remove(espCamera.getIpAddress());

        // save all available cameras
        saveEspCameras();
    }

    /**
     * loads the data from sharedPreferences
     */
    @Override
    public void loadEspCameras() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("EspCameras", null);
        Type type = new TypeToken<ArrayList<EspCamera>>() {
        }.getType();
        ArrayList<EspCamera> mExampleList = gson.fromJson(json, type);
        if (mExampleList != null) {
            for (EspCamera espCamera : mExampleList) {
                setupNewEspCamera(espCamera.getIpAddress(), espCamera.getName());
            }
        }
    }

    /**
     * saves the data to sharedPreferences
     */
    @Override
    public void saveEspCameras() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        ArrayList<EspCamera> espCamerasArrayList = new ArrayList<>();
        for (EspCamera espCamera : espCameraMap.values()) {
            EspCamera newEspCamera = new EspCamera(espCamera.getIpAddress(), espCamera.getName());
            espCamerasArrayList.add(newEspCamera);
        }
        String json = gson.toJson(espCamerasArrayList);
        editor.putString("EspCameras", json);
        editor.apply();
    }


    @Override
    public void navigateToCamSettingsFragment(EspCamera espCamera) {
        viewState = MainActivity.State.CamSettingsFragment;

        camSettingsPresenter.setSelectedEspCamera(espCamera);

        mainActivity.navigateToCamSettingsFragment();
    }

    @Override
    public void navigateToHomeFragment() {
        viewState = MainActivity.State.HomeFragment;
        mainActivity.navigateToHomeFragment();
    }


    public MainActivity.State getViewState() {
        return viewState;
    }

    @Override
    public Activity getActivity() {
        return mainActivity;
    }

    @Override
    public void sendWebSocketMessage(String message) {
        webSocketServiceMap.get(camSettingsPresenter.getEspCamera().getIpAddress()).sendMessage(message);
    }

    @Override
    public boolean isWebSocketConnected() {
        return webSocketServiceMap.get(camSettingsPresenter.getEspCamera().getIpAddress()).isWebSocketConnected();
    }
}

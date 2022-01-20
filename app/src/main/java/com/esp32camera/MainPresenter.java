package com.esp32camera;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.MenuItem;

import com.esp32camera.camSettings.CamSettingsPresenter;
import com.esp32camera.home.HomePresenter;
import com.esp32camera.home.notification.NotificationPresenter;
import com.esp32camera.model.CameraCard;
import com.esp32camera.model.EspCamera;
import com.esp32camera.model.Notification;
import com.esp32camera.net.WebSocketService;
import com.esp32camera.net.WebSocketServiceInterface;
import com.esp32camera.util.CreateFile;
import com.esp32camera.util.NotificationHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPresenter implements MainContract.Presenter {

    private MainActivity mainActivity;
    private MainActivity.State viewState;
    private HomePresenter homePresenter;
    private CamSettingsPresenter camSettingsPresenter;
    private NotificationPresenter notificationPresenter;
    private NotificationHandler notificationHandler;
    private Map<String, EspCamera> espCameraMap;
    private Map<String, CameraCard> cameraCardMap;
    private List<Notification> notificationList;
    private int openedWebSocketCount;
    private WebSocketService webSocketService;

    public MainPresenter(MainActivity mainActivity, HomePresenter homePresenter, CamSettingsPresenter camSettingsPresenter, NotificationPresenter notificationPresenter, NotificationHandler notificationHandler) {
        this.mainActivity = mainActivity;
        this.homePresenter = homePresenter;
        this.camSettingsPresenter = camSettingsPresenter;
        this.notificationPresenter = notificationPresenter;
        this.notificationHandler = notificationHandler;

        viewState = MainActivity.State.StartUp;
        espCameraMap = new HashMap<>();
        cameraCardMap = new HashMap<>();
        notificationList = new ArrayList<>();
        openedWebSocketCount = 0;
    }

    /**
     * method to change and handle the bottomNavigationView with fragments
     */
    @Override
    public void changeToSelectedFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_gallery:
                if (viewState != MainActivity.State.GalleryFragment) {
                    viewState = MainActivity.State.GalleryFragment;
                    mainActivity.navigateToGalleryFragmentWithAnim(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
            case R.id.nav_item_home:
                if (viewState != MainActivity.State.HomeFragment) {
                    // from gallery fragment
                    if (viewState == MainActivity.State.GalleryFragment) {
                        viewState = MainActivity.State.HomeFragment;
                        mainActivity.navigateToHomeFragmentWithAnim(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    // from notification fragment
                    else if (viewState == MainActivity.State.NotificationFragment) {
                        viewState = MainActivity.State.HomeFragment;
                        mainActivity.navigateToHomeFragmentWithAnim(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    // at start up
                    else if (viewState == MainActivity.State.StartUp) {
                        viewState = MainActivity.State.HomeFragment;
                        mainActivity.navigateToHomeFragment();
                    }
                }
                break;
            case R.id.nav_item_notifications:
                if (viewState != MainActivity.State.NotificationFragment) {
                    viewState = MainActivity.State.NotificationFragment;
                    mainActivity.navigateToNotificationFragmentWithAnim(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    notificationPresenter.scrollToTop();
                }
                break;
            default:
                break;
        }
    }

    /**
     * method to create a new EspCamera object and save it
     * starts the webSocketService in case it isn't running already and handles webSocket actions
     * updates the webSocket in case the app was restarted (service contains old instances of objects, because on restart app creates new instances of e.g. espCameras)
     */
    @Override
    public void setupNewEspCamera(String ipAddress, String name) {
        // create new EspCamera
        EspCamera newEspCamera = new EspCamera(ipAddress, name);
        espCameraMap.put(ipAddress, newEspCamera);

        // create/connect to webSocketService
        // here i can show error view of the specific webView when webSocket has error
        if (webSocketService == null) {
            webSocketService = new WebSocketService(this, new WebSocketServiceInterface() {
                @Override
                public void OnConnectionOpened(EspCamera espCamera, String status) {
                    if (viewState.equals(MainActivity.State.HomeFragment)) {
                        cameraCardMap.get(espCamera.getIpAddress()).onWebSocketConnectionOpened();
                    } else if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) {
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

                @Override
                public void OnServiceConnected() {
                    // create and start webSocket
                    if (!webSocketService.webSocketAlreadyExisting(newEspCamera)) {
                        webSocketService.createWebSocket(newEspCamera, getThisMainPresenter());
                    } else {
                        webSocketService.updateWebSocketForegroundService(newEspCamera, getThisMainPresenter());
                    }
                }
            });
        } else {
            // create and start webSocket
            if (!webSocketService.webSocketAlreadyExisting(newEspCamera)) {
                webSocketService.createWebSocket(newEspCamera, getThisMainPresenter());
            } else {
                webSocketService.updateWebSocketForegroundService(newEspCamera, getThisMainPresenter());
            }
        }


        // create and show new Camera Card
        createNewCameraCard(newEspCamera);
    }

    /**
     * method to create and show a new cameraCard with the newEspCamera
     */
    private void createNewCameraCard(EspCamera newEspCamera) {
        // create and add new cardView
        CameraCard cameraCard = new CameraCard(this, newEspCamera);
        if (viewState != MainActivity.State.StartUp) { // if app is started view is not visible - only when a new espCamera is manually creating it will be directly added
            homePresenter.addNewCameraCard(cameraCard);
        }
        cameraCardMap.put(newEspCamera.getIpAddress(), cameraCard);
    }

    /**
     * method to save name to espCamera
     */
    @Override
    public void setCameraName(String newName) {
        if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() != null) {
            // set cameraName to model
            camSettingsPresenter.getEspCamera().setName(newName);

            cameraCardMap.get(camSettingsPresenter.getEspCamera().getIpAddress()).setCameraName(newName);

            camSettingsPresenter.setCameraName(newName);
        }
    }

    /**
     * method to save framesize to espCamera
     */
    @Override
    public void setCameraFramesize(EspCamera espCamera, int framesize) {
        if (espCamera.getFramesize() != framesize) {
            espCamera.setFramesize(framesize); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraFramesize(framesize);
            }
        }
    }

    /**
     * method to save quality to espCamera
     */
    @Override
    public void setCameraQuality(EspCamera espCamera, int quality) {
        if (espCamera.getQuality() != quality) {
            espCamera.setQuality(quality); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraQuality(quality);
            }
        }
    }

    /**
     * method to save brightness to espCamera
     */
    @Override
    public void setCameraBrightness(EspCamera espCamera, int brightness) {
        if (espCamera.getBrightness() != brightness) {
            espCamera.setBrightness(brightness); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraBrightness(brightness);
            }
        }
    }

    /**
     * method to save contrast to espCamera
     */
    @Override
    public void setCameraContrast(EspCamera espCamera, int contrast) {
        if (espCamera.getContrast() != contrast) {
            espCamera.setContrast(contrast); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraContrast(contrast);
            }
        }
    }

    /**
     * method to save saturation to espCamera
     */
    @Override
    public void setCameraSaturation(EspCamera espCamera, int saturation) {
        if (espCamera.getSaturation() != saturation) {
            espCamera.setSaturation(saturation); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraSaturation(saturation);
            }
        }
    }

    /**
     * method to save specialEffect to espCamera
     */
    @Override
    public void setCameraSpecialEffect(EspCamera espCamera, int specialEffect) {
        if (espCamera.getSpecialEffect() != specialEffect) {
            espCamera.setSpecialEffect(specialEffect); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraSpecialEffect(specialEffect);
            }
        }
    }

    /**
     * method to save autoWhiteBalanceState to espCamera
     */
    @Override
    public void setCameraAutoWhiteBalanceState(EspCamera espCamera, int autoWhiteBalanceState) {
        if (espCamera.getAutoWhiteBalanceState() != autoWhiteBalanceState) {
            espCamera.setAutoWhiteBalanceState(autoWhiteBalanceState); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAutoWhiteBalanceState(autoWhiteBalanceState);
            }
        }
    }

    /**
     * method to save autoWbGainState to espCamera
     */
    @Override
    public void setCameraAutoWbGain(EspCamera espCamera, int autoWbGain) {
        if (espCamera.getAutoWbGain() != autoWbGain) {
            espCamera.setAutoWbGain(autoWbGain); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAutoWbGain(autoWbGain);
            }
        }
    }

    /**
     * method to save wbMode to espCamera
     */
    @Override
    public void setCameraWbMode(EspCamera espCamera, int wbMode) {
        if (espCamera.getWbMode() != wbMode) {
            espCamera.setWbMode(wbMode); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraWbMode(wbMode);
            }
        }
    }

    /**
     * method to save exposureCtrlState to espCamera
     */
    @Override
    public void setCameraExposureCtrlState(EspCamera espCamera, int exposureCtrlState) {
        if (espCamera.getExposureCtrlState() != exposureCtrlState) {
            espCamera.setExposureCtrlState(exposureCtrlState); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraExposureCtrlState(exposureCtrlState);
            }
        }
    }

    /**
     * method to save aecValue to espCamera
     */
    @Override
    public void setCameraAecValue(EspCamera espCamera, int aecValue) {
        if (espCamera.getAecValue() != aecValue) {
            espCamera.setAecValue(aecValue); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAecValue(aecValue);
            }
        }
    }

    /**
     * method to save aec2State to espCamera
     */
    @Override
    public void setCameraAec2(EspCamera espCamera, int aec2) {
        if (espCamera.getAec2() != aec2) {
            espCamera.setAec2(aec2); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAec2(aec2);
            }
        }
    }

    /**
     * method to save aeLevel to espCamera
     */
    @Override
    public void setCameraAeLevel(EspCamera espCamera, int aeLevel) {
        if (espCamera.getAeLevel() != aeLevel) {
            espCamera.setAeLevel(aeLevel); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAeLevel(aeLevel);
            }
        }
    }

    /**
     * method to save agcCtrlState to espCamera
     */
    @Override
    public void setCameraAgcCtrlState(EspCamera espCamera, int agcCtrlState) {
        if (espCamera.getAgcCtrlState() != agcCtrlState) {
            espCamera.setAgcCtrlState(agcCtrlState); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAgcCtrlState(agcCtrlState);
            }
        }
    }

    /**
     * method to save agcGain to espCamera
     */
    @Override
    public void setCameraAgcGain(EspCamera espCamera, int agcGain) {
        if (espCamera.getAgcGain() != agcGain) {
            espCamera.setAgcGain(agcGain); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraAgcGain(agcGain);
            }
        }
    }

    /**
     * method to save gainCeiling to espCamera
     */
    @Override
    public void setCameraGainCeiling(EspCamera espCamera, int gainCeiling) {
        if (espCamera.getGainCeiling() != gainCeiling) {
            espCamera.setGainCeiling(gainCeiling); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraGainCeiling(gainCeiling);
            }
        }
    }

    /**
     * method to save bpcState to espCamera
     */
    @Override
    public void setCameraBpc(EspCamera espCamera, int bpc) {
        if (espCamera.getBpc() != bpc) {
            espCamera.setBpc(bpc); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraBpc(bpc);
            }
        }
    }

    /**
     * method to save wpcState to espCamera
     */
    @Override
    public void setCameraWpc(EspCamera espCamera, int wpc) {
        if (espCamera.getWpc() != wpc) {
            espCamera.setWpc(wpc); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraWpc(wpc);
            }
        }
    }

    /**
     * method to save rawGmaState to espCamera
     */
    @Override
    public void setCameraRawGma(EspCamera espCamera, int rawGma) {
        if (espCamera.getRawGma() != rawGma) {
            espCamera.setRawGma(rawGma); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraRawGma(rawGma);
            }
        }
    }

    /**
     * method to save lencState to espCamera
     */
    @Override
    public void setCameraLenc(EspCamera espCamera, int lenc) {
        if (espCamera.getLenc() != lenc) {
            espCamera.setLenc(lenc); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraLenc(lenc);
            }
        }
    }

    /**
     * method to save hMirrorState to espCamera
     */
    @Override
    public void setCameraHmirror(EspCamera espCamera, int hMirror) {
        if (espCamera.getHmirror() != hMirror) {
            espCamera.setHmirror(hMirror); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraHmirror(hMirror);
            }
        }
    }

    /**
     * method to save vFlipState to espCamera
     */
    @Override
    public void setCameraVflip(EspCamera espCamera, int vFlip) {
        if (espCamera.getVflip() != vFlip) {
            espCamera.setVflip(vFlip); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraVflip(vFlip);
            }
        }
    }

    /**
     * method to save colobarState to espCamera
     */
    @Override
    public void setCameraColorbar(EspCamera espCamera, int colorbar) {
        if (espCamera.getColorbar() != colorbar) {
            espCamera.setColorbar(colorbar); // set new value to camera
            if (viewState.equals(MainActivity.State.CamSettingsFragment) && camSettingsPresenter.getEspCamera() == espCamera) { // set visible action to camSettingsFragment when settings of this espCamera is shown
                camSettingsPresenter.setCameraColorbar(colorbar);
            }
        }
    }

    /**
     * method to save flashlightState to espCamera
     */
    @Override
    public void setCameraFlashlight(EspCamera espCamera, int flashlightState) {
        if (espCamera.getFlashlightState() != flashlightState) {
            espCamera.setFlashlightState(flashlightState); // set new value to camera
            if (viewState.equals(MainActivity.State.HomeFragment)) { // set visible action to cameraCard when homeFragment is shown
                cameraCardMap.get(espCamera.getIpAddress()).setCameraFlashlight(flashlightState);
            }
        }
    }

    /**
     * method to create a notification on detection
     */
    @Override
    public void notifyOnMotionDetected(EspCamera espCamera) {
        Notification notification = new Notification(espCamera.getName(), espCamera.getIpAddress());
        notificationList.add(notification);
    }

    /**
     * method to refresh the current notification with picture data
     */
    @Override
    public void notifyOnMotionDetectedPictureData(EspCamera espCamera, byte[] pictureData) {
        Bitmap bmp = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
        for (int i = notificationList.size() - 1; i >= 0; i--) {
            if (notificationList.get(i).getCameraName().equals(espCamera.getName())) {
                notificationList.get(i).setPictureBmp(bmp);

                new BackgroundSaveNotificationPicture(espCamera, notificationList.get(i)).execute();
                break;
            }
        }
    }

    /**
     * AsyncTask-class with methods to create and save a file of the notification picture
     */
    class BackgroundSaveNotificationPicture extends AsyncTask<Void, Void, Void> {
        private final EspCamera espCamera;
        private final Notification notification;

        public BackgroundSaveNotificationPicture(EspCamera espCamera, Notification notification) {
            this.espCamera = espCamera;
            this.notification = notification;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // create notification picture
            String jpegFilePath = CreateFile.createJpegFile(espCamera).getAbsolutePath();

            try {
                FileOutputStream fOut = new FileOutputStream(jpegFilePath);
                notification.getPictureBmp().compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                notification.setPicturePath(jpegFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Void result) {
            // save notificationList, update view if needed and push notification
            saveNotifications();

            if (viewState == MainActivity.State.NotificationFragment) {
                notificationPresenter.notifyOnMotionDetected(notification);
            }

            notificationHandler.notifyFrom(espCamera, notification);
        }
    }

    /**
     * method to reset the values of the espCamera and send the command to the espCamera
     */
    @Override
    public void resetCameraValues(EspCamera espCamera) {

    }

    /**
     * method to remove the espCamera -> closes webSocket, removes cameraCard and removes it from the maps -> then save all remaining espCameras
     */
    @Override
    public void removeCamera(EspCamera espCamera) {
        // close and remove webSocket in service
        webSocketService.closeWebSocket(espCamera);

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

    /**
     * method to delete all notification with picture files from selectedItemsToDelete
     */
    @Override
    public void deleteSelectedNotificationItems(List<Notification> selectedItemsToDelete) {

        for (Notification notification : selectedItemsToDelete) {
            if (notificationList.contains(notification)) {

                File file = new File(notification.getPicturePath());
                if (file.delete()) {
                    notificationList.remove(notification);
                }
            }
        }

        saveNotifications();
    }

    /**
     * loads the data from sharedPreferences
     */
    public void loadNotifications() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Notifications", null);
        Type type = new TypeToken<ArrayList<Notification>>() {
        }.getType();
        ArrayList<Notification> mExampleList = gson.fromJson(json, type);
        if (mExampleList != null) {
            notificationList = mExampleList;
        }

        // load pictures
        for (Notification notification : notificationList) {
            Bitmap bitmap = BitmapFactory.decodeFile(notification.getPicturePath());
            notification.setPictureBmp(bitmap);
        }
    }

    /**
     * saves the data to sharedPreferences
     */
    public void saveNotifications() {
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // save new notificationList without bitmaps
        ArrayList<Notification> notificationsArrayList = new ArrayList<>();
        for (Notification notification : notificationList) {
            Notification newNotification = new Notification();
            newNotification.setCameraName(notification.getCameraName());
            newNotification.setCameraIp(notification.getCameraIp());
            newNotification.setTimeStamp(notification.getTimeStamp());
            newNotification.setPicturePath(notification.getPicturePath());

            notificationsArrayList.add(newNotification);
        }

        String json = gson.toJson(notificationsArrayList);
        editor.putString("Notifications", json);
        editor.apply();
    }

    /**
     * method to navigate to CamSettingsFragment and sets the selected camera for the correct settings
     */
    @Override
    public void navigateToCamSettingsFragment(EspCamera espCamera) {
        viewState = MainActivity.State.CamSettingsFragment;

        camSettingsPresenter.setSelectedEspCamera(espCameraMap.get(espCamera.getIpAddress()));

        mainActivity.navigateToCamSettingsFragment();
    }

    /**
     * method to navigate to HomeFragment
     */
    @Override
    public void navigateToHomeFragment() {
        viewState = MainActivity.State.HomeFragment;
        mainActivity.navigateToHomeFragment();
    }

    /**
     * method to navigate to GalleryFragment
     */
    @Override
    public void navigateToGalleryFragment() {
        viewState = MainActivity.State.GalleryFragment;
        mainActivity.navigateToGalleryFragment();
    }

    /**
     * method to navigate to NotificationFragment
     */
    @Override
    public void navigateToNotificationFragment() {
        viewState = MainActivity.State.NotificationFragment;
        mainActivity.navigateToNotificationFragment();
    }

    /**
     * method to navigate to ViewPagerFragment
     */
    @Override
    public void navigateToGalleryViewPagerFragment() {
        viewState = MainActivity.State.GalleryViewPagerFragment;
        mainActivity.navigateToGalleryViewPagerFragment();
    }

    /**
     * method to send a message with the webSocket of the espCamera
     */
    @Override
    public void sendWebSocketMessage(EspCamera espCamera, String message) {
        webSocketService.sendMessage(espCamera, message);
    }

    /**
     * method checks if the webSocket of this espCamera is connected and active
     */
    @Override
    public boolean isWebSocketConnected(EspCamera espCamera) {
        return webSocketService.isWebSocketConnected(espCamera);
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
    public List<Notification> getNotificationItems() {
        return notificationList;
    }

    @Override
    public int getAllCamerasCount() {
        return espCameraMap.size();
    }

    @Override
    public int getOpenedWebSocketCount() {
        return openedWebSocketCount;
    }

    @Override
    public void setOpenedWebSocketCount(int newCount) {
        this.openedWebSocketCount = newCount;
    }

    public MainActivity.State getViewState() {
        return viewState;
    }

    @Override
    public Activity getActivity() {
        return mainActivity;
    }

    private MainPresenter getThisMainPresenter() {
        return this;
    }

    /**
     * method is called when MainActivity is destroyed so all cameraCards can be stopped
     */
    @Override
    public void onDestroy() {
        for (CameraCard cameraCard : cameraCardMap.values()) {
            cameraCard.stop();
        }
    }
}

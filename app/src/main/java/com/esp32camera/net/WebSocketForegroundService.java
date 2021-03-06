package com.esp32camera.net;

import static com.esp32camera.util.Constants.WEBSOCKETS_SERVER_PORT;
import static com.esp32camera.util.Constants.WEBSOCKETS_SERVER_WS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.esp32camera.MainActivity;
import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.model.EspCamera;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// example and info - https://developer.android.com/guide/components/foreground-services
// example and info - https://stackoverflow.com/questions/20594936/communication-between-activity-and-service
// example and info - https://developer.android.com/reference/android/app/Service#LocalServiceSample

public class WebSocketForegroundService extends Service {
    public static final String CHANNEL_ID = "WebSocketForegroundServiceChannel";
    private Callbacks webSocketService;
    private final IBinder mBinder = new LocalBinder();

    private Map<String, CustomWebSocketClient> webSocketClientMap;

    /**
     * this method is called when the service is started with startService()
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * this method is called when the service is started with startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ESP Camera app is running!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        // init variables
        webSocketClientMap = new HashMap<>();

        return START_NOT_STICKY;
    }

    /**
     * method to send a message with the webSocket of the espCamera to the espCamera
     */
    public void sendMessage(EspCamera espCamera, String message) {
        webSocketClientMap.get(espCamera.getIpAddress()).send(message);
    }

    /**
     * method to create/connect a new webSocket for this espCamera and saves it to the map
     */
    public void createWebSocket(EspCamera espCamera, MainPresenter mainPresenter, WebSocketServiceInterface webSocketServiceInterface) {
        // create new one when app starts after hard stop
        URI uri = null;
        try {
            uri = new URI(WEBSOCKETS_SERVER_WS + espCamera.getIpAddress() + WEBSOCKETS_SERVER_PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        CustomWebSocketClient webSocketClient = new CustomWebSocketClient(Objects.requireNonNull(uri));
        webSocketClient.init(webSocketService, this, espCamera, mainPresenter, webSocketServiceInterface);

        webSocketClient.setConnectionLostTimeout(3); // timeout to reconnect
        webSocketClient.connect();

        webSocketClientMap.put(espCamera.getIpAddress(), webSocketClient);
    }

    /**
     * method to update the service with new/fresh objects after app restart
     */
    public void updateWebSocketForegroundService(EspCamera newEspCamera, MainPresenter mainPresenter, WebSocketService webSocketService, WebSocketServiceInterface webSocketServiceInterface) {
        webSocketClientMap.get(newEspCamera.getIpAddress()).updateObjects(newEspCamera, mainPresenter, webSocketService, webSocketServiceInterface);
    }

    /**
     * method to close the webSocket of the espCamera and removes it from the map
     */
    public void closeWebSocket(EspCamera espCamera, MainPresenter mainPresenter) {
        webSocketClientMap.get(espCamera.getIpAddress()).close();
        webSocketClientMap.remove(espCamera.getIpAddress());

        // removes one from webSocketCount
        if (mainPresenter.getOpenedWebSocketCount() != 0) {
            mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() - 1);
        }
        if (mainPresenter.getAllCamerasCount() == 0) {
            stopSelf();
        }

        updateNotification(mainPresenter);
    }

    /**
     * method checks if the webSocket of this espCamera is connected/open and active
     */
    public boolean isWebSocketConnected(EspCamera espCamera) {
        return webSocketClientMap.get(espCamera.getIpAddress()).isOpen();
    }

    /**
     * method checks if a webSocket object of this espCamera is already existing
     */
    public boolean webSocketAlreadyExisting(EspCamera espCamera) {
        return webSocketClientMap.containsKey(espCamera.getIpAddress());
    }

    /**
     * localBinder to bind it to the WebSocketService class to communicate
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // returns the instance of this service
    public class LocalBinder extends Binder {
        public WebSocketForegroundService getService() {
            return WebSocketForegroundService.this;
        }
    }

    /**
     * method to create the notification channel to let the service run
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "ESP Camera is running!",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    /**
     * method to client registers to the service as Callbacks client to communicate
     */
    public void registerClient(WebSocketService webSocketService) {
        this.webSocketService = (Callbacks) webSocketService;
    }

    /**
     * method to update the notification with e.g. "1 of 2 cameras are online!" when new webSocket opened or closes
     */
    protected void updateNotification(MainPresenter mainPresenter) {
        Intent notificationIntent = new Intent(getApplication(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(),
                0, notificationIntent, 0);
        Notification notification;
        // handle notification camera count
        if (mainPresenter.getAllCamerasCount() > 0) {
            notification = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
                    .setContentTitle("ESP Camera app is running!")
                    .setContentText(mainPresenter.getOpenedWebSocketCount() + " of " + mainPresenter.getAllCamerasCount() + " cameras are online!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("ESP Camera app is running!")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        startForeground(1, notification);
    }

    /**
     * callbacks interface for communication with service clients!
     */
    public interface Callbacks {
        void handleMessage(EspCamera espCamera, String message);

        void handleByteBuffer(EspCamera espCamera, ByteBuffer bytes);
    }

    /**
     * method which is called when service is closed/killed to close webSockets and clear the map
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        for (WebSocketClient webSocketClient : webSocketClientMap.values()) {
            webSocketClient.close();
        }
        webSocketClientMap.clear();
        stopSelf();
    }

    /**
     * method to stop the service
     */
    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}

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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.esp32camera.MainActivity;
import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.model.EspCamera;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Objects;

public class WebSocketForegroundService extends Service {
    public static final String CHANNEL_ID = "WebSocketForegroundServiceChannel";
    private Callbacks webSocketService;

    private final IBinder mBinder = new LocalBinder();
    private Date date;

    private WebSocketClient webSocketClient;
    private boolean previousStateWasOpen;

    @Override
    public void onCreate() {
        super.onCreate();
    }

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
        //do heavy work on a background thread


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void send(String message) {
        webSocketClient.send(message);
    }

    public boolean isOpen() {
        return webSocketClient.isOpen();
    }

    public void close() {
        webSocketClient.close();
    }

    //returns the instance of the service
    public class LocalBinder extends Binder {
        public WebSocketForegroundService getServiceInstance() {
            return WebSocketForegroundService.this;
        }
    }

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

    public void startCounter() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Counter started", Toast.LENGTH_SHORT).show();
                webSocketService.updateClient("TEST STRING"); //Update Activity (client) by the implementd callback
            }
        }, 5000);
    }

    //Here Activity register to the service as Callbacks client
    public void registerClient(WebSocketService webSocketService) {
        date = new Date();
        this.webSocketService = (Callbacks) webSocketService;
    }

    public void startWebSocketService(WebSocketService webSocketService, MainPresenter mainPresenter, EspCamera espCamera, WebSocketServiceInterface webSocketServiceInterface) {

        URI uri = null;
        try {
            uri = new URI(WEBSOCKETS_SERVER_WS + espCamera.getIpAddress() + WEBSOCKETS_SERVER_PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        previousStateWasOpen = true;

        webSocketClient = new WebSocketClient(Objects.requireNonNull(uri)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                webSocketServiceInterface.OnConnectionOpened(espCamera, "WEBSOCKET OPENED");
                Log.i("WebSocket", "Opened");

                mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() + 1);

                webSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);

//                noopTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // Send NOOP Message
//                        sendMessage("COM_NOOP");
//                    }
//                }, 0, 1000 * 30);

                updateNotification(mainPresenter);

                previousStateWasOpen = true;
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                webSocketService.handleMessage(message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {

                webSocketServiceInterface.OnConnectionClosed(espCamera, "WEBSOCKET CLOSE");
                Log.i("WebSocket", "Closed " + s);

                if (mainPresenter.getOpenedWebSocketCount() != 0) {
                    mainPresenter.setOpenedWebSocketCount(mainPresenter.getOpenedWebSocketCount() - 1);
                }

                // tries to reconnect the webSocket after 5 sec; if not working then there will be an onClose Error again with reconnect etc.
                mainPresenter.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("WebSocket", "Try to reconnect...");
                                webSocketClient.reconnect();
                            }
                        }, 5000);
                    }
                });

                if (previousStateWasOpen) {
                    updateNotification(mainPresenter);
                }

                previousStateWasOpen = false;
            }

            @Override
            public void onError(Exception e) {
                webSocketServiceInterface.OnConnectionFailed(espCamera, "WEBSOCKET ERROR");
                Log.i("WebSocket", "Error " + e.getMessage());
            }
        };
        webSocketClient.setConnectionLostTimeout(3);
        webSocketClient.connect();
    }

    private void updateNotification(MainPresenter mainPresenter) {
        Intent notificationIntent = new Intent(getApplication(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(),
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
                .setContentTitle("ESP Camera app is running!")
                .setContentText(mainPresenter.getOpenedWebSocketCount() + " of " + mainPresenter.getAllCamerasCount() + " cameras are online!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    //callbacks interface for communication with service clients!
    public interface Callbacks {
        void updateClient(String data);
    }
}

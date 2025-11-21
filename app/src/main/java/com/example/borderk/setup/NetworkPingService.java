package com.example.borderk.setup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.borderk.network.NetworkClient;

public class NetworkPingService extends Service {

    private static final String TAG = "PingService";
    private static final String CHANNEL_ID = "PingServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    public static final String CMD_START_LAUNCH_MODE = "START_LAUNCH_MODE";
    public static final String CMD_START_STATUS_MODE = "START_START_MODE";
    public static final String ACTION_PING_RECEIVED = "com.example.borderk.PING_RECEIVED";   // For MainActivity
    public static final String ACTION_STATUS_UPDATE = "com.example.borderk.STATUS_UPDATE";   // For Fragments/GameActivity
    public static final String EXTRA_GAME_ID = "game_id";
    public static final String EXTRA_STATUS_DATA = "status_data";

    private Handler handler;
    private Runnable pollingRunnable;

    private static final long POLLING_INTERVAL = 3000;

    private boolean isStatusMode = false;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (CMD_START_LAUNCH_MODE.equals(action)) {
                isStatusMode = false;
                setupPollingRunnable();
                Log.d(TAG, "Service: Launch Mode started.");

            } else if (CMD_START_STATUS_MODE.equals(action)) {
                isStatusMode = true;
                setupPollingRunnable();
                Log.d(TAG, "Service: Status Mode started.");
            }

        } else {
            isStatusMode = false;
            setupPollingRunnable();
        }

        createNotificationChannel();
        Notification notification = buildForegroundNotification();
        startForeground(NOTIFICATION_ID, notification);

        handler.removeCallbacks(pollingRunnable);
        handler.post(pollingRunnable);

        return START_STICKY;
    }
    private void setupPollingRunnable() {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {

                if (!isStatusMode) {
                    simulateApiCallForLaunch();
                } else {
                    simulateApiCallForStatus();
                }

                handler.postDelayed(this, POLLING_INTERVAL);
            }
        };
    }
    private void simulateApiCallForLaunch() {

        String receivedData = NetworkClient.checkForGameLaunchSignal();

        if (receivedData != null) {
            String gameId = parseGameId(receivedData);

            if (gameId != null) {
                Log.i(TAG, "Launch detected. Game ID = " + gameId);

                // Stop polling so that the app reacts immediately
                handler.removeCallbacks(pollingRunnable);

                notifyMainActivity(gameId);
            }
        }
    }
    private void simulateApiCallForStatus() {

        String statusUpdate = NetworkClient.getCurrentStatusUpdate();

        if (statusUpdate != null) {
            notifyStatusUpdate(statusUpdate);
        }
    }
    private String parseGameId(String jsonData) {
        try {
            int startIndex = jsonData.indexOf("\"game_id\": \"") + 12;
            int endIndex = jsonData.indexOf("\"", startIndex);

            if (startIndex != -1 && endIndex != -1) {
                return jsonData.substring(startIndex, endIndex);
            }

        } catch (Exception e) {
            Log.e(TAG, "JSON parse error: " + e.getMessage());
        }
        return null;
    }
    private void notifyMainActivity(String gameId) {
        Intent localIntent = new Intent(ACTION_PING_RECEIVED);
        localIntent.putExtra(EXTRA_GAME_ID, gameId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
    private void notifyStatusUpdate(String statusUpdate) {
        Intent localIntent = new Intent(ACTION_STATUS_UPDATE);
        localIntent.putExtra(EXTRA_STATUS_DATA, statusUpdate);

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Game Controller Ping Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
    private Notification buildForegroundNotification() {
        String contentText = isStatusMode
                ? "Running Status Mode..."
                : "Waiting for Launch Signal...";

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Controller Active")
                .setContentText(contentText)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handler != null && pollingRunnable != null) {
            handler.removeCallbacks(pollingRunnable);
        }

        Log.d(TAG, "NetworkPingService destroyed.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.example.borderk.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ControlService extends Service {
    private static final String TAG = "ControlService";
    private static final int NOTIFICATION_ID = 101;
    private static final String CHANNEL_ID = "BorderKControlChannel";



    public static final String ACTION_STATUS_UPDATE = "com.example.borderk.STATUS_UPDATE";
    public static final String ACTION_LAUNCH_TRIGGERED = "com.example.borderk.LAUNCH_TRIGGERED";
    public static final String ACTION_STOP_RECEIVED = "com.example.borderk.STOP_RECEIVED";
    public static final String EXTRA_STATUS_MESSAGE = "status_message";
    public static final String EXTRA_GAME_ID = "game_id";
    public static final String EXTRA_APP_ID = "app_id";



    private static final long POLL_INTERVAL_MS = 5000;
    private static final String API_BASE_URL = "https://bordera-api.vercel.app/api/launch-signal";
    private Handler handler;
    private Runnable runnable;
    private OkHttpClient client;
    private String appId;
    private boolean isGameRunning = false;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "ControlService criado.");
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildForegroundNotification("Serviço Inicializado"));
        client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .build();

        handler = new Handler();
        startPolling();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra(EXTRA_APP_ID)) {
                appId = intent.getStringExtra(EXTRA_APP_ID);
                Log.d(TAG, "ID do App definido para polling: " + appId);
            }
        }
        return START_STICKY;
    }
    private void startPolling() {
        if (runnable != null) handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                pollApiForLaunch();
                handler.postDelayed(this, POLL_INTERVAL_MS);
            }
        };

        handler.post(runnable);
        Log.d(TAG, "Polling iniciado (Contínuo).");
    }
    private void pollApiForLaunch() {
        if (appId == null || appId.isEmpty() || "null".equalsIgnoreCase(appId)) {
            updateStatus("Falta ou ID do App inválido: Polling pausado.");
            return;
        }
        String url = API_BASE_URL + "?appId=" + appId;
        updateStatus(isGameRunning ? "Aguardando STOP..." : "Aguardando START...");
        Log.d(TAG, "Executing GET request to: " + url);

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateStatus("Erro de rede: " + e.getMessage());
                Log.e(TAG, "Falha no Polling da API: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.w(TAG, "API Polling non-successful, URL: " + url + " Code: " + response.code());
                    updateStatus("Status: Erro (" + response.code() + ")");
                    return;
                }
                String body = "";
                if (response.body() != null) {
                    body = response.body().string();
                }
                Log.d(TAG, "Resposta Bruta da API: " + body);

                if (body.trim().isEmpty()) {
                    if (isGameRunning) {
                        Log.i(TAG, "STOP Implícito: Corpo da API vazio enquanto o jogo estava em execução.");
                        isGameRunning = false;
                        sendStopSignal();
                        updateStatus("PARADO: Jogo Finalizado (IDLE)");
                    } else {
                        updateStatus("Status: Ocioso (corpo vazio)");
                    }
                    return;
                }
                try {
                    JSONObject json = new JSONObject(body);
                    String status = json.optString("status", "").toLowerCase();
                    String action = json.optString("action", "").toLowerCase();
                    String gameId = json.optString("game_id", "unknown");

                    boolean isStartSignal = "start".equalsIgnoreCase(status) || "start".equalsIgnoreCase(action);
                    boolean isStopSignal = "stop".equalsIgnoreCase(status) || "stop".equalsIgnoreCase(action);

                    if (isStartSignal) {
                        if (!isGameRunning) {
                            isGameRunning = true;
                            triggerLaunch(gameId);
                            updateStatus("LANÇADO: " + gameId);

                        } else {
                            updateStatus("Jogo em Execução: " + gameId);
                        }
                    } else if (isStopSignal) {
                        if (isGameRunning) {
                            Log.i(TAG, "STOP recebido (Explícito). Sinalizando GameActivity para fechar.");
                        } else {
                            Log.w(TAG, "STOP recebido (Explícito e Inesperado). Sinalizando GameActivity para fechar.");
                        }

                        isGameRunning = false;
                        sendStopSignal();
                        updateStatus("PARADO: Sinal Explícito.");
                    } else {
                        updateStatus("Status da API: " + status + action);
                    }
                } catch (JSONException e) {
                    updateStatus("Erro de Análise JSON: " + e.getMessage());
                    Log.e(TAG, "Erro ao analisar JSON. Corpo: " + body, e);
                }
            }
        });
    }
    private void triggerLaunch(String gameId) {
        Intent it = new Intent(ACTION_LAUNCH_TRIGGERED);
        it.putExtra(EXTRA_GAME_ID, gameId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(it);
        Log.i(TAG, "Broadcast de Lançamento enviado: " + gameId);
    }
    private void sendStopSignal() {
        Intent it = new Intent(ACTION_STOP_RECEIVED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(it);
        Log.i(TAG, "Broadcast de STOP recebido enviado.");
    }

    private void updateStatus(String msg) {
        Intent it = new Intent(ACTION_STATUS_UPDATE);
        it.putExtra(EXTRA_STATUS_MESSAGE, msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(it);
        updateForegroundNotification(msg);
    }
    private Notification buildForegroundNotification(String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Serviço BorderK")
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setPriority(Notification.PRIORITY_LOW)
                .build();
    }

    private void updateForegroundNotification(String text) {
        Notification notification = buildForegroundNotification(text);
        NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (m != null) m.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Serviço de Controle",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager m = getSystemService(NotificationManager.class);
            if (m != null) m.createNotificationChannel(ch);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        client.dispatcher().cancelAll();
        Log.i(TAG, "Serviço destruído");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
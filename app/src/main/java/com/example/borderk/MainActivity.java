package com.example.borderk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.borderk.service.ControlService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView statusText;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ControlService.ACTION_STATUS_UPDATE.equals(intent.getAction())) {
               String msg = intent.getStringExtra(ControlService.EXTRA_STATUS_MESSAGE);
                return;
            }
            if (ControlService.ACTION_LAUNCH_TRIGGERED.equals(intent.getAction())) {
                String gameId = intent.getStringExtra(ControlService.EXTRA_GAME_ID);
                Log.i(TAG, "Trigger recebido -> starting GameActivity for: " + gameId);

                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                gameIntent.putExtra("game_id", gameId);

                startActivity(gameIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusText = findViewById(R.id.text_debug);

        Intent serviceIntent = new Intent(this, ControlService.class);
        String uniqueAppIdValue = "ANDROID_APP_DEFAULT_ID";
        serviceIntent.putExtra(ControlService.EXTRA_APP_ID, uniqueAppIdValue);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ControlService.ACTION_STATUS_UPDATE);
        filter.addAction(ControlService.ACTION_LAUNCH_TRIGGERED);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
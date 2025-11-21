package com.example.borderk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.borderk.games.StepFive;
import com.example.borderk.games.StepFour;
import com.example.borderk.games.StepOne;
import com.example.borderk.games.StepTwo;
import com.example.borderk.games.StepThree;
import com.example.borderk.service.ControlService;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private static final long STEP_DURATION = 4000; // 4s
    private final Handler handler = new Handler();
    private String gameId;
   private long totalTimedDuration = 0;
    private final BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ControlService.ACTION_STOP_RECEIVED.equals(intent.getAction())) {
                Log.i(TAG, "Sinal de STOP recebido do ControlService. Finalizando jogo.");
                finishGame();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameId = getIntent().getStringExtra("game_id");

        if (gameId == null || gameId.isEmpty()) {
            Toast.makeText(this, "Erro: ID do Jogo não encontrado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        Log.d(TAG, "Iniciando GameActivity com ID: " + gameId);
        LocalBroadcastManager.getInstance(this).registerReceiver(stopReceiver, new IntentFilter(ControlService.ACTION_STOP_RECEIVED));
        runStepSequence();
    }
    private void runStepSequence() {
        totalTimedDuration = 0;
        int finalDrawable;
        int finalText;

        switch (gameId) {
            case "ACE_OF_SPADES":
                finalDrawable = R.drawable.ace_of_spades;
                finalText = R.string.ace_of_spades;
                showFragment(StepOne.newInstance(gameId));

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepTwo.newInstance(gameId));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFour.newInstance(gameId, finalDrawable));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFive.newInstance(gameId, finalDrawable, finalText));
                }, totalTimedDuration);
                break;
            case "FOUR_OF_SPADES":
                finalDrawable = R.drawable._4_of_spades;
                finalText = R.string.four_of_spades;
                showFragment(StepOne.newInstance(gameId));

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepTwo.newInstance(gameId));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFour.newInstance(gameId, finalDrawable));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFive.newInstance(gameId, finalDrawable, finalText));
                }, totalTimedDuration);
                break;
            case "FOUR_OF_CLUBS":
                finalDrawable = R.drawable._4_of_clubs;
                finalText = R.string.four_of_clubs;
                showFragment(StepOne.newInstance(gameId));

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepTwo.newInstance(gameId));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFour.newInstance(gameId, finalDrawable));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFive.newInstance(gameId, finalDrawable, finalText));
                }, totalTimedDuration);
                break;
            case "EIGHT_OF_HEARTS":
                finalDrawable = R.drawable._8_of_hearts;
                finalText = R.string.eight_of_hearts;
                showFragment(StepOne.newInstance(gameId));

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepTwo.newInstance(gameId));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFour.newInstance(gameId, finalDrawable));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
               handler.postDelayed(() -> {
                    showFragment(StepFive.newInstance(gameId, finalDrawable, finalText));
                }, totalTimedDuration);
                break;

            case "SEVEN_OF_DIAMONDS":
                finalDrawable = R.drawable._7_of_diamonds;
                finalText = R.string.seven_of_diamonds;
                showFragment(StepOne.newInstance(gameId));

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepTwo.newInstance(gameId));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFour.newInstance(gameId, finalDrawable));
                }, totalTimedDuration);

                totalTimedDuration += STEP_DURATION;
                handler.postDelayed(() -> {
                    showFragment(StepFive.newInstance(gameId, finalDrawable, finalText));
                }, totalTimedDuration);
                break;
            default:
                Log.e(TAG, "ID do Jogo não reconhecido: " + gameId + ". Finalizando.");
                finishGame();
                break;
        }
    }
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Assuming R.id.fragment_container exists
        transaction.commit();
    }
    private void finishGame() {
        if (isFinishing()) return;
        Log.i(TAG, "Atividade do Jogo está finalizando.");
        finish();
    }
   @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(stopReceiver);
    }
}
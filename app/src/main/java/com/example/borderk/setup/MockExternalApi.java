package com.example.borderk.setup;

import android.util.Log;

public class MockExternalApi {
    private static final String TAG = "MockExternalApi";

    public static String checkForGameLaunchSignal() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if (currentTimeSeconds % 30 < 5) {
            String gameId = "LEVEL_" + (currentTimeSeconds % 100);
            String response = "{\"status\": \"start\", \"game_id\": \"" + gameId + "\"}";
            Log.d(TAG, "Generating active launch signal: " + gameId);
            return response;
        }

        return null;
    }

    public static String getCurrentStatusUpdate() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        int statusCycle = (int) (currentTimeSeconds % 15);

        if (statusCycle < 5) {
            return "SYSTEM ONLINE: Awaiting next command...";
        } else if (statusCycle < 10) {
            return "STATUS: IDLE. Energy levels nominal.";
        } else {
            return "CHECKPOINT REACHED. Data transfer complete.";
        }
    }
}
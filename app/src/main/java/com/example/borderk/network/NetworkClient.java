package com.example.borderk.network;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class NetworkClient {
    private static final String TAG = "NetworkClient";
    private static final String BASE_URL = "https://bordera-api.vercel.app/api";
    private static final String LAUNCH_SIGNAL_ENDPOINT = BASE_URL + "/launch-signal";
    private static final String STATUS_ENDPOINT = BASE_URL + "/status";
    private static final OkHttpClient client = new OkHttpClient();

    public static String checkForGameLaunchSignal() {
        Request request = new Request.Builder()
                .url(LAUNCH_SIGNAL_ENDPOINT)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                String jsonResponse = response.body().string();

                Log.d(TAG, "LIVE SIGNAL RECEIVED (200 OK): " + jsonResponse);
                return jsonResponse;

            } else if (response.code() == 204) {
                Log.d(TAG, "SIGNAL DENIED (204 No Content). Waiting...");
                return null;
            } else {
                Log.e(TAG, "Launch Check Error: Unexpected response code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Launch Check Network Exception: " + e.getMessage());
            return null;
        }
    }
    public static String getCurrentStatusUpdate() {
        Request request = new Request.Builder()
                .url(STATUS_ENDPOINT)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String statusText = response.body().string();

                Log.d(TAG, "LIVE STATUS RECEIVED: " + statusText);
                return statusText;
            } else {
                String error = "STATUS ERROR: HTTP " + response.code();
                Log.e(TAG, error + " from " + STATUS_ENDPOINT);
                return error;
            }
        } catch (IOException e) {
            String error = "STATUS ERROR: Network down: " + e.getMessage();
            Log.e(TAG, error);
            return error;
        }
    }
}
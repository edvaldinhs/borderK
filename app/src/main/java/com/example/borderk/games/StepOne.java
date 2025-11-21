package com.example.borderk.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.borderk.R;

public class StepOne extends Fragment {

    private static final String ARG_GAME_ID = "gameId";
    private String gameId;

    private TextView infoText;

    public static StepOne newInstance(String gameId) {
        StepOne frag = new StepOne();
        Bundle b = new Bundle();
        b.putString(ARG_GAME_ID, gameId);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            gameId = getArguments().getString(ARG_GAME_ID, "UNKNOWN");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_value_card_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

//        infoText = v.findViewById(R.id.text_info_step);
        // Exibe o ID do jogo na tela
//        infoText.setText("Step 1 — Game ID: " + gameId + " (Timed)");
    }
}
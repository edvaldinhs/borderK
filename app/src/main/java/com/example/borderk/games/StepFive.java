package com.example.borderk.games;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.borderk.R;

public class StepFive extends Fragment {

    private static final String TAG = "StepFive";
    private static final String ARG_GAME_ID = "game_id";
    private static final String ARG_DRAWABLE_ID = "drawableId";
    private static final String ARG_TEXT_ID = "textId";

    private String gameId;
    private int drawableId;
    private int textId;

    public static StepFive newInstance(String gameId, int drawableResId, int nameTextResId) {
        StepFive fragment = new StepFive();
        Bundle args = new Bundle();
        args.putString(ARG_GAME_ID, gameId);
        args.putInt(ARG_DRAWABLE_ID, drawableResId);
        args.putInt(ARG_TEXT_ID, nameTextResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameId = getArguments().getString(ARG_GAME_ID);
            drawableId = getArguments().getInt(ARG_DRAWABLE_ID, 0);
            textId = getArguments().getInt(ARG_TEXT_ID, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_value_card_five, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView diffImageView = view.findViewById(R.id.diff);
        TextView gameNameTextView = view.findViewById(R.id.gameName);

        if (diffImageView != null && drawableId != 0) {
            diffImageView.setImageResource(drawableId);
        }

        if (gameNameTextView != null && textId != 0) {
            gameNameTextView.setText(textId);
        }

        // Placeholder logic for status messages
//        TextView statusMessageText = view.findViewById(R.id.text_status_message);
//        if (statusMessageText != null) {
//            statusMessageText.setText("Jogo " + (gameId != null ? gameId : "ID desconhecido") + " concluído. Aguardando o Controller enviar o sinal de STOP...");
//        }

        Log.d(TAG, "StepFive em execução. Aguardando GameActivity receber broadcast de STOP para fechar.");
    }
}
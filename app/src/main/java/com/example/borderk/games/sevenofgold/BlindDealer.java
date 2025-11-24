package com.example.borderk.games.sevenofgold;

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

public class BlindDealer extends Fragment {

    private static final String TAG = "BlindDealer";
    private static final String ARG_GAME_ID = "game_id";
    private static final String ARG_DRAWABLE_ID = "drawableId";
    private static final String ARG_TEXT_ID = "textId";

    private String gameId;
    private int drawableId;
    private int textId;
    private int game_value;
    int press_x_times;

    private ImageView chip_red;
    private ImageView chip_green;
    private ImageView chip_blue;
    private ImageView chip_black;
    public static BlindDealer newInstance(String gameId, int drawableResId, int nameTextResId) {
        BlindDealer fragment = new BlindDealer();
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
            game_value = 0;
            press_x_times = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blind_dealer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView diffImageView = view.findViewById(R.id.diff);
        TextView gameNameTextView = view.findViewById(R.id.gameName);
        TextView gameValueTextView = view.findViewById(R.id.gameValue);
        TextView pressedTimesTextView = view.findViewById(R.id.pressedTimes);
        chip_red = view.findViewById(R.id.chip_red);
        chip_green = view.findViewById(R.id.chip_green);
        chip_blue = view.findViewById(R.id.chip_blue);
        chip_black = view.findViewById(R.id.chip_black);

        if (diffImageView != null && drawableId != 0) {
            diffImageView.setImageResource(drawableId);
        }

        if (gameNameTextView != null && textId != 0) {
            gameNameTextView.setText(textId);
        }

        chip_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_value += 5;
                press_x_times +=1;
                if (pressedTimesTextView != null) {
                    pressedTimesTextView.setText(String.valueOf(press_x_times));
                }
                if (gameValueTextView != null) {
                    gameValueTextView.setText(String.valueOf(game_value));
                }
            }
        });

        chip_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_value += 10;
                press_x_times +=1;
                if (pressedTimesTextView != null) {
                    pressedTimesTextView.setText(String.valueOf(press_x_times));
                }
                if (gameValueTextView != null) {
                    gameValueTextView.setText(String.valueOf(game_value));
                }
            }
        });
        chip_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_value += 25;
                press_x_times +=1;
                if (pressedTimesTextView != null) {
                    pressedTimesTextView.setText(String.valueOf(press_x_times));
                }
                if (gameValueTextView != null) {
                    gameValueTextView.setText(String.valueOf(game_value));
                }
            }
        });
        chip_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_value += 50;
                press_x_times +=1;
                if (pressedTimesTextView != null) {
                    pressedTimesTextView.setText(String.valueOf(press_x_times));
                }
                if (gameValueTextView != null) {
                    gameValueTextView.setText(String.valueOf(game_value));
                }
            }
        });

                // Placeholder logic for status messages
//        TextView statusMessageText = view.findViewById(R.id.text_status_message);
//        if (statusMessageText != null) {
//            statusMessageText.setText("Jogo " + (gameId != null ? gameId : "ID desconhecido") + " concluído. Aguardando o Controller enviar o sinal de STOP...");
//        }

        Log.d(TAG, "BlindDealer em execução. Aguardando GameActivity receber broadcast de STOP para fechar.");
    }
}
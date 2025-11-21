package com.example.borderk.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.borderk.R; // Ensure your R file is correct

public class StepFour extends Fragment {

    private static final String ARG_GAME_ID = "gameId";
    private static final String ARG_DRAWABLE_ID = "drawableId";

    private String gameId;
    private int drawableId;

    public static StepFour newInstance(String gameId, int drawableResId) {
        StepFour frag = new StepFour();
        Bundle b = new Bundle();
        b.putString(ARG_GAME_ID, gameId);
        b.putInt(ARG_DRAWABLE_ID, drawableResId);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gameId = getArguments().getString(ARG_GAME_ID, "UNKNOWN");
            drawableId = getArguments().getInt(ARG_DRAWABLE_ID, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_value_card_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        ImageView diffImageView = v.findViewById(R.id.diff);

        if (diffImageView != null && drawableId != 0) {
            diffImageView.setImageResource(drawableId);
        }
    }
}
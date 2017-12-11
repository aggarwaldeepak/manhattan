package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerBarFragment extends Fragment {

    ProgressBar pbLoading, pbProgress;
    ImageView ivPlayPause, ivUpArrow;
    public static Fragment fragment;
    Boolean f = false;
    public PlayerBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View playerBarFragment = inflater.inflate(R.layout.fragment_player_bar, container, false);

        pbLoading = playerBarFragment.findViewById(R.id.pb_loading);
        pbProgress = playerBarFragment.findViewById(R.id.pb_progress);
        ivPlayPause = playerBarFragment.findViewById(R.id.iv_play_pause);
        ivUpArrow = playerBarFragment.findViewById(R.id.iv_up_arrow);
        fragment = new PlayerFragment();

        ivUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_layout, fragment)
                        .commit();
                ((PlayerCommunication)getActivity()).onOpenPlayerFragment();
//                MainActivity.toolbar.setVisibility(GONE);
            }
        });

        pbLoading.setVisibility(GONE);
        pbProgress.setProgress(50);

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!f) {
                    f = true;
                    ivPlayPause.setVisibility(GONE);
                    pbLoading.setVisibility(View.VISIBLE);
                } else {
                    f = false;
                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    ivPlayPause.setVisibility(View.VISIBLE);
                    pbLoading.setVisibility(GONE);
                }
            }
        });

        pbLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivPlayPause.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(GONE);
            }
        });


        return playerBarFragment;
    }

}

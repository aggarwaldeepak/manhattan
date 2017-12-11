package com.agarwal.vinod.govindkigali.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.agarwal.vinod.govindkigali.Utils.MediaPlayBack;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerBarFragment extends Fragment {

    ProgressBar pbLoading, pbProgress;
    ImageView ivPlayPause, ivUpArrow;
    TextView tvSong;
    private String title = "", url = "";
    private Integer duration = 0;
    Boolean f = false;
    public PlayerBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("Title");
        url = getArguments().getString("Stream");
        duration = getArguments().getInt("Duration");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View playerBarFragment = inflater.inflate(R.layout.fragment_player_bar, container, false);

        pbLoading = playerBarFragment.findViewById(R.id.pb_loading);
        pbProgress = playerBarFragment.findViewById(R.id.pb_progress);
        ivPlayPause = playerBarFragment.findViewById(R.id.iv_play_pause);
        ivUpArrow = playerBarFragment.findViewById(R.id.iv_up_arrow);
        tvSong = playerBarFragment.findViewById(R.id.tv_song);

        pbLoading.setVisibility(View.VISIBLE);
        ivPlayPause.setVisibility(GONE);

        //TO-DO ==============>> HANGE IN MAX VALUE TO ORIGINAL
        pbProgress.setMax(100);

        MainActivity.playBack.play(getContext(), url + "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J", pbLoading, ivPlayPause, pbProgress);

        ivUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Title", title);
                MainActivity.fragment.setArguments(bundle);
                ((PlayerCommunication)getActivity()).onOpenPlayerFragment();
            }
        });

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f) {
                    f = false;
                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    MainActivity.playBack.playCustom();
                } else {
                    f = true;
                    ivPlayPause.setImageResource(R.drawable.ic_play_arrow_red_a700_48dp);
                    MainActivity.playBack.pauseCustom();
                }
            }
        });

        tvSong.setText(title);

        return playerBarFragment;
    }



}

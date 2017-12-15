package com.agarwal.vinod.govindkigali.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.activities.FavActivity;
import com.agarwal.vinod.govindkigali.activities.PlayListActivity;
import com.agarwal.vinod.govindkigali.activities.RecentsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMusicFragment extends Fragment {

    TextView tvRecents, tvFav, tvPlaylists;
    public MyMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myMusicFragment = inflater.inflate(R.layout.fragment_mymusic, container, false);

        tvFav = myMusicFragment.findViewById(R.id.tv_fav);
        tvRecents = myMusicFragment.findViewById(R.id.tv_recently_played);
        tvPlaylists = myMusicFragment.findViewById(R.id.tv_play_list);

        tvFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FavActivity.class));
            }
        });

        tvPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PlayListActivity.class));
            }
        });

        tvRecents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RecentsActivity.class));
            }
        });

        return myMusicFragment;
    }

}

package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMusicFragment extends Fragment {

    CardView cvRecents, cvFav, cvPlayLists;
    public MyMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myMusicFragment = inflater.inflate(R.layout.fragment_mymusic, container, false);

        cvFav = myMusicFragment.findViewById(R.id.cv_fav);
        cvRecents = myMusicFragment.findViewById(R.id.cv_recents);
        cvPlayLists = myMusicFragment.findViewById(R.id.cv_play_lists);

        


        return myMusicFragment;
    }

}

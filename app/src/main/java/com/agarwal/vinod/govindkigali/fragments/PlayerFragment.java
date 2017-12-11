package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.PlayListAdapter;
import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    ImageView iv_more, iv_close;
    private ArrayList<Song> playList = new ArrayList<>();

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);

        View playerFragment = inflater.inflate(R.layout.fragment_player, container, false);

        iv_more = playerFragment.findViewById(R.id.iv_more);
        iv_close = playerFragment.findViewById(R.id.iv_close);
        TextSwitcher tvName = playerFragment.findViewById(R.id.tv_name);

        initData();
//        tvName.setFactory(new ViewSwitcher.ViewFactory() {
//            @Override
//            public View makeView() {
//                LayoutInflater inflater1 = LayoutInflater.from(getContext());
//
//            }
//        });

        PlayListAdapter adapter = new PlayListAdapter(playList, getContext());
        FeatureCoverFlow featureCoverFlow = playerFragment.findViewById(R.id.cf_list);

        featureCoverFlow.setAdapter(adapter);

//        featureCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
//            @Override
//            public void onScrolledToPosition(int position) {
//
//            }
//
//            @Override
//            public void onScrolling() {
//
//            }
//        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PlayerCommunication)getActivity()).onClosePlayerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(PlayerBarFragment.fragment)
                        .commit();
            }
        });

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(iv_more);
            }
        });


        return playerFragment;
    }

    private void initData() {
        playList.add(new Song("Unravel","https://images.shazam.com/coverart/t321637896-b1123768851_s400.jpg"));
        playList.add(new Song("Heroes", "https://images.shazam.com/coverart/t377195488-b1291768065_s400.jpg "));
        playList.add(new Song("Butterfly", "https://images.shazam.com/coverart/t362760493-b1253298937_s400.jpg"));
        playList.add(new Song("Resonance", "https://images.shazam.com/coverart/t370903022-b1270579223_s400.jpg"));
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();
    }

}

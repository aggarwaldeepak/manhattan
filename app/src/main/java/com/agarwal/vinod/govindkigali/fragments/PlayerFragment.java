package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    ImageView iv_more, iv_close;

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

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PlayerCommunication)getActivity()).onClosePlayerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(MainActivity.fragment)
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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();
    }

}

package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.fragments.mymusic.FavFragment;
import com.agarwal.vinod.govindkigali.fragments.mymusic.PlayListsFragment;
import com.agarwal.vinod.govindkigali.fragments.mymusic.RecentsFragment;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.agarwal.vinod.govindkigali.utils.Util;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMusicFragment extends Fragment {

    TextView tvRecents, tvFav, tvPlaylists,tvSeeAll;
    FragmentManager fragmentManager;
    public MyMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myMusicFragment = inflater.inflate(R.layout.fragment_mymusic, container, false);

        try{
            getActivity().setTitle(
                    Util.getLocalizedResources(getContext(),
                            new Locale(new PrefManager(getContext()).getUserLanguage()))
                            .getString(R.string.my_music_frag_title)
            );
        } catch (Exception e){
            getActivity().setTitle(getString(R.string.app_name));
        }


        tvFav = myMusicFragment.findViewById(R.id.tv_fav);
        tvRecents = myMusicFragment.findViewById(R.id.tv_recently_played);
        tvPlaylists = myMusicFragment.findViewById(R.id.tv_play_list);
        tvSeeAll = myMusicFragment.findViewById(R.id.tvSeeAllRecentlyPlayed);
        fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.containerRecentlyplayed, getNewRecentsFragment(SongAdapter.HORIZONTAL))
                .addToBackStack("remove")
                .commit();

        tvFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++MainActivity.fragmentCheck;
                fragmentManager.beginTransaction()
                        .replace(R.id.fg, new FavFragment())
                        .addToBackStack("remove")
                        .commit();
            }
        });

        tvPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++MainActivity.fragmentCheck;
                fragmentManager.beginTransaction()
                        .replace(R.id.fg, new PlayListsFragment())
                        .addToBackStack("remove")
                        .commit();
            }
        });

        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++MainActivity.fragmentCheck;
                fragmentManager.beginTransaction()
                        .replace(R.id.fg, new RecentsFragment())
                        .addToBackStack("remove")
                        .commit();
            }
        });

        return myMusicFragment;
    }

    RecentsFragment getNewRecentsFragment(int orientation) {
        RecentsFragment recentsFragment = new RecentsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SongAdapter.SHAPE,orientation);
        recentsFragment.setArguments(bundle);
        return recentsFragment;
    }

}

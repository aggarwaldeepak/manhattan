package com.agarwal.vinod.govindkigali.fragments;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.api.SongService;
import com.agarwal.vinod.govindkigali.models.Song;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static RecyclerView rvPlayList;
    public static final String TAG = "MAIN";
    public static ArrayList<Song> playlist = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        rvPlayList = mainFragment.findViewById(R.id.rv_playlist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPlayList.setLayoutManager(layoutManager);

        SongAdapter.PlayerInterface playerInterface = new SongAdapter.PlayerInterface() {
            @Override
            public void playSong(Integer value) {

                PlayerFragment fragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Value", value);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fg_main, fragment)
                        .commit();
            }
        };

        final SongAdapter adapter = new SongAdapter(getContext(), playerInterface);
        rvPlayList.setAdapter(adapter);

        if (playlist.size() == 0) {
            Log.d(TAG, "onCreateView: ======== Empty");
            SongService.getSongApi().getTracks().enqueue(new Callback<ArrayList<Song>>() {
                @Override
                public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                    playlist = response.body();
                    Log.d(TAG, "onResponse: " + playlist.get(0).getTitle());
                    adapter.updateTracks(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Song>> call, Throwable t) {

                }
            });
        } else {
            adapter.updateTracks(playlist);
        }

        return mainFragment;
    }
}

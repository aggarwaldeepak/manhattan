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

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    RecyclerView rvPlayList;
    RelativeLayout rlPlayer, rlPlayerOptions;
    private MediaPlayer mediaPlayer;
    ProgressBar pbLoading, pbProgress;
    ImageView ivPlayPause, ivUpArrow, ivPlay, ivNext, ivPrevious, ivClose;
    TextView tvSongName, tvTitle, tvStart, tvEnd;
    FrameLayout flPlayerOptions, ll;
    SeekBar sbProgress;
    LinearLayout llHead, llProgress;
    private String title = "", url = "", client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    private Integer duration = 0;
    Boolean f = false;
    public static final String TAG = "MAIN";
    public static ArrayList<Song> playlist = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainFragment = inflater.inflate(R.layout.fragment_main, container, false);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        pbLoading = mainFragment.findViewById(R.id.pb_loading);
        pbProgress = mainFragment.findViewById(R.id.pb_progress);
        ivPlayPause = mainFragment.findViewById(R.id.iv_play_pause);
        ivPlay = mainFragment.findViewById(R.id.iv_play);
        ivPrevious = mainFragment.findViewById(R.id.iv_previous);
        ivNext = mainFragment.findViewById(R.id.iv_next);
        ivUpArrow = mainFragment.findViewById(R.id.iv_up_arrow);
        ivUpArrow = mainFragment.findViewById(R.id.iv_up_arrow);
        ivClose = mainFragment.findViewById(R.id.iv_close);
        tvSongName = mainFragment.findViewById(R.id.tv_song);
        tvTitle = mainFragment.findViewById(R.id.tv_name);
        tvStart = mainFragment.findViewById(R.id.tv_start);
        tvEnd = mainFragment.findViewById(R.id.tv_end);
        rlPlayerOptions = mainFragment.findViewById(R.id.rl_player_options);
        flPlayerOptions = mainFragment.findViewById(R.id.fl_player_options);
        llHead = mainFragment.findViewById(R.id.ll_head);
        llProgress = mainFragment.findViewById(R.id.ll_progress);
        sbProgress = mainFragment.findViewById(R.id.sb_progress);
        rvPlayList = mainFragment.findViewById(R.id.rv_playlist);
        rlPlayer = mainFragment.findViewById(R.id.rl_player);

        rlPlayer.setVisibility(GONE);
        flPlayerOptions.setVisibility(GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPlayList.setLayoutManager(layoutManager);

        SongAdapter.PlayerInterface playerInterface = new SongAdapter.PlayerInterface() {
            @Override
            public void playSong(Integer value) {
                flPlayerOptions.setVisibility(View.VISIBLE);
                tvSongName.setText(playlist.get(value).getTitle());
                pbLoading.setVisibility(View.VISIBLE);
                ivPlayPause.setVisibility(GONE);
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(playlist.get(value).getStream_url() + client_id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        pbLoading.setVisibility(View.GONE);
                        ivPlayPause.setVisibility(View.VISIBLE);
                        ivPlay.setVisibility(View.VISIBLE);
                        ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                        ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                        mp.start();
                    }
                });
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

        ivUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlPlayer.setVisibility(View.VISIBLE);
                rvPlayList.setVisibility(GONE);
                flPlayerOptions.setVisibility(GONE);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvPlayList.setVisibility(View.VISIBLE);
                flPlayerOptions.setVisibility(View.VISIBLE);
                rlPlayer.setVisibility(GONE);
            }
        });

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f) {
                    f = false;
                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    mediaPlayer.start();
                } else {
                    f = true;
                    ivPlayPause.setImageResource(R.drawable.ic_play_arrow_red_a700_48dp);
                    ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    mediaPlayer.pause();
                }
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (f) {
                    f = false;
                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    mediaPlayer.start();
                } else {
                    f = true;
                    ivPlayPause.setImageResource(R.drawable.ic_play_arrow_red_a700_48dp);
                    ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    mediaPlayer.pause();
                }
            }
        });

        return mainFragment;
    }
}

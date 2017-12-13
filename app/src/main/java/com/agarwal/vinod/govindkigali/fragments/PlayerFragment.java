package com.agarwal.vinod.govindkigali.fragments;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.PlayListAdapter;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    RelativeLayout rlPlayer, rlPlayerOptions;
    private MediaPlayer mediaPlayer;
    ProgressBar pbLoading, pbProgress;
    ImageView ivPlayPause, ivUpArrow, ivPlay, ivNext, ivPrevious, ivClose, ivMore, ivFav;
    TextView tvSongName, tvTitle, tvStart, tvEnd;
    FrameLayout flPlayerOptions, ll;
    SeekBar sbProgress;
    LinearLayout llHead, llProgress;
    private String client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    private Integer value = 0;
    Boolean f = false;
    public static final String TAG = "PL";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference favRef = reference.child("fav");

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value = getArguments().getInt("Value");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View playerFragment = inflater.inflate(R.layout.fragment_player, container, false);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        pbLoading = playerFragment.findViewById(R.id.pb_loading);
        pbProgress = playerFragment.findViewById(R.id.pb_progress);
        ivPlayPause = playerFragment.findViewById(R.id.iv_play_pause);
        ivPlay = playerFragment.findViewById(R.id.iv_play);
        ivPrevious = playerFragment.findViewById(R.id.iv_previous);
        ivNext = playerFragment.findViewById(R.id.iv_next);
        ivUpArrow = playerFragment.findViewById(R.id.iv_up_arrow);
        ivUpArrow = playerFragment.findViewById(R.id.iv_up_arrow);
        ivClose = playerFragment.findViewById(R.id.iv_close);
        tvSongName = playerFragment.findViewById(R.id.tv_song);
        tvTitle = playerFragment.findViewById(R.id.tv_name);
        tvStart = playerFragment.findViewById(R.id.tv_start);
        tvEnd = playerFragment.findViewById(R.id.tv_end);
        ivMore = playerFragment.findViewById(R.id.iv_more);
        ivFav = playerFragment.findViewById(R.id.iv_fav);
        rlPlayerOptions = playerFragment.findViewById(R.id.rl_player_options);
        flPlayerOptions = playerFragment.findViewById(R.id.fl_player_options);
        llHead = playerFragment.findViewById(R.id.ll_head);
        llProgress = playerFragment.findViewById(R.id.ll_progress);
        sbProgress = playerFragment.findViewById(R.id.sb_progress);
        rlPlayer = playerFragment.findViewById(R.id.rl_player);

        rlPlayer.setVisibility(GONE);
        ivPlayPause.setVisibility(GONE);
        tvSongName.setText(MainFragment.playlist.get(value).getTitle());
        tvTitle.setText(MainFragment.playlist.get(value).getTitle());
        try {
            mediaPlayer.setDataSource(MainFragment.playlist.get(value).getStream_url() + client_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pbLoading.setVisibility(View.GONE);
                ivPlayPause.setVisibility(View.VISIBLE);
                ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                mp.start();
            }
        });


        ivUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlPlayer.setVisibility(View.VISIBLE);
                MainFragment.rvPlayList.setVisibility(GONE);
                flPlayerOptions.setVisibility(GONE);
            }
        });

        tvSongName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlPlayer.setVisibility(View.VISIBLE);
                MainFragment.rvPlayList.setVisibility(GONE);
                flPlayerOptions.setVisibility(GONE);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragment.rvPlayList.setVisibility(View.VISIBLE);
                flPlayerOptions.setVisibility(View.VISIBLE);
                rlPlayer.setVisibility(GONE);
            }
        });

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                favRef.child(MainFragment.playlist.get(value).getTitle()).setValue(MainFragment.playlist.get(value).getStream_url());
                favRef.child(MainFragment.playlist.get(value).getId()).setValue(MainFragment.playlist.get(value));
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

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value - 1 > 0) {
                    value = value - 1;
                    preparePlayer(value);
                } else {
                    value = MainFragment.playlist.size();
                    preparePlayer(value);
                }
                Log.d(TAG, "onClick: " + value);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value + 1 < MainFragment.playlist.size()) {
                    value = value + 1;
                    preparePlayer(value);
                } else {
                    value = 0;
                    preparePlayer(value);
                }
                Log.d(TAG, "onClick: " + value);
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

    @Override
    public void onDetach() {
        mediaPlayer.release();
        super.onDetach();
    }

    void preparePlayer(Integer pos) {
        tvSongName.setText(MainFragment.playlist.get(pos).getTitle());
        tvTitle.setText(MainFragment.playlist.get(pos).getTitle());
        pbLoading.setVisibility(View.VISIBLE);
        ivPlayPause.setVisibility(GONE);
        ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(MainFragment.playlist.get(pos).getStream_url() + client_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pbLoading.setVisibility(GONE);
                ivPlayPause.setVisibility(View.VISIBLE);
                ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                mp.start();
            }
        });
    }
}

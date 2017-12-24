package com.agarwal.vinod.govindkigali.fragments;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.agarwal.vinod.govindkigali.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    //CHECK THIS
    static RelativeLayout rlPlayer, rlPlayerOptions;
    static FrameLayout flPlayerOptions;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    ProgressBar pbLoading, pbProgress;
    ImageView ivPlayPause, ivUpArrow, ivPlay, ivNext, ivPrevious, ivClose, ivMore, ivFav, ivRepeat;
    TextView tvSongName, tvTitle, tvStart, tvEnd;
    SeekBar sbProgress;
    LinearLayout llHead, llProgress;
    private String client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    private Integer value = 0;
    Boolean f = false;
    Boolean repeat = false;
    Boolean manual = true;
    public static Boolean focus = true;
    public static final String TAG = "PL";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference favRef = reference.child("fav");
    DatabaseReference recentRef = reference.child("recents");
    private String ADDTOPLAYLIST = "Add to Playlist";
    int maxVolume;
    int curVolume;

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        Log.d(TAG, "onAudioFocusChange: =========================================>" );
                        f = false;
                        manual = false;
                        playPause();
                    } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        Log.d(TAG, "onAudioFocusChange: ******************************************>");
                        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(maxVolume * 0.1), 0);
                    } else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                        Log.d(TAG, "onAudioFocusChange: ???????????????????????????????????????????>");
                        f = true;
                        manual = false;
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume, 0);
                        playPause();
                    } else if (i == AudioManager.AUDIOFOCUS_LOSS) {
                        Log.d(TAG, "onAudioFocusChange: ------------------------------------------>");
                        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        f = false;
                        manual = false;
                        playPause();
                    }
                }
            };

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

//        //Getting all id's
//        pbLoading = playerFragment.findViewById(R.id.pb_loading);
//        pbProgress = playerFragment.findViewById(R.id.pb_progress);
//        ivPlayPause = playerFragment.findViewById(R.id.iv_play_pause);
//        ivPlay = playerFragment.findViewById(R.id.iv_play);
//        ivPrevious = playerFragment.findViewById(R.id.iv_previous);
//        ivNext = playerFragment.findViewById(R.id.iv_next);
//        ivUpArrow = playerFragment.findViewById(R.id.iv_up_arrow);
//        ivUpArrow = playerFragment.findViewById(R.id.iv_up_arrow);
//        ivClose = playerFragment.findViewById(R.id.iv_close);
//        tvSongName = playerFragment.findViewById(R.id.tv_song);
//        tvTitle = playerFragment.findViewById(R.id.tv_name);
//        tvStart = playerFragment.findViewById(R.id.tv_start);
//        tvEnd = playerFragment.findViewById(R.id.tv_end);
//        ivMore = playerFragment.findViewById(R.id.iv_more);
//        ivFav = playerFragment.findViewById(R.id.iv_fav);
//        ivRepeat= playerFragment.findViewById(R.id.iv_repeat);
//        rlPlayerOptions = playerFragment.findViewById(R.id.rl_player_options);
//        flPlayerOptions = playerFragment.findViewById(R.id.fl_player_options);
//        llHead = playerFragment.findViewById(R.id.ll_head);
//        llProgress = playerFragment.findViewById(R.id.ll_progress);
//        sbProgress = playerFragment.findViewById(R.id.sb_progress);
//        rlPlayer = playerFragment.findViewById(R.id.rl_player);
//
//        //Setting initial visibility modes
//        rlPlayer.setVisibility(GONE);
//        ivPlayPause.setVisibility(GONE);
//
//        //Assigning title of sing to textview
//        tvSongName.setText(MainFragment.playlist.get(value).getTitle());
//        tvTitle.setText(MainFragment.playlist.get(value).getTitle());
//
//        //Media player services
//        //Manager
//        //Completion Listener
//        releaseMediaPlayer();
//        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//
//        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//
//            Log.d(TAG, "onCreateView: ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::>>");
//            focus = true;
//            recentRef.child(MainFragment.playlist.get(value).getId()).setValue(MainFragment.playlist.get(value));
//            setFav(MainFragment.playlist.get(value).getId());
//            tvEnd.setText(String.valueOf(MainFragment.playlist.get(value).getDuration()/1000));
//            sbProgress.setMax(MainFragment.playlist.get(value).getDuration()/1000);
//            pbProgress.setMax(MainFragment.playlist.get(value).getDuration()/1000);
//
//
//            //Initializing media player object
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//            try {
//                mediaPlayer.setDataSource(MainFragment.playlist.get(value).getStream_url() + client_id);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mediaPlayer.prepareAsync();
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//
//                    //Changing visibility
//                    //as player loaded
//                    pbLoading.setVisibility(View.GONE);
//                    ivPlayPause.setVisibility(View.VISIBLE);
//                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
//                    ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
//
//                    //after preparing media-player
//                    //launching player to play music
//                    mp.start();
//                    Log.d(TAG, "onPrepare1d: 111111111111111111111");
//                }
//            });
//
//            final Handler mHandler = new Handler();
//            //Make sure you update Seekbar on UI thread
//            getActivity().runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    if(mediaPlayer != null){
//                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                        sbProgress.setProgress(mCurrentPosition);
//                        pbProgress.setProgress(mCurrentPosition);
//                        tvStart.setText(String.valueOf(mCurrentPosition));
//                    }
//                    mHandler.postDelayed(this, 1000);
//                }
//            });
//
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    if (!repeat) {
//                        if (value + 1 < MainFragment.playlist.size()) {
//                            value = value + 1;
//                            preparePlayer(value);
//                        } else {
//                            value = 0;
//                            preparePlayer(value);
//                        }
//                        Log.d(TAG, "onClick: OnCreateView:" + value);
//                    } else {
//                        preparePlayer(value);
//                    }
//                }
//            });
//        }
//
//
//        //For launching full screen player
//        ivUpArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlPlayer.setVisibility(View.VISIBLE);
//                MainFragment.rvPlayList.setVisibility(GONE);
//                flPlayerOptions.setVisibility(GONE);
//            }
//        });
//
//        //full screen player option to text-View to
//        tvSongName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlPlayer.setVisibility(View.VISIBLE);
//                MainFragment.rvPlayList.setVisibility(GONE);
//                flPlayerOptions.setVisibility(GONE);
//            }
//        });
//
//        //getting back to player bar(small player)
//        ivClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainFragment.rvPlayList.setVisibility(View.VISIBLE);
//                flPlayerOptions.setVisibility(View.VISIBLE);
//                rlPlayer.setVisibility(GONE);
//            }
//        });
//
//        //Pop up menu
//        ivMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup(view);
//            }
//        });
//
//        //fav goes to firebase as fav folder
//        ivFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                favRef.child(MainFragment.playlist.get(value).getId()).setValue(MainFragment.playlist.get(value));
//                ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
//            }
//        });
//
//        //now play pause button set-up
//        ivPlayPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manual = true;
//                playPause();
//            }
//        });
//
//        ivPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manual = true;
//                playPause();
//            }
//        });
//
//        ivPrevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playPrevious();
//            }
//        });
//
//        ivNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playNext();
//            }
//        });
//
//        ivRepeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (repeat) {
//                    repeat = false;
//                    ivRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
//                } else {
//                    repeat = true;
//                    ivRepeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
//                }
//            }
//        });
//
//        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if (mediaPlayer != null && b) {
//                    mediaPlayer.seekTo(i * 1000);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        return playerFragment;
    }

//    public void showPopup(View v) {
//        PopupMenu popup = new PopupMenu(getContext(), v);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.main, popup.getMenu());
//        popup.show();
//
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.d(TAG, "onMenuItemClick: " + item);
//                switch (item.getItemId()) {
//                    case R.id.navigation_add_to_playlist:
//                        CustomDialogClass cdd=new CustomDialogClass(getContext(), MainFragment.playlist.get(value));
//                        cdd.show();
//                        Log.d(TAG, "onMenuItemClick: jksahdkjsdhaksjhdaksjdhk");
//                        break;
//                }
//                return true;
//            }
//        });
//    }

    @Override
    public void onDetach() {
        if (mediaPlayer != null) {
            mediaPlayer.release();

            mediaPlayer = null;
        }
        super.onDetach();
    }

    void preparePlayer(Integer pos) {

//        releaseMediaPlayer();
//
//        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//
//            setFav(MainFragment.playlist.get(value).getId());
//            recentRef.child(MainFragment.playlist.get(value).getId()).setValue(MainFragment.playlist.get(value));
//            tvEnd.setText(String.valueOf(MainFragment.playlist.get(value).getDuration()/1000));
//            sbProgress.setMax(MainFragment.playlist.get(value).getDuration()/1000);
//            pbProgress.setMax(MainFragment.playlist.get(value).getDuration()/1000);
//
//            //Initializing media player object
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//            tvSongName.setText(MainFragment.playlist.get(pos).getTitle());
//            tvTitle.setText(MainFragment.playlist.get(pos).getTitle());
//            pbLoading.setVisibility(View.VISIBLE);
//            ivPlayPause.setVisibility(GONE);
//            ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
//            try {
//                mediaPlayer.setDataSource(MainFragment.playlist.get(pos).getStream_url() + client_id);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mediaPlayer.prepareAsync();
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    pbLoading.setVisibility(GONE);
//                    ivPlayPause.setVisibility(View.VISIBLE);
//                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
//                    ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
//                    mp.start();
//                    Log.d(TAG, "onPrepared2: 2222222222");
//                }
//            });
//
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    if (!repeat) {
//                        if (value + 1 < MainFragment.playlist.size()) {
//                            value = value + 1;
//                            preparePlayer(value);
//                        } else {
//                            value = 0;
//                            preparePlayer(value);
//                        }
//                        Log.d(TAG, "onClick: preparePlayer:" + value);
//                    } else {
//                        preparePlayer(value);
//                    }
//                }
//            });
//        }
    }

    void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            //Before playing new song  we have to release the player
            mediaPlayer.release();

            //And set player to null
            mediaPlayer = null;

            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    void playPause() {
        if (f) {
            f = false;
            ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
            ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
            if (manual) {
                int res = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if (res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    Log.d(TAG, "playPause: granted again :)" + audioFocusChangeListener);
                    mediaPlayer.start();
                }
            } else {
                mediaPlayer.start();
            }
        } else {
            f = true;
            ivPlayPause.setImageResource(R.drawable.ic_play_arrow_red_a700_48dp);
            ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
            if (manual) {
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                Log.d(TAG, "playPause: abondoned :)" + audioFocusChangeListener);
                mediaPlayer.pause();
            } else {
                mediaPlayer.pause();
            }
        }
//    }
//
//    void playNext() {
//        audioManager.abandonAudioFocus(audioFocusChangeListener);
//        if (value + 1 < MainFragment.playlist.size()) {
//            value = value + 1;
//            preparePlayer(value);
//        } else {
//            value = 0;
//            preparePlayer(value);
//        }
//        Log.d(TAG, "onClick: " + value);
//    }
//
//    void playPrevious() {
//        audioManager.abandonAudioFocus(audioFocusChangeListener);
//        if (value - 1 > 0) {
//            value = value - 1;
//            preparePlayer(value);
//        } else {
//            value = MainFragment.playlist.size() - 1;
//            preparePlayer(value);
//        }
//        Log.d(TAG, "onClick: " + value);
//    }
//
//    void setFav(final String id) {
//        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(id).exists()) {
//                    ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
//                } else {
//                    ivFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public static void hideIt() {
//        if (flPlayerOptions != null && rlPlayer != null) {
//            flPlayerOptions.setVisibility(View.VISIBLE);
//            rlPlayer.setVisibility(GONE);
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (!focus) {
//            audioManager.abandonAudioFocus(audioFocusChangeListener);
//        } else {
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    audioManager.abandonAudioFocus(audioFocusChangeListener);
//                    if (!repeat) {
//                        if (value + 1 < MainFragment.playlist.size()) {
//                            value = value + 1;
//                            preparePlayer(value);
//                        } else {
//                            value = 0;
//                            preparePlayer(value);
//                        }
//                        Log.d(TAG, "onClick: onPause:" + value);
//                    } else {
//                        preparePlayer(value);
//                    }
//                }
//            });
//        }
//        Log.d(TAG, "onPause: " + audioFocusChangeListener);
    }
}

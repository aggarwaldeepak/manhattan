package com.agarwal.vinod.govindkigali;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.fragments.MainFragment;
import com.agarwal.vinod.govindkigali.fragments.MyMusicFragment;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.fragments.SettingsFragment;
import com.agarwal.vinod.govindkigali.fragments.UpcomingFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.utils.BottomNavigationViewHelper;
import com.agarwal.vinod.govindkigali.utils.CustomDialogClass;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.agarwal.vinod.govindkigali.utils.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFY_PREVIOUS = "com.com.agarwal.vinod.govindkigali.previous";
    public static final String NOTIFY_CLOSE = "com.com.agarwal.vinod.govindkigali.close";
    public static final String NOTIFY_PLAY = "com.com.agarwal.vinod.govindkigali.play";
    public static final String NOTIFY_NEXT = "com.com.agarwal.vinod.govindkigali.next";

    RemoteViews simpleContentView;
    RemoteViews expandedView;
    Toolbar toolbar;
    Spinner spinnerToolbar;
    private ArrayList<Song> playList = new ArrayList<>();
    static SlidingUpPanelLayout slidingUpPanelLayout;
    SearchView searchView;
    RelativeLayout rlPlayer, rlPlayerOptions;
    FrameLayout flPlayerOptions;
    RecyclerView rvImage;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    ProgressBar pbLoading, pbProgress;
    ImageView ivPlayPause, ivUpArrow, ivPlay, ivNext, ivPrevious, ivMore, ivFav, ivRepeat, ivDownload;
    TextView tvSongName, tvStart, tvEnd, tvNotName;
    SeekBar sbProgress;
    LinearLayout llProgress;
    private String client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    private Integer value = 0;
    Boolean f = false;
    static Boolean repeat = false;
    Boolean manual = true;
    Boolean fav = true;
    public static Boolean focus = true;
    public static Integer fragmentCheck = 0;
    public static final String TAG = "MAIN";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference favRef = reference.child("fav");
    DatabaseReference recentRef = reference.child("recents");
    int maxVolume;
    int curVolume;
    ViewPager viewPager;
    PagerContainer container;
    BottomNavigationView navigation;
    NotificationManager mNotificationManager;
    Notification notification;
    String CHANNEL_ID = "player_goving_ki_gali";
    Integer NOTIFICATION_ID = 50891387;

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
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(curVolume * 0.5), 0);
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


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_play:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new MainFragment())
                            .commit();
                    return true;
                case R.id.navigation_thought:
                    return true;
                case R.id.navigation_upcoming:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new UpcomingFragment())
                            .commit();
                    return true;
                case R.id.navigation_my_music:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new MyMusicFragment())
                            .commit();
                    return true;
                case R.id.navigation_settings:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg,new SettingsFragment())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initiateNightMode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        spinnerToolbar = toolbar.findViewById(R.id.spinner_toolbar);
        spinnerToolbar.setVisibility(View.INVISIBLE);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        pbLoading = findViewById(R.id.pb_loading);
        pbProgress = findViewById(R.id.pb_progress);
        ivPlayPause = findViewById(R.id.iv_play_pause);
        ivPlay = findViewById(R.id.iv_play);
        ivPrevious = findViewById(R.id.iv_previous);
        ivNext = findViewById(R.id.iv_next);
        ivUpArrow = findViewById(R.id.iv_up_arrow);
        ivUpArrow = findViewById(R.id.iv_up_arrow);
        tvSongName = findViewById(R.id.tv_song);
        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);
        rvImage = findViewById(R.id.rv_song_image);
        ivMore = findViewById(R.id.iv_more);
        ivFav = findViewById(R.id.iv_fav);
        ivDownload = findViewById(R.id.iv_download);
        ivRepeat = findViewById(R.id.iv_repeat);
        rlPlayerOptions = findViewById(R.id.rl_player_options);
        flPlayerOptions = findViewById(R.id.fl_player_options);
        llProgress = findViewById(R.id.ll_progress);
        sbProgress = findViewById(R.id.sb_progress);
        rlPlayer = findViewById(R.id.rl_player);
        container = findViewById(R.id.pager_container);
        viewPager = container.getViewPager();
        navigation = findViewById(R.id.navigation);
        simpleContentView = new RemoteViews(getPackageName(), R.layout.layout_not_sm_player);
        expandedView = new RemoteViews(getPackageName(), R.layout.layout_not_player);

        setTitle(
                Util.getLocalizedResources(MainActivity.this,
                        new Locale(new PrefManager(MainActivity.this).getUserLanguage()))
                        .getString(R.string.app_name)
        );
        initiateFirstLaunch();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fg, new MainFragment())
                .commit();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.d(TAG, "onCreate: 111111111111111111111111111111111111111 ");
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            Log.d(TAG, "onCreate: 222222222222222222222222222222222222222");
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    ivPlayPause.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                    pbProgress.setVisibility(View.GONE);
                    ivUpArrow.setImageResource(R.drawable.ic_close_black_24dp);
                    ivMore.setVisibility(View.VISIBLE);
                    ivDownload.setVisibility(View.VISIBLE);
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    ivUpArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    ivDownload.setVisibility(View.GONE);
                    ivMore.setVisibility(View.GONE);
                    pbProgress.setVisibility(View.VISIBLE);
                    if (mediaPlayer != null && (mediaPlayer.isPlaying() || sbProgress.getProgress() != 0)) {
                        ivPlayPause.setVisibility(View.VISIBLE);
                    } else if (mediaPlayer != null) {
                        pbLoading.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("custom-message"));

        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_PLAY));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_NEXT));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_CLOSE));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_PREVIOUS));

//        SnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rvImage)
//        For launching full screen player
//        ivUpArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlPlayer.setVisibility(View.VISIBLE);
//                MainFragment.rvPlayList.setVisibility(GONE);
//                flPlayerOptions.setVisibility(GONE);
//            }
//        });

        //full screen player option to text-View to
//        tvSongName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlPlayer.setVisibility(View.VISIBLE);
//                MainFragment.rvPlayList.setVisibility(GONE);
//                flPlayerOptions.setVisibility(GONE);
//            }
//        });

        //getting back to player bar(small player)
//        ivClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainFragment.rvPlayList.setVisibility(View.VISIBLE);
//                flPlayerOptions.setVisibility(View.VISIBLE);
//                rlPlayer.setVisibility(GONE);
//            }
//        });

        //Pop up menu
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //fav goes to firebase as fav folder
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    if (!fav) {
                        fav = true;
                        favRef.child(SongAdapter.playList.get(value).getId()).setValue(SongAdapter.playList.get(value));
                        ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                    } else {
                        fav = false;
                        favRef.child(SongAdapter.playList.get(value).getId()).removeValue();
                        ivFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //now play pause button set-up
        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manual = true;
                playPause();
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manual = true;
                playPause();
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPrevious();
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNext();
            }
        });

        ivRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat) {
                    repeat = false;
                    ivRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
                } else {
                    repeat = true;
                    ivRepeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                }
            }
        });

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        //navigation.setSelectedItemId(R.id.navigation_upcoming); //TODO: REMOVE

    }

    private void initiateNightMode() {
        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isNightModeEnabled2()) {
            setTheme(R.style.FeedActivityThemeDark);
            Log.d(TAG, "initiateNightMode: NIGHT MODE ENABLED");
        } else {
            setTheme(R.style.FeedActivityThemeLight);
            Log.d(TAG, "initiateNightMode: NIGHT MODE DISABLED");
        }
    }

    private void initiateFirstLaunch() {

        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            setLanguageWithDialog(prefManager);
        } else {
            setLanguage(prefManager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Yooo", "onQueryTextSubmit: " + query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Yooo", "onQueryTextSubmit: " + newText);
                return false;
            }


        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notifications:
                break;
            case R.id.action_search:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else if (fragmentCheck > 0) {
            --fragmentCheck;
            MyMusicFragment.fragmentManager.popBackStack();
        } else {
            moveTaskToBack(true);
//            super.onBackPressed();
        }
    }


    public void setLanguageWithDialog(final PrefManager prefManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.languages);
        // Add the buttons
        builder.setPositiveButton(R.string.english, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("en");
                dialog.dismiss();
                recreate();
                /*Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();*/

            }
        });
        builder.setNegativeButton(R.string.hindi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                String languageToLoad = "hi"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("hi");
                dialog.dismiss();
               /* rEditor.putString("language", languageToLoad);
                rEditor.commit();*/


                recreate();
               /* Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();*/

            }
        });

        builder.create().show();
    }

    public void setLanguage(PrefManager prefManager) {
        String languageToLoad = prefManager.getUserLanguage();

        if (Locale.getDefault().getLanguage().equals(languageToLoad)) return;

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Integer pos = intent.getIntExtra("val", 0);
            Log.d(TAG, "onReceive: " + pos);

            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            preparePlayer(pos);
        }
    };

    void preparePlayer(final Integer pos) {
        value = pos;
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED
                || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            pbLoading.setVisibility(View.VISIBLE);
            ivPlayPause.setVisibility(GONE);
            ivMore.setVisibility(GONE);
            ivDownload.setVisibility(GONE);
        }
        ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);

        //Assigning title of sing to textview
        tvSongName.setText(SongAdapter.playList.get(value).getTitle());
        simpleContentView.setTextViewText(R.id.tv_not_name, SongAdapter.playList.get(value).getTitle());
        expandedView.setTextViewText(R.id.tv_not_name, SongAdapter.playList.get(value).getTitle());

        releaseMediaPlayer();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            Log.d(TAG, "onCreateView: ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::>>");
            focus = true;
            recentRef.child(SongAdapter.playList.get(value).getId()).setValue(SongAdapter.playList.get(value));
            setFav(SongAdapter.playList.get(value).getId());
            setRepeat();
            loadImage();
            Integer num = SongAdapter.playList.get(value).getDuration() / 1000;
            int hh = num / 3600;
            int mm = num / 60;
            int ss = num - (hh * 3600) - (mm * 60);
            String HH, MM, SS;
            if (hh > 10) {
                HH ="" + hh;
            } else {
                HH = "0" + hh;
            }
            if (mm > 10) {
                MM ="" + mm;
            } else {
                MM = "0" + mm;
            }
            if (ss > 10) {
                SS ="" + ss;
            } else {
                SS = "0" + ss;
            }
            String time;
            if (hh != 0) {
                time = HH + ":" + MM + ":" + SS;
            } else {
                time = MM + ":" + SS;
            }
            tvEnd.setText(time);
            Log.d(TAG, "preparePlayer: " + time);
//            tvEnd.setText(dateFormat.format(new Date(SongAdapter.playList.get(value).getDuration())));
            sbProgress.setMax(SongAdapter.playList.get(value).getDuration()/1000);
            pbProgress.setMax(SongAdapter.playList.get(value).getDuration()/1000);


            //Initializing media player object
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(SongAdapter.playList.get(value).getStream_url() + client_id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    //Changing visibility
                    //as player loaded
                    if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        pbLoading.setVisibility(View.GONE);
                        ivPlayPause.setVisibility(View.VISIBLE);
                    }
                    f = false;
                    generateNotification();
                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                    expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                    mNotificationManager.notify(NOTIFICATION_ID, notification);

                    //after preparing media-player
                    //launching player to play music
                    mp.start();
                    Log.d(TAG, "onPrepare1d: 111111111111111111111");
                }
            });

            final Handler mHandler = new Handler();
            //Make sure you update Seekbar on UI thread
            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int num = mediaPlayer.getCurrentPosition() / 1000;
                        sbProgress.setProgress(num);
                        pbProgress.setProgress(num);
                        int hh = num / 3600;
                        int mm = num / 60;
                        int ss = num - (hh * 3600) - (mm * 60);
                        String HH, MM, SS;
                        if (hh > 10) {
                            HH ="" + hh;
                        } else {
                            HH = "0" + hh;
                        }
                        if (mm > 10) {
                            MM ="" + mm;
                        } else {
                            MM = "0" + mm;
                        }
                        if (ss > 10) {
                            SS ="" + ss;
                        } else {
                            SS = "0" + ss;
                        }
                        String time;
                        if (hh != 0) {
                            time = HH + ":" + MM + ":" + SS;
                        } else {
                            time = MM + ":" + SS;
                        }
                        tvStart.setText(time);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        if (!repeat) {
                            if (value + 1 < SongAdapter.playList.size()) {
                                value = value + 1;
                                preparePlayer(value);
                            } else {
                                value = 0;
                                preparePlayer(value);
                            }
                            Log.d(TAG, "onClick: OnCreateView:" + value);
                        } else {
                            preparePlayer(value);
                        }
                    }else {
                        if (repeat) {
                            mediaPlayer.pause();
                            mediaPlayer.start();
                        } else {
                            f = true;
                            mediaPlayer.pause();
                            ivPlayPause.setImageResource(R.drawable.ic_play_arrow_red_a700_48dp);
                            ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                            simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                            expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                            mNotificationManager.notify(NOTIFICATION_ID, notification);
                        }
                        Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void releaseMediaPlayer() {
        if (mediaPlayer != null) {

            Log.d(TAG, "releaseMediaPlayer: ----------------------------------------------");
            //Before playing new song  we have to release the player
            mediaPlayer.release();

            //And set player to null
            mediaPlayer = null;

            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item);
                switch (item.getItemId()) {
                    case R.id.navigation_add_to_playlist:
                        CustomDialogClass cdd=new CustomDialogClass(MainActivity.this, SongAdapter.playList.get(value));
                        cdd.show();
                        Log.d(TAG, "onMenuItemClick: jksahdkjsdhaksjhdaksjdhk");
                        break;
                }
                return true;
            }
        });
    }

    void playPause() {
        Log.d(TAG, "playPause: " + f);
        if (mediaPlayer != null) {
            if (f) {
                f = false;
                ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                mNotificationManager.notify(NOTIFICATION_ID, notification);
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
                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                mNotificationManager.notify(NOTIFICATION_ID, notification);
                if (manual) {
                    audioManager.abandonAudioFocus(audioFocusChangeListener);
                    Log.d(TAG, "playPause: abondoned :)" + audioFocusChangeListener);
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.pause();
                }
            }
        }
    }

    void playNext() {
        if (mediaPlayer != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                if (value + 1 < SongAdapter.playList.size()) {
                    value = value + 1;
                    preparePlayer(value);
                } else {
                    value = 0;
                    preparePlayer(value);
                }
                Log.d(TAG, "onClick: " + value);
            } else {
                Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void playPrevious() {
        if (mediaPlayer != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                if (value - 1 > 0) {
                    value = value - 1;
                    preparePlayer(value);
                } else {
                    value = SongAdapter.playList.size() - 1;
                    preparePlayer(value);
                }
                Log.d(TAG, "onClick: " + value);
            } else {
                Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void setFav(final String id) {
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                    fav = true;
                } else {
                    fav = false;
                    ivFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getDetails());
            }
        });
    }

    void setRepeat() {
        if (!repeat) {
            ivRepeat.setImageResource(R.drawable.ic_repeat_white_24dp);
        } else {
            ivRepeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
        }
    }

    void generateNotification() {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Song name")
                .build();

        //Set Listeners
        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if (currentVersionSupportBigNotification()) {
            notification.bigContentView = expandedView;
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if(sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
            return true;
        }
        return false;
    }

    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent close = new Intent(NOTIFY_CLOSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);
        Log.d(TAG, "setListeners: entered in listener");

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pClose = PendingIntent.getBroadcast(getApplicationContext(), 0, close, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnClose, pClose);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);
    }

    void loadImage() {

//        ImageAdapter imageAdapter = new ImageAdapter(this, SongAdapter.playList);
//        rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        rvImage.setAdapter(imageAdapter);
        SongImageAdapter adapter = new SongImageAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(15);
        new CoverFlow.Builder()
                .with(viewPager)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                .scale(0.3f)
                .spaceSize(0f)
                .rotationY(0f)
                .build();
        container.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(MainActivity.this, "Fuck Bitch!!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (!focus) {
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ConnectivityManager cm =
                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();
                        if (isConnected) {
                            audioManager.abandonAudioFocus(audioFocusChangeListener);
                            if (!repeat) {
                                if (value + 1 < SongAdapter.playList.size()) {
                                    value = value + 1;
                                    preparePlayer(value);
                                } else {
                                    value = 0;
                                    preparePlayer(value);
                                }
                                Log.d(TAG, "onClick: OnCreateView:" + value);
                            } else {
                                preparePlayer(value);
                            }
                        } else {
                            if (repeat) {
                                mediaPlayer.pause();
                                mediaPlayer.start();
                            } else {
                                f = true;
                                mediaPlayer.pause();
                                ivPlayPause.setImageResource(R.drawable.ic_play_arrow_red_a700_48dp);
                                ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                                mNotificationManager.notify(NOTIFICATION_ID, notification);
                            }
                            Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            Log.d(TAG, "onPause: " + audioFocusChangeListener);
        }
    }

    void hideIt() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    public class SongImageAdapter extends PagerAdapter{


        LayoutInflater layoutInflater;
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_image,container,false);
            ImageView im = view.findViewById(R.id.iv_Image);
            container.addView(view);
//            int sze = value%SongAdapter.playList.size();
//            im.setImageResource(SongAdapter.playList.get(value);
            im.setImageResource(R.drawable.ic_dashboard_black_24dp);
            return view;
        }

        @Override
        public int getCount() {
            return SongAdapter.playList.size();
//            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()) {
                case NOTIFY_PLAY:
                    Toast.makeText(context, "Play/Pause", Toast.LENGTH_SHORT).show();
                    manual = true;
                    playPause();
                    break;

                case NOTIFY_NEXT:
                    Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show();
                    playNext();
                    break;

                case NOTIFY_PREVIOUS:
                    Toast.makeText(context, "Previous", Toast.LENGTH_SHORT).show();
                    playPrevious();
                    break;

                case NOTIFY_CLOSE:
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    mNotificationManager.cancel(NOTIFICATION_ID);
                    break;
            }
            Toast.makeText(context, "Receiver :) ", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNotificationManager != null) {
            Log.d(TAG, "onDestroy:  in  player");
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
        Log.d(TAG, "onDestroy: in destroy");
    }
}

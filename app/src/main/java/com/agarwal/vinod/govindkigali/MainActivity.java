package com.agarwal.vinod.govindkigali;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.adapters.SongImageAdapter;
import com.agarwal.vinod.govindkigali.fragments.MainFragment;
import com.agarwal.vinod.govindkigali.fragments.MyMusicFragment;
import com.agarwal.vinod.govindkigali.fragments.SettingsFragment;
import com.agarwal.vinod.govindkigali.fragments.ThoughtFragment;
import com.agarwal.vinod.govindkigali.fragments.UpcomingFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.DownloadMusic;
import com.agarwal.vinod.govindkigali.playerUtils.ImageLoader;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
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

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements PlayerCommunication {

    public static final String NOTIFY_PREVIOUS = "com.com.agarwal.vinod.govindkigali.previous";
    public static final String NOTIFY_CLOSE = "com.com.agarwal.vinod.govindkigali.close";
    public static final String NOTIFY_PLAY = "com.com.agarwal.vinod.govindkigali.play";
    public static final String NOTIFY_NEXT = "com.com.agarwal.vinod.govindkigali.next";

    RemoteViews simpleContentView;
    RemoteViews expandedView;
    Toolbar toolbar;
    Spinner spinnerToolbar;
    SlidingUpPanelLayout slidingUpPanelLayout;
    SearchView searchView;
    RelativeLayout rlPlayer, rlPlayerOptions;
    FrameLayout flPlayerOptions;
    public PlayerService service;
    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;
    public ProgressBar pbLoading, pbProgress;
//    private DownloadMusic downloadMusic;
    public ImageView ivPlayPause, ivUpArrow, ivPlay, ivNext, ivPrevious, ivRepeat, ivFav;
//    ivMore, ivDownload;
    public TextView tvSongName, tvStart, tvEnd;
    public LinearLayout llProgress;
    public String client_id = "?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J";
    public Integer value = 0;
    public Boolean f = false;
    public Boolean manual = true;
    boolean mBound = false;
    public Boolean fav = true;
    public static Boolean focus = true;
    static Boolean repeat = false;
    public static Integer fragmentCheck = 0;
    public static final String TAG = "MAIN";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference favRef = reference.child("fav");
    DatabaseReference recentRef = reference.child("recents");
    int maxVolume;
    int curVolume;
    String filePath;
    File fileTemp;
    File file;
    RecyclerView recyclerView;
    SongImageAdapter adapter;
    BottomNavigationView navigation;
    NotificationManager mNotificationManager;
    Notification notification;
    DiscreteSeekBar discreteSeekBar;
    MainFragment mainFragment;
    public ArrayList<Song> playlist = new ArrayList<>();
    String CHANNEL_ID = "player_goving_ki_gali";
    Integer NOTIFICATION_ID = 50891387;

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        Log.d(TAG, "onAudioFocusChange: =========================================>");
                        f = false;
                        manual = false;
                        playPause();
                    } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        Log.d(TAG, "onAudioFocusChange: ******************************************>");
                        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (curVolume * 0.5), 0);
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
                    mainFragment = new MainFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, mainFragment)
                            .commit();
                    return true;
                case R.id.navigation_thought:
                    hideIt();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg,new ThoughtFragment())
                            .commit();
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
                            .replace(R.id.fg, new SettingsFragment())
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
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

//        Log.d(TAG, "onCreate: " + getFilesDir() + "");
//        Log.d(TAG, "onCreate: " + getDir("darsh.txt", MODE_PRIVATE));

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
//        ivMore = findViewById(R.id.iv_more);
        ivFav = findViewById(R.id.iv_fav);
//        ivDownload = findViewById(R.id.iv_download);
        ivRepeat = findViewById(R.id.iv_repeat);
        rlPlayerOptions = findViewById(R.id.rl_player_options);
        flPlayerOptions = findViewById(R.id.fl_player_options);
        recyclerView = findViewById(R.id.rv_song_image);
        llProgress = findViewById(R.id.ll_progress);
//        sbProgress = findViewById(R.id.sb_progress);
        discreteSeekBar = findViewById(R.id.dsb_progress);
        rlPlayer = findViewById(R.id.rl_player);
//        container = findViewById(R.id.pager_container);
//        viewPager = container.getViewPager();
        navigation = findViewById(R.id.navigation);
        simpleContentView = new RemoteViews(getPackageName(), R.layout.layout_not_sm_player);
        expandedView = new RemoteViews(getPackageName(), R.layout.layout_not_player);

//        if (recreate) {
//            releaseMediaPlayer();
//            Log.d(TAG, "onCreate: HIDE");
//            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//        }

        setTitle(
                Util.getLocalizedResources(MainActivity.this,
                        new Locale(new PrefManager(MainActivity.this).getUserLanguage()))
                        .getString(R.string.app_name)
        );
        initiateFirstLaunch();

        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fg, mainFragment)
                .commit();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.d(TAG, "onCreate: 111111111111111111111111111111111111111 ");
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            Log.d(TAG, "onCreate: 222222222222222222222222222222222222222");
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }

        Log.d("SL", "onCreate: " + slidingUpPanelLayout.getPanelState());

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
                    ivUpArrow.setImageResource(R.drawable.ic_close_white_24dp);
//                    ivMore.setVisibility(View.VISIBLE);
//                    ivDownload.setVisibility(View.VISIBLE);
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    ivUpArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);
//                    ivDownload.setVisibility(View.GONE);
//                    ivMore.setVisibility(View.GONE);
                    pbProgress.setVisibility(View.VISIBLE);
                    if (mediaPlayer != null && (mediaPlayer.isPlaying() || discreteSeekBar.getProgress() != 0)) {
                        ivPlayPause.setVisibility(View.VISIBLE);
                    } else if (mediaPlayer != null) {
                        pbLoading.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        adapter = new SongImageAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

//        LocalBroadcastManager.getInstance(this).
//                registerReceiver(receiver, new IntentFilter("custom-message"));

        LocalBroadcastManager.getInstance(this).
                registerReceiver(imageReceiver, new IntentFilter("custom-image"));

        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_PLAY));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_NEXT));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_CLOSE));
        registerReceiver(playerReceiver, new IntentFilter(NOTIFY_PREVIOUS));

        //Pop up menu
//        ivMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup(view);
//            }
//        });

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
                        favRef.child(playlist.get(value).getId()).setValue(playlist.get(value));
                        ivFav.setImageResource(R.drawable.ic_favorite_white_24dp);
                    } else {
                        fav = false;
                        favRef.child(playlist.get(value).getId()).removeValue();
                        ivFav.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        ivDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Starting Download", Toast.LENGTH_SHORT).show();
//                file = new File(getDir("music", MODE_PRIVATE) + "/" + playlist.get(value).getId());
//                new DownloadMusic(MainActivity.this).execute(file.getPath(), playlist.get(value).getStream_url() + client_id);
//            }
//        });

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

        discreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, final int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
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
                /*if(mainFragment != null) {
                    mainFragment.setSongAdapterFilter(query);
                }*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Yooo", "onQueryTextSubmit: " + newText);
                if(mainFragment != null) {
                    mainFragment.setSongAdapterFilter(newText);
                }
                return true;
            }



        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(mainFragment != null) {
                    mainFragment.setSongAdapterFilter("");
                }
                return true;
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
        } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (fragmentCheck > 0) {
            --fragmentCheck;
            getSupportFragmentManager().popBackStack();
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
//                recreate = true;
//                getSupportFragmentManager().beginTransaction()
//                        .remove(mainFragment)
//                        .commit();
//<<<<<<< HEAD
                /*Intent i = new Intent("recreate");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(i);*/
//                recreate();
//=======
//                Intent i = new Intent("recreate");
//                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(i);


                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
//                recreate();
//>>>>>>> player
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
//                getSupportFragmentManager().beginTransaction()
//                        .remove(mainFragment)
//                        .commit();
//                recreate = true;
//<<<<<<< HEAD
                /*Intent i = new Intent("recreate");
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(i);*/
//                recreate();
//=======
//                Intent i = new Intent("recreate");
//                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(i);


                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
//                recreate();
//>>>>>>> player
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
//<<<<<<< HEAD
       /* Intent i = new Intent("recreate");
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);*/
//=======
//        Intent i = new Intent("recreate");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
//>>>>>>> player
//        recreate = true;
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
//        recreate();
    }

    public BroadcastReceiver imageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            releaseMediaPlayer();
            Integer pos = intent.getIntExtra("val", 0);
            Log.d(TAG, "onReceive: " + pos);
            preparePlayer(pos);
        }
    };

    void preparePlayer(final Integer pos) {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED
                || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            pbLoading.setVisibility(View.VISIBLE);
            ivPlayPause.setVisibility(GONE);
//            ivMore.setVisibility(GONE);
//            ivDownload.setVisibility(GONE);
        }

        //taking download music instance
//        downloadMusic = new DownloadMusic();

        Log.d("SL", "preparePlayer: " + slidingUpPanelLayout.getPanelState());

        ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);

        //Assigning title of sing to textview
        tvSongName.setText(playlist.get(pos).getTitle());
        simpleContentView.setTextViewText(R.id.tv_not_name, playlist.get(pos).getTitle());
        expandedView.setTextViewText(R.id.tv_not_name, playlist.get(pos).getTitle());

        String imageurl = playlist.get(pos).getArtwork_url();
        if (imageurl != null) {
            new ImageLoader(this).execute(imageurl);
        }

        releaseMediaPlayer();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        assert audioManager != null;
        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            Log.d(TAG, "onCreateView: ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::>>");
            focus = true;
            recentRef.child(playlist.get(pos).getId()).setValue(playlist.get(pos));
            setFav(playlist.get(pos).getId());
            setRepeat();
            loadImage();
            Integer num = playlist.get(pos).getDuration() / 1000;
            int hh = num / 3600;
            int mm = num / 60 - (hh * 60);
            Log.d(TAG, "preparePlayer: " + hh + " ==== " + mm);
            int ss = num - (hh * 3600) - (mm * 60);
            String HH, MM, SS;
            if (hh >= 10) {
                HH = "" + hh;
            } else {
                HH = "0" + hh;
            }
            if (mm >= 10) {
                MM = "" + mm;
            } else {
                MM = "0" + mm;
            }
            if (ss >= 10) {
                SS = "" + ss;
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
//            sbProgress.setMax(SongAdapter.playList.get(value).getDuration()/1000);
            discreteSeekBar.setMax(playlist.get(pos).getDuration() / 1000);
            pbProgress.setMax(playlist.get(pos).getDuration() / 1000);


            //Initializing media player object
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//            fileTemp = new File(getDir("temp", MODE_PRIVATE) + "/" + playlist.get(pos).getId());
//            file = new File(getDir("music", MODE_PRIVATE) + "/" + playlist.get(pos).getId());
//            if (file.exists()) {
//                filePath = file.getPath();
//                Log.d(TAG, "preparePlayer: Path Exists");
//            } else {
//                filePath = fileTemp.getPath();
////                fileTemp.deleteOnExit();
//                new DownloadPermMusic().execute(file.getPath(), playlist.get(pos).getStream_url() + client_id);
//                downloadMusic.execute(filePath, playlist.get(pos).getStream_url() + client_id, "0");
//                Log.d(TAG, "preparePlayer: Do not exist");
//            }
//            Log.d(TAG, "preparePlayer: " + filePath);
//            Handler handler = new Handler();
//            Runnable r = new Runnable() {
//                public void run() {
//                    //what ever you do here will be done after 3 seconds delay.
//            try {

            mediaPlayer.reset();
//            try {
////                provideDataSource(mediaPlayer, pos);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//                mediaPlayer.setDataSource(filePath);
//                Log.d(TAG, "preparePlayer: providing filepath");
                //                mediaPlayer.setDataSource(SongAdapter.playList.get(pos).getStream_url() + client_id);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //            mediaPlayer.prepareAsync();

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
                    adapter.updateImage(playlist);
                    ivPlayPause.setImageResource(R.drawable.ic_pause_white_48dp);
                    ivPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                    expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_pause_white_48dp);
                    if (mNotificationManager != null) {
                        mNotificationManager.notify(NOTIFICATION_ID, notification);
                    }
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
                     if (mediaPlayer != null) {
                         int num = mediaPlayer.getCurrentPosition() / 1000;
                         //                        sbProgress.setProgress(num);
                         discreteSeekBar.setProgress(num);
                         pbProgress.setProgress(num);
                         int hh = num / 3600;
                         int mm = num / 60 - (hh * 60);
                         int ss = num - (hh * 3600) - (mm * 60);
                         String HH, MM, SS;
                         if (hh >= 10) {
                             HH = "" + hh;
                         } else {
                             HH = "0" + hh;
                         }
                         if (mm >= 10) {
                             MM = "" + mm;
                         } else {
                             MM = "0" + mm;
                         }
                         if (ss >= 10) {
                             SS = "" + ss;
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
                     if (discreteSeekBar.getProgress() < playlist.get(pos).getDuration()) {
                         return;
                     }
                     ConnectivityManager cm =
                             (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                     NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        if (!repeat) {
                            if (value + 1 < playlist.size()) {
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
                            ivPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                            ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                            simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                            expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                            if (mNotificationManager != null) {
                                mNotificationManager.notify(NOTIFICATION_ID, notification);
                            }
                        }
                        Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//                }
//            };
//            if (file.exists()) {
//                handler.postDelayed(r, 0);
//            } else {
//                handler.postDelayed(r, 3000);
//            }
        }
        Log.d("SL", "preparePlayer: " + slidingUpPanelLayout.getPanelState());

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
                        CustomDialogClass cdd = new CustomDialogClass(MainActivity.this, playlist.get(value), MainActivity.this);
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
                if (mNotificationManager != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
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
                ivPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
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
//                downloadMusic.cancel(true);

                if (value + 1 < playlist.size()) {
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
//                downloadMusic.cancel(true);
                audioManager.abandonAudioFocus(audioFocusChangeListener);
                if (value - 1 > 0) {
                    value = value - 1;
                    preparePlayer(value);
                } else {
                    value = playlist.size() - 1;
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
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        //Set Listeners
        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if (currentVersionSupportBigNotification()) {
            notification.bigContentView = expandedView;
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
        recyclerView.scrollToPosition(value);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (!focus) {
//            audioManager.abandonAudioFocus(audioFocusChangeListener);
//        } else {
//            if (mediaPlayer != null) {
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        ConnectivityManager cm =
//                                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//                        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                        boolean isConnected = activeNetwork != null &&
//                                activeNetwork.isConnectedOrConnecting();
//                        if (isConnected) {
//                            audioManager.abandonAudioFocus(audioFocusChangeListener);
//                            if (!repeat) {
//                                if (value + 1 < playlist.size()) {
//                                    value = value + 1;
//                                    preparePlayer(value);
//                                } else {
//                                    value = 0;
//                                    preparePlayer(value);
//                                }
//                                Log.d(TAG, "onClick: OnCreateView:" + value);
//                            } else {
//                                preparePlayer(value);
//                            }
//                        } else {
//                            if (repeat) {
//                                mediaPlayer.pause();
//                                mediaPlayer.start();
//                            } else {
//                                f = true;
//                                mediaPlayer.pause();
//                                ivPlayPause.setImageResource(R.drawable.ic_play_arrow_white_48dp);
//                                ivPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
//                                simpleContentView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
//                                expandedView.setImageViewResource(R.id.btnPlay, R.drawable.ic_play_arrow_white_48dp);
//                                if (mNotificationManager != null) {
//                                    mNotificationManager.notify(NOTIFICATION_ID, notification);
//                                }
//                            }
//                            Toast.makeText(MainActivity.this, "Internet not available!!!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//            Log.d(TAG, "onPause: " + audioFocusChangeListener);
//        }
//    }

    void hideIt() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

//    public class SongImageAdapter extends PagerAdapter{
//
//        Integer pos;
//        SongImageAdapter (Integer pos) {
//            this.pos = pos;
//        }
//        LayoutInflater layoutInflater;
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            View view = layoutInflater.inflate(R.layout.layout_image,container,false);
//            ImageView im = view.findViewById(R.id.iv_Image);
//            container.addView(view);
//            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.placeholder(R.drawable.photo);
//            requestOptions.error(R.drawable.photo);
//            requestOptions.centerCrop();
//            Glide.with(MainActivity.this).load(SongAdapter.playList.get(position).getArtwork_url()).apply(requestOptions).into(im);
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return SongAdapter.playList.size();
////            return Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//    }

    public BroadcastReceiver playerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
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
    public void uploadImage(Bitmap bitmap) {
        simpleContentView.setImageViewBitmap(R.id.iv_not_image, bitmap);
        expandedView.setImageViewBitmap(R.id.iv_not_image, bitmap);
    }

    /** Interface function to get playlist and song id/number to play song */
    @Override
    public void playSong(ArrayList<Song> playlist, Integer value) {
        Log.d(TAG, "playSong: ---------------------------------------");

        //If Service is bounded correctly
        if (mBound) {
            //setting panel to COLLAPSED STATE
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            //Providing playlist to service
            service.updateplayList(playlist);

            //Calling service method to start playing song
            service.createPlayer(value);

        } else {

            //If not bounded correctly
            Log.d(TAG, "playSong: not binding");
        }
    }

    @Override
    public void onDestroy() {
        releaseMediaPlayer();
        if (mNotificationManager != null) mNotificationManager.cancel(NOTIFICATION_ID);
        focus = true;
        repeat = false;
        fragmentCheck = 0;
        unregisterReceiver(playerReceiver);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        slidingUpPanelLayout = null;
        Log.d(TAG, "recreate: called :) :) :) :) :) :)");
        super.onDestroy();
    }

//    /** Function to provide data source to mediaplayer */
//    public void provideDataSource(MediaPlayer mediaPlayer, Integer pos) throws IOException {
//        mediaPlayer.setDataSource(playlist.get(pos).getStream_url() + client_id);
//        mediaPlayer.prepareAsync();
//    }

    public String calculateTime() {
        return null;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder mService) {

            // We've bound to PlayerService, cast the IBinder and get PlayerService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) mService;
            service = binder.getService();

            //Passing activity instance to PlayerService
            service = new PlayerService(MainActivity.this);

            //Check value to true
            mBound = true;

            Log.d(TAG, "onServiceConnected: Binding service");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /** Creating Intent for service and calling bind service in onStart() */
    @Override
    protected void onStart() {
        super.onStart();

        //Creating Intent for service
        Intent intent = new Intent(this, PlayerService.class);

        //TODO: If background possible by startService()
        //startService(intent);

        //Binding service
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
}


package com.agarwal.vinod.govindkigali;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.Utils.BottomNavigationViewHelper;
import com.agarwal.vinod.govindkigali.fragments.PlayerBarFragment;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements PlayerCommunication {

    public static Toolbar toolbar;
    Button btPlay;
    SearchView searchView;
//    ProgressBar pb_loading, pb_progress;
//    ImageView iv_play_pause, iv_up_arrow;
//    public static Fragment fragment;
//    Boolean f = false;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_play:
                    return true;
                case R.id.navigation_thought:
                    return true;
                case R.id.navigation_upcoming:
                    return true;
                case R.id.navigation_my_music:
                    return true;
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        pb_loading = findViewById(R.id.pb_loading);
//        pb_progress = findViewById(R.id.pb_progress);
//        iv_play_pause = findViewById(R.id.iv_play_pause);
//        iv_up_arrow = findViewById(R.id.iv_up_arrow);
        btPlay = findViewById(R.id.bt_play);
//        fragment = new PlayerFragment();


        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fg, new PlayerBarFragment())
                        .commit();
            }
        });

//        iv_up_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.main_layout, fragment)
//                        .commit();
//                toolbar.setVisibility(View.GONE);
//            }
//        });
//
//        pb_loading.setVisibility(GONE);
//        pb_progress.setProgress(50);
//
//        iv_play_pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!f) {
//                    f = true;
//                    iv_play_pause.setVisibility(GONE);
//                    pb_loading.setVisibility(View.VISIBLE);
//                } else {
//                    f = false;
//                    iv_play_pause.setImageResource(R.drawable.ic_pause_white_48dp);
//                    iv_play_pause.setVisibility(View.VISIBLE);
//                    pb_loading.setVisibility(GONE);
//                }
//            }
//        });
//
//        pb_loading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iv_play_pause.setVisibility(View.VISIBLE);
//                pb_loading.setVisibility(GONE);
//            }
//        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);


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
        switch (item.getItemId()){
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
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClosePlayerFragment() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onOpenPlayerFragment() {
        toolbar.setVisibility(GONE);
    }
}

package com.agarwal.vinod.govindkigali;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agarwal.vinod.govindkigali.Utils.BottomNavigationViewHelper;
import com.agarwal.vinod.govindkigali.Utils.MediaPlayBack;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.api.SongService;
import com.agarwal.vinod.govindkigali.fragments.PlayerBarFragment;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements PlayerCommunication {

    Toolbar toolbar;
    RecyclerView rvPLayList;
    SearchView searchView;
    public static PlayerFragment fragment;
    public static MediaPlayBack playBack;

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


        playBack = new MediaPlayBack();
        playBack.initiate(this);
        fragment = new PlayerFragment();

        rvPLayList = findViewById(R.id.rv_playlist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPLayList.setLayoutManager(layoutManager);

        final SongAdapter adapter = new SongAdapter(this, this);
        rvPLayList.setAdapter(adapter);

        SongService.getSongApi().getTracks().enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                adapter.updateTracks(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {

            }
        });

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
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void onOpenPlayerFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, fragment)
                .commit();
        toolbar.setVisibility(GONE);
    }

    @Override
    public void playSong(Song song) {
        PlayerBarFragment fragmentBar = new PlayerBarFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title", song.getTitle());
        bundle.putString("Stream", song.getStream_url());
        bundle.putInt("Duration", song.getDuration());
        fragmentBar.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fg, fragmentBar)
                        .commit();
    }
}

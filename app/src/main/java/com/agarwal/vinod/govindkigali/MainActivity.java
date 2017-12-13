package com.agarwal.vinod.govindkigali;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agarwal.vinod.govindkigali.fragments.MainFragment;
import com.agarwal.vinod.govindkigali.fragments.MyMusicFragment;
import com.agarwal.vinod.govindkigali.fragments.UpcomingFragment;
import com.agarwal.vinod.govindkigali.utils.BottomNavigationViewHelper;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.agarwal.vinod.govindkigali.utils.Util;
import com.agarwal.vinod.govindkigali.utils.MediaPlayBack;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.api.SongService;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new MainFragment())
                            .commit();
                    return true;
                case R.id.navigation_thought:
                    return true;
                case R.id.navigation_upcoming:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new UpcomingFragment())
                            .commit();
                    return true;
                case R.id.navigation_my_music:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fg, new MyMusicFragment())
                            .commit();
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

        setTitle(
                Util.getLocalizedResources(MainActivity.this,
                        new Locale(new PrefManager(MainActivity.this).getUserLanguage()))
                        .getString(R.string.app_name)
        );
        initiateFirstLaunch();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fg, new MainFragment())
                .commit();

//        playBack = new MediaPlayBack();
//        playBack.initiate(this);
//        fragment = new PlayerFragment();
//
//        rvPLayList = findViewById(R.id.rv_playlist);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        rvPLayList.setLayoutManager(layoutManager);
//
//        final SongAdapter adapter = new SongAdapter(this, this);
//        rvPLayList.setAdapter(adapter);
//
//        SongService.getSongApi().getTracks().enqueue(new Callback<ArrayList<Song>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
//                adapter.updateTracks(response.body());
////>>>>>>> darsh
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
//
//            }
//        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);


    }

    private void initiateFirstLaunch() {

        PrefManager prefManager = new PrefManager(this);
        if(prefManager.isFirstTimeLaunch()){
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


    public void setLanguageWithDialog(final PrefManager prefManager){
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

    public void setLanguage(PrefManager prefManager){
        String languageToLoad = prefManager.getUserLanguage();

        if(Locale.getDefault().getLanguage().equals(languageToLoad))return;

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

}

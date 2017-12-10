package com.agarwal.vinod.govindkigali;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.Utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_DURATION = 1000;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_play:
                    mTextMessage.setText(R.string.title_play);
                    return true;
                case R.id.navigation_thought:
                    mTextMessage.setText(R.string.title_thought);
                    return true;
                case R.id.navigation_upcoming:
                    mTextMessage.setText(R.string.title_upcoming);
                    return true;
                case R.id.navigation_my_music:
                    mTextMessage.setText(R.string.title_my_music);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_settings);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showSplash();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }

    private void showSplash() {
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_DURATION);*/
    }



}

package com.agarwal.vinod.govindkigali.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.agarwal.vinod.govindkigali.R;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by darsh on 12/12/17.
 */

public class MediaPlayBack {

    private MediaPlayer mediaPlayer;

    public void initiate(Context context) {
        context = context;
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//        mediaPlayer.prepareAsync();
    }

    public void play(Context context, String url, final ProgressBar pb, final ImageView iv, final ProgressBar pbProgress) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        Log.d("HI", url);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pb.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_pause_white_48dp);
                mp.start();
                Log.d("HI", "CHECK========<<");
            }
        });
        final Handler mHandler = new Handler();
        //Make sure you update Seekbar on UI thread
        ((Activity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    Log.d("HI", mCurrentPosition + "");
                    pbProgress.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    public void playCustom() {
            Log.d("HI", "CHECK========>>");
            mediaPlayer.start();
//        }

    }

    public void pauseCustom() {
        mediaPlayer.pause();
    }

}

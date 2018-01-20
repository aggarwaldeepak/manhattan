package com.agarwal.vinod.govindkigali.playerUtils;

/**
 * Created by darsh on 24/12/17.
 */

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by darsh on 30/12/17.
 */

public class DownloadMusic extends Service {

    public DownloadMusic(){
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(MainActivity.TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MainActivity.TAG, "onStartCommand: ");
        Log.d(MainActivity.TAG, "doInBackground: Downloading + playing music");
        String path = intent.getStringExtra("path");
        String urlString = intent.getStringExtra("url");
        try {
            RandomAccessFile file = new RandomAccessFile(path, "rw");
            //TODO: MAKE Activity run even after user kills the app
            String CHANNEL_ID = "download govin ki gali";
            Integer id = 50891350;
            NotificationManager mNotifyManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID);
            notificationBuilder.setContentText("Download in progress")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true);
            URL url = new URL(urlString);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            long lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
            byte data[] = new byte[1024];
            long total = 0, count = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                notificationBuilder.setProgress(100, (int)((total*100)/lenghtOfFile), false);
                Log.d(MainActivity.TAG, "doInBackground: " + (int)((total*100)/lenghtOfFile));
                // Displays the progress bar for the first time.
                mNotifyManager.notify(id, notificationBuilder.build());
                file.write(data);
                Log.d("ANDRO_ASYNC", "doInBackground: " + total);
            }
            file.close();
            mNotifyManager.cancel(id);
            Log.d(MainActivity.TAG, "doInBackground: Download complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Log.d(MainActivity.TAG, "Download Service Destroyed");
    }
}


package com.agarwal.vinod.govindkigali.playerUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.agarwal.vinod.govindkigali.MainActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by darsh on 24/12/17.
 */
public class DownloadPermMusic extends AsyncTask<String, Void, Void> {

    public static final String TAG = "PER_MUS";
    @Override
    protected Void doInBackground(String... strings) {
        Log.d(MainActivity.TAG, "doInBackground: Downloading + playing music");
        try {
            RandomAccessFile file = new RandomAccessFile(strings[0], "rw");
            URL url = new URL(strings[1]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            Log.d(TAG, "Lenght of file: " + lenghtOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
//                OutputStream file = new FileOutputStream(strings[0]);
            byte data[] = new byte[1024];
            long total = 0, count = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
//                    publishProgress(""+(int)((total*100)/lenghtOfFile));125010337
                file.write(data);
                Log.d(TAG, "doInBackground: " + total);
            }
            file.close();
            Log.d(TAG, "doInBackground: playing complete");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

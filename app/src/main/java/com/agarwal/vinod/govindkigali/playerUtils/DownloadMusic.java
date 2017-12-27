package com.agarwal.vinod.govindkigali.playerUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class DownloadMusic extends AsyncTask<String, Void, Void> {

    private Context context;
    public DownloadMusic(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.d(MainActivity.TAG, "doInBackground: Downloading + playing music");
        try {
            RandomAccessFile file = new RandomAccessFile(strings[0], "rw");

            //TODO: MAKE Activity run even after user kills the app

//            Integer skip = Integer.parseInt(strings[2]);
            URL url = new URL(strings[1]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
//                OutputStream file = new FileOutputStream(strings[0]);
            byte data[] = new byte[1024];
            long total = 0, count = 0;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    Log.d("ANDRO_ASYNC", "doInBackground: CANCELLED................................");
                    break;
                }
                total += count;
//                    publishProgress(""+(int)((total*100)/lenghtOfFile));125010337
                file.write(data);
                Log.d("ANDRO_ASYNC", "doInBackground: " + total);
            }
            file.close();
            Log.d(MainActivity.TAG, "doInBackground: Download complete");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show();
    }
}

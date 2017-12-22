package com.agarwal.vinod.govindkigali.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.utils.PrefManager;

import java.util.ArrayList;


/**
 * Created by darsh on 11/12/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    public static ArrayList<Song> playList = new ArrayList<>();
    private Context context;
    public static final String TAG = "SA";

    public SongAdapter(Context context, ArrayList<Song> playList) {
        this.context = context;
        SongAdapter.playList = playList;
    }

    public void updateTracks(ArrayList<Song> playList) {
        Log.d(TAG, "updateNews: " + playList.size());
        SongAdapter.playList = playList;
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SongViewHolder songViewHolder  = new SongViewHolder(inflater.inflate(R.layout.layout_song, parent, false));
        return songViewHolder ;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.bindView(playList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        LinearLayout llSong;

        private SongViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            llSong = itemView.findViewById(R.id.ll_song);
            PrefManager preferenceManager = new PrefManager(context);
            if(preferenceManager.isNightModeEnabled2()){
                llSong.setBackgroundColor(Color.parseColor("#000000"));
                tvName.setTextColor(Color.parseColor("#ffffff"));
            }

        }

        void bindView(final Song song, final int pos) {
            tvName.setText(song.getTitle());

            llSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager cm =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    final boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    Log.d(TAG, "onClick: " + isConnected);
                    if (isConnected) {
                        PlayerFragment.focus = false;
                        Log.d(TAG, "onClick: checking loss first :)");
                        Intent i = new Intent("custom-message");
                        i.putExtra("val", pos);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                    } else {
                        Toast.makeText(context, "Internet not available!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}

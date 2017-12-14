package com.agarwal.vinod.govindkigali.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.PlayerCommunication;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.fragments.PlayerFragment;
import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;


/**
 * Created by darsh on 11/12/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> playList = new ArrayList<>();
    private Context context;
    PlayerInterface playerInterface;
    public static final String TAG = "CHECK";

    public SongAdapter(Context context, PlayerInterface playerInterface) {
        this.context = context;
        this.playerInterface = playerInterface;
    }

    public void updateTracks(ArrayList<Song> playList) {
        Log.d(TAG, "updateNews: " + playList.size());
        this.playList = playList;
        notifyDataSetChanged();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new SongViewHolder(inflater.inflate(R.layout.layout_song, parent, false));
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

        }

        void bindView(final Song song, final int pos) {
            tvName.setText(song.getTitle());

            llSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playerInterface.playSong(pos);
                }
            });

        }
    }

    public interface PlayerInterface {
        void playSong(Integer value);
    }
}

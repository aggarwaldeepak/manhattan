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
import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;


/**
 * Created by darsh on 11/12/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> playList = new ArrayList<>();
    private Context context;
//    private PlayerCommunication communication;
    private Activity activity;
    public static final String TAG = "CHECK";

    public SongAdapter(Context context, Activity activity) {
//    }, PlayerCommunication communication) {
        this.context = context;
        this.activity = activity;
//        this.communication = communication;
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
        holder.bindView(playList.get(position));
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDescription, tvLikes, tvViews;
        LinearLayout llSong;

        private SongViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
//            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLikes = itemView.findViewById(R.id.tv_likes);
            tvViews = itemView.findViewById(R.id.tv_views);
            llSong = itemView.findViewById(R.id.ll_song);

        }

        void bindView(final Song song) {
            tvName.setText(song.getTitle());
//            tvDescription.setText(song.getDescription());
            tvLikes.setText(song.getFavoritings_count());
            tvViews.setText(song.getPlayback_count());

            llSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PlayerCommunication)activity).playSong(song);
                }
            });

        }
    }
}

package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.fragments.mymusic.SubPlayListFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.utils.CustomDialogClass;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by darsh on 15/12/17.
 */

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.DialogListViewHolder> {


    private ArrayList<String> playList = new ArrayList<>();
    private Context context;
    private Song song;
    private Boolean flag;
    private CustomDialogClass activity;
    public static final String TAG = "POAD";

    public DialogListAdapter(Context context, Song song, Boolean flag, CustomDialogClass activity) {
        this.context = context;
        this.song = song;
        this.flag = flag;
        this.activity = activity;
    }

    public void updateList(ArrayList<String> playList) {
        Log.d(TAG, "updateNews: " + playList.size());
        this.playList = playList;
        notifyDataSetChanged();
    }

    @Override
    public DialogListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new DialogListViewHolder(inflater.inflate(R.layout.layout_dialog_list, parent, false));
    }

    @Override
    public void onBindViewHolder(DialogListViewHolder holder, int position) {
        holder.bindView(playList.get(position));
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class DialogListViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        LinearLayout llSong;
        DialogListViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            llSong = itemView.findViewById(R.id.ll_song);
        }

        void bindView(final String name) {
            tvName.setText(name);
            llSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    FirebaseDatabase.getInstance().getReference("pop").child(name).child(song.getId()).setValue(song);
                    Toast.makeText(context, "Song added to " + name, Toast.LENGTH_SHORT).show();
                    activity.dismiss();
                }
            });
        }
    }
}

package com.agarwal.vinod.govindkigali.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayBack;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by darsh on 22/12/17.
 */

public class SongImageAdapter extends RecyclerView.Adapter<SongImageAdapter.SongImageviewHolder> {

    Context context;
    ArrayList<Song> playList = new ArrayList<>();
    Activity activity;
    public static final String TAG = "SIA";
    private PlayerCommunication playerCommunication;

    public SongImageAdapter(Context context, Activity activity, PlayerCommunication playerCommunication) {
        this.context = context;
        this.playerCommunication = playerCommunication;
        this.activity = activity;
    }

    public void updateImage(ArrayList<Song> playList) {
        this.playList = playList;
        notifyDataSetChanged();
    }

    @Override
    public SongImageviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new SongImageviewHolder(inflater.inflate(R.layout.layout_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SongImageviewHolder holder, int position) {
        holder.bindView(playList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class SongImageviewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        LinearLayout llImageContainer;
        TextView tvSongName;

        SongImageviewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_Image);
            llImageContainer = itemView.findViewById(R.id.ll_image_container);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
        }

        void bindView(Song song, final Integer pos) {

            tvSongName.setText(song.getAlbum().getName_en());
            ivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
            ScreenResolution screenRes = deviceDimensions();
            ivImage.getLayoutParams().height = screenRes.height / 2;
            llImageContainer.getLayoutParams().width = screenRes.width - 250;
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.photo);
            requestOptions.error(R.drawable.photo);
            requestOptions.centerCrop();
            Glide.with(context).load(song.getArtwork_song()).apply(requestOptions).into(ivImage);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager cm =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    final boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    Log.d(TAG, "onClick: " + isConnected);
                    if (isConnected) {
                        PlayBack.focus = false;
                        playerCommunication.playSong(playList, pos);
                        Log.d(TAG, "onClick: checking loss first :)");
                    } else {
                        Toast.makeText(context, "Internet not available!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private class ScreenResolution {
        int width;
        int height;
        private ScreenResolution(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    ScreenResolution deviceDimensions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // getsize() is available from API 13
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return new ScreenResolution(size.x, size.y);
        }
        else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            // getWidth() & getHeight() are deprecated
            return new ScreenResolution(display.getWidth(), display.getHeight());
        }
    }

}

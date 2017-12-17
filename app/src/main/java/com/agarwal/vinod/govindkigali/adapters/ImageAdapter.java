package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;

/**
 * Created by darsh on 17/12/17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    Context context;
    ArrayList<Song> playList = new ArrayList<>();
    public static final String TAG = "SNG";
    Integer pos;
    public ImageAdapter(Context context, ArrayList<Song> playList) {
        this.context = context;
        this.playList = playList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ImageViewHolder(inflater.inflate(R.layout.layout_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bindView(playList.get(position));
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ImageViewHolder(View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_Image);

            DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
            int height = (int) (2000/displaymetrics.density);
            int width = (int) (2000/displaymetrics.density);
//            int width = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 800, displaymetrics );
            Log.d(TAG, "ImageViewHolder: " + height + "===" + width);
            ivImage.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        }

        void bindView(Song song) {
            ivImage.setImageResource(R.drawable.ic_dashboard_black_24dp);
        }
    }
}

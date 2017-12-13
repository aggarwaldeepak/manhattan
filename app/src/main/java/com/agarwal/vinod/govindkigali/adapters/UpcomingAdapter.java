package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Upcoming;

import java.util.ArrayList;

/**
 * Created by Anirudh Gupta on 12/13/2017.
 */

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    Context context;
    ArrayList<Upcoming> upcomings;
    public UpcomingAdapter(Context context, ArrayList<Upcoming> upcomings) {
        this.context = context;
        this.upcomings = upcomings;
        if(this.upcomings == null){
            this.upcomings = new ArrayList<>();
        }
    }

    public void update(@NonNull ArrayList<Upcoming> upcomings){
        this.upcomings = upcomings;
        notifyDataSetChanged();
    }

    @Override
    public UpcomingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new UpcomingViewHolder(inflater.inflate(R.layout.layout_upcoming,parent,false));
    }

    @Override
    public void onBindViewHolder(UpcomingViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return upcomings.size();
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvVenue,tvTime;
        Button btnOptions;

        public UpcomingViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvVenue = itemView.findViewById(R.id.tv_venue);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnOptions = itemView.findViewById(R.id.btn_options);
        }

        public void bindView(int position){
            tvDate.setText(upcomings.get(position).getmMonth()
                    + " " + upcomings.get(position).getmDate()
                    + "\n" + upcomings.get(position).getmYear());
            tvVenue.setText(upcomings.get(position).getmVenue());
            tvTime.setText(upcomings.get(position).getmTime());
        }
    }
}

package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.UpcomingAdapter;
import com.agarwal.vinod.govindkigali.api.SongService;
import com.agarwal.vinod.govindkigali.api.UpcomingService;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.models.Upcoming;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {


    public static ArrayList<Upcoming> feededUpcomings = null;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        RecyclerView rvUpcoming;
        rvUpcoming = view.findViewById(R.id.rvUpcoming);
        final UpcomingAdapter adapter = new UpcomingAdapter(getContext(),null);
        rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUpcoming.setAdapter(adapter);
        if(feededUpcomings == null){
            UpcomingService.getUpcomingApi().getUpcomings().enqueue(new Callback<ArrayList<Upcoming>>() {
                @Override
                public void onResponse(Call<ArrayList<Upcoming>> call, Response<ArrayList<Upcoming>> response) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Log.d("Yooooooooo", "onResponse: " + response.body().get(i).getmVenue());
                    }
                    feededUpcomings = response.body();
                    adapter.update(feededUpcomings);
                }

                @Override
                public void onFailure(Call<ArrayList<Upcoming>> call, Throwable t) {
                    Log.d("Yooo", "onFailure: ");
                    t.printStackTrace();
                }
            });
        } else {
            adapter.update(feededUpcomings);
        }
        return view;
    }

}

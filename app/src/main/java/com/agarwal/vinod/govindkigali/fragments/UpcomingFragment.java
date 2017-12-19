package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.UpcomingAdapter;
import com.agarwal.vinod.govindkigali.adapters.UpcomingSpinnerAdapter;
import com.agarwal.vinod.govindkigali.api.UpcomingService;
import com.agarwal.vinod.govindkigali.models.Upcoming;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {


    ActionBar actionBar;
    Toolbar toolbar;
    Spinner toolbarSpinner;
    public static ArrayList<Upcoming> feededUpcomings = null;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        initiateToolbar();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    void initiateToolbar(){
        if(actionBar == null){
            actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        }
        if(toolbar == null){
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        }

        actionBar.setDisplayShowTitleEnabled(false);
        setupSpinner();
    }

    void setupSpinner(){
        if (toolbarSpinner == null){
            toolbarSpinner = toolbar.findViewById(R.id.spinner_toolbar);
        }
        toolbarSpinner.setVisibility(View.VISIBLE);
        toolbarSpinner.setAdapter(new UpcomingSpinnerAdapter(getContext()));
    }

    @Override
    public void onStop() {
        super.onStop();
        actionBar.setDisplayShowTitleEnabled(true);
        toolbarSpinner.setVisibility(View.GONE);
    }
}

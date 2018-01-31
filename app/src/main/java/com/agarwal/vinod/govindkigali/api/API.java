package com.agarwal.vinod.govindkigali.api;

import com.agarwal.vinod.govindkigali.models.Result;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.models.Upcoming;
import com.agarwal.vinod.govindkigali.models.upcomings.Year;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;



public interface API {

    @GET("songs/")
    Call<Result> getTracks();

    @GET("bins/clo31")
    Call<ArrayList<Upcoming>> getUpcomings();

    @GET("bins/7wsaj")
    Call<ArrayList<Upcoming>> getUpcomingsWithoutHINDI();

    @GET("bins/uz8qr")
    Call<ArrayList<Upcoming>> getUpcomingsOldOrig();

    @GET("bins/11avcb")
    Call<ArrayList<Year>> getUpcomingYearsModulated();
}

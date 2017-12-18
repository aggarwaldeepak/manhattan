package com.agarwal.vinod.govindkigali.api;

import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.models.Upcoming;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by darsh on 11/12/17.
 */

public interface API {

    @GET("users/17410596/tracks?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J")
    Call<ArrayList<Song>> getTracks();

    @GET("bins/uz8qr")
    Call<ArrayList<Upcoming>> getUpcomings();
}

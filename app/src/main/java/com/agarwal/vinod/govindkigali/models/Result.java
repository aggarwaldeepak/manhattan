package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by darsh on 26/1/18.
 */

public class Result {


    @SerializedName("results")
    @Expose
    private ArrayList<Song> results = new ArrayList<>();

    public Result(){}

    public Result(ArrayList<Song> results) {
        this.results = results;
    }

    public ArrayList<Song> getSongs() {
        return results;
    }
}

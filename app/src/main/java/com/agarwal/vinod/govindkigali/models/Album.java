package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by darsh on 26/1/18.
 */

public class Album {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name_en")
    @Expose
    private String name_en;

    @SerializedName("name_hi")
    @Expose
    private String name_hi;

    public Album() {}

    public Album(String id, String name_en, String name_hi) {
        this.id = id;
        this.name_en = name_en;
        this.name_hi = name_hi;
    }

    public String getId() {
        return id;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_hi() {
        return name_hi;
    }
}

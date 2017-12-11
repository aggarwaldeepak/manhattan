package com.agarwal.vinod.govindkigali.models;

/**
 * Created by darsh on 11/12/17.
 */

public class Song {
    private String name, imageUrl;

    public Song(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

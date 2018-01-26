package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by darsh on 11/12/17.
 */

public class Song {

    @SerializedName("album")
    @Expose
    private Album album;

    @SerializedName("song")
    @Expose
    private String song;

    @SerializedName("artwork_song")
    @Expose
    private String artwork_song;

    @SerializedName("duration")
    @Expose
    private String duration;

    public Song(Album album, String song, String artwork_song, String duration) {
        this.album = album;
        this.song = song;
        this.artwork_song = artwork_song;
        this.duration = duration;
    }

    public Album getAlbum() {
        return album;
    }

    public String getSong() {
        return song;
    }

    public String getArtwork_song() {
        return artwork_song;
    }

    public String getDuration() {
        return duration;
    }
}

package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by darsh on 11/12/17.
 */

public class Song {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("stream_url")
    @Expose
    private String stream_url;

    @SerializedName("download_url")
    @Expose
    private String download_url;

    @SerializedName("artwork_url")
    @Expose
    private String artwork_url;

    @SerializedName("duration")
    @Expose
    private Integer duration;

    public Song() {
    }

    public Song(String title, String id, String description, String stream_url, String download_url, String artwork_url, Integer duration) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.stream_url = stream_url;
        this.download_url = download_url;
        this.artwork_url = artwork_url;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStream_url() {
        return stream_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public String getArtwork_url() {
        return artwork_url;
    }

    public Integer getDuration() {
        return duration;
    }
}

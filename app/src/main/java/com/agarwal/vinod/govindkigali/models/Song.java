package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by darsh on 11/12/17.
 */

public class Song {

//    @SerializedName("Title")
//    @Expose
//    private String Title;
//
//    @SerializedName("AudioFileId")
//    @Expose
//    private String AudioFileId;
//
//    @SerializedName("Mp3Url")
//    @Expose
//    private String Mp3Url;
//
//    @SerializedName("Duration")
//    @Expose
//    private Double Duration;
//
//    public Song(String title, String audioFileId, String mp3Ur, Double duration) {
//        Title = title;
//        AudioFileId = audioFileId;
//        Mp3Url = mp3Ur;
//        Duration = duration;
//    }
//
//    public String getTitle() {
//        return Title;
//    }
//
//    public String getAudioFileId() {
//        return AudioFileId;
//    }
//
//    public String getMp3Url() {
//        return Mp3Url;
//    }
//
//    public Double getDuration() {
//        return Duration;
//    }

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

    @SerializedName("favoritings_count")
    @Expose
    private String favoritings_count;

    @SerializedName("playback_count")
    @Expose
    private String playback_count;

    @SerializedName("duration")
    @Expose
    private Integer duration;

    public Song(String title, String id, String description, String stream_url, String download_url, String favoritings_count, String playback_count, Integer duration) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.stream_url = stream_url;
        this.download_url = download_url;
        this.favoritings_count = favoritings_count;
        this.playback_count = playback_count;
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

    public String getFavoritings_count() {
        return favoritings_count;
    }

    public String getPlayback_count() {
        return playback_count;
    }

    public Integer getDuration() {
        return duration;
    }
}

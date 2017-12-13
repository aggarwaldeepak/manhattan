package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anirudh Gupta on 12/13/2017.
 */

public class Upcoming {

    public Upcoming(int mDate, String mDay, String mMonth, String mTime, String mVenue, int mYear) {
        this.mDate = mDate;
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mTime = mTime;
        this.mVenue = mVenue;
        this.mYear = mYear;
    }

    @SerializedName("Date")
    @Expose
    int mDate;

    @SerializedName("Day")
    @Expose
    String mDay;

    @SerializedName("Month")
    @Expose
    String mMonth;

    @SerializedName("Time")
    @Expose
    String mTime;

    @SerializedName("Venue")
    @Expose
    String mVenue;

    @SerializedName("Year")
    @Expose
    int mYear;

    public void setmDate(int mDate) {
        this.mDate = mDate;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmVenue(String mVenue) {
        this.mVenue = mVenue;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmDate() {
        return mDate;
    }

    public String getmDay() {
        return mDay;
    }

    public String getmMonth() {
        return mMonth;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmVenue() {
        return mVenue;
    }

    public int getmYear() {
        return mYear;
    }
}

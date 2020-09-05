package com.example.stargazzer;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PicInfo {

    @SerializedName("date")
    private Date date;

    @SerializedName("explanation")
    private  String explanation;

    @SerializedName("title")
    private String title;

    @SerializedName("media_type")
    private String media_type;

    @SerializedName("url")
    private String url;

    public Date getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getTitle() {
        return title;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getUrl() {
        return url;
    }

    public PicInfo(Date date, String exp, String title, String media_type, String hdurl){
        this.date = date;
        this.explanation = exp;
        this.title = title;
        this.url = hdurl;
        this.media_type = media_type;
    }
}

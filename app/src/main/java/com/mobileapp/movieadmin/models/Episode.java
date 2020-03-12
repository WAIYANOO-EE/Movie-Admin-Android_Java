package com.mobileapp.movieadmin.models;

public class Episode {

    public static final String COLLECTION_NAME = "episodes";

    private String id;
    private String name;
    private String videoLink;
    private String seriesId;

    public Episode(String id, String name, String videoLink, String seriesId) {
        this.id = id;
        this.name = name;
        this.videoLink = videoLink;
        this.seriesId = seriesId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }
}

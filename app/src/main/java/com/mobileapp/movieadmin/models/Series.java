package com.mobileapp.movieadmin.models;

public class Series {

    public static final String COLLECTION_NAME = "series";

    private String id;
    private String title;
    private String description;
    private int episodesCount;
    private String imageUrl;
    private String genreId;
    private String genreName;

    public Series() { }

    public Series(String id, String title, String description, int episodesCount, String imageUrl, String genreId, String genreName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.episodesCount = episodesCount;
        this.imageUrl = imageUrl;
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}

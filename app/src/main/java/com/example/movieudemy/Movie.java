package com.example.movieudemy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int id;

    private String imagePath;
    private String title;
    private String originTitle;
    private String voteAverage;
    private String releaseDate;
    private String overview;

    public Movie(int id, String title, String originTitle, String overview, String imagePath, String releaseDate, String voteAverage) {
        this.id = id;
        this.title = title;
        this.originTitle = originTitle;
        this.overview = overview;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

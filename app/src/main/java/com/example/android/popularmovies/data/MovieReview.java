package com.example.android.popularmovies.data;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Flavio on 8/22/2015.
 */
public class MovieReview implements Serializable {

    public static final String ID = "id";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";

    private String author;
    private String id;
    private String content;
    private String movieId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public MovieReview(String author, String id, String content, String movieId) {
        this.author = author;
        this.id = id;
        this.content = content;
        this.movieId = movieId;
    }

    protected MovieReview(Parcel in) {
    }
}

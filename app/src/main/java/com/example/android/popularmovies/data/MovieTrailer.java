package com.example.android.popularmovies.data;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Flavio on 8/22/2015.
 */
public class MovieTrailer implements Serializable {

    public static final String ID = "id";
    public  static final String NAME = "name";
    public  static final String KEY="key";

    private String movieId;
    private String id;
    private String name;
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    protected MovieTrailer(Parcel in) {
    }

    public MovieTrailer(String movieId, String id, String name, String key) {
        this.movieId = movieId;
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}

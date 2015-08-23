package com.example.android.popularmovies.data;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Flavio on 7/11/2015.
 */
public class MovieItem implements Serializable {

    public static final String POSTER_PATH = "poster_path";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    public static final String ID = "id";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_COUNT= "vote_count";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String TITLE = "title";

    private String id;
    private String originalTitle;
    private String moviePosterThumbnail;
    private String synopsis;
    private String rating;
    private String releaseDate;
    private String popularity;
    private String voteCount;
    private String backdropPath;
    private String title;
    private ArrayList<MovieReview> movieReviews;
    private ArrayList<MovieTrailer> movieTrailers;

    protected MovieItem(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        moviePosterThumbnail = in.readString();
        synopsis = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }

    public List<MovieReview> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(ArrayList<MovieReview> movieReviews) {
        this.movieReviews = movieReviews;
    }

    public List<MovieTrailer> getMovieTrailers() {
        return movieTrailers;
    }

    public void setMovieTrailers(ArrayList<MovieTrailer> movieTrailers) {
        this.movieTrailers = movieTrailers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePosterThumbnail() {
        return moviePosterThumbnail;
    }

    public void setMoviePosterThumbnail(String moviePosterThumbnail) {
        this.moviePosterThumbnail = moviePosterThumbnail;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieItem(String movieId, String originalTitle, String moviePoster, String overview, String vote_average, String releaseDate, String popularity, String voteCount, String backdropPath, String title){

        setId(movieId);
        setOriginalTitle(originalTitle);
        setMoviePosterThumbnail(moviePoster);
        setSynopsis(overview);
        setRating(vote_average);
        setReleaseDate(releaseDate);
        setPopularity(popularity);
        setVoteCount(voteCount);
        setBackdropPath(backdropPath);
        setTitle(title);
    }
}

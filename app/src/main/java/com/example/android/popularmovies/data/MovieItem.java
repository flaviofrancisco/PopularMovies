package com.example.android.popularmovies.data;

/**
 * Created by Flavio on 7/11/2015.
 */
public class MovieItem {

    public static final String POSTER_PATH = "poster_path";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    public static final String ID = "id";

    private String id;
    private String originalTitle;
    private String moviePosterThumbnail;
    private String synopsis;
    private String rating;
    private String releaseDate;

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

    public MovieItem(String movieId, String otitle, String moviePoster, String overview, String vote_average, String rdate){

        setId(movieId);
        setOriginalTitle(otitle);
        setMoviePosterThumbnail(moviePoster);
        setSynopsis(overview);
        setRating(vote_average);
        setReleaseDate(rdate);
    }
}

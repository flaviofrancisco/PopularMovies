package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Flavio on 8/9/2015.
 */
public class PopularMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    private static final String COLUMN_MOVIE_PREFIX = "movie_";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

         public static final String COLUMN_MOVIE_BACKDROP_PATH = COLUMN_MOVIE_PREFIX + "backdrop_path";

        public  static final String COLUMN_MOVIE_RELASE_DATE = COLUMN_MOVIE_PREFIX + "release_date";

        public  static final  String COLUMN_MOVIE_POSTER_PATH = COLUMN_MOVIE_PREFIX + "poster_path";

        public static final String COLUMN_MOVIE_POPULARITY = COLUMN_MOVIE_PREFIX + "popularity";

        public static final String COLUMN_MOVIE_VOTE_AVG = COLUMN_MOVIE_PREFIX + "vote_avg";

        public static final String COLUMN_MOVIE_VOTE_COUNT = COLUMN_MOVIE_PREFIX + "vote_count";

        public static final String COLUMN_MOVIE_TITLE = COLUMN_MOVIE_PREFIX + "title";

        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = COLUMN_MOVIE_PREFIX + "original_title";

        public static final String COLUMN_MOVIE_OVERVIEW = COLUMN_MOVIE_PREFIX + "overview";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MovieReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        private static final String COLUMN_REVIEW_PREFIX = "review_";

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_MOVIE_ID = COLUMN_MOVIE_PREFIX + "id";

        public static final String COLUMN_REVIEW_AUTHOR = COLUMN_REVIEW_PREFIX + "author";

        public static final String COLUMN_REVIEW_CONTENT = COLUMN_REVIEW_PREFIX + "content";

    }

    public static final class MovieTrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        private static final String COLUMN_TRAILER_PREFIX = "trailer_";

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_MOVIE_ID = COLUMN_MOVIE_PREFIX + "id";

        public static final String COLUM_TRAILER_KEY = COLUMN_TRAILER_PREFIX + "key";

        public static final String COLUMN_TRAILER_NAME = COLUMN_TRAILER_PREFIX + "name";

    }
}

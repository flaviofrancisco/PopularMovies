package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Flavio on 8/9/2015.
 */
public class PopularMoviesProvider extends ContentProvider {

    private PopularMoviesDBHelper mOpenHelper;

    private static final SQLiteQueryBuilder sMovieInnerJoinTrailerAndReview;

    static {
        sMovieInnerJoinTrailerAndReview = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        // movie INNER JOIN review ON movie._id = review.movie_id
        // INNER JOIN trailer ON movie._id = trailer.movie_id
        sMovieInnerJoinTrailerAndReview.setTables(
                PopularMoviesContract.MovieEntry.TABLE_NAME
                + " INNER JOIN "
                + PopularMoviesContract.MovieReviewEntry.TABLE_NAME + " ON "
                + PopularMoviesContract.MovieEntry.TABLE_NAME + "." + PopularMoviesContract.MovieEntry._ID + " = "
                + PopularMoviesContract.MovieReviewEntry.TABLE_NAME + "." + PopularMoviesContract.MovieReviewEntry.COLUMN_MOVIE_ID
                + " INNER JOIN "
                + PopularMoviesContract.MovieTrailerEntry.TABLE_NAME + " ON "
                + PopularMoviesContract.MovieEntry.TABLE_NAME + "." + PopularMoviesContract.MovieEntry._ID + " = "
                + PopularMoviesContract.MovieTrailerEntry.TABLE_NAME + "." + PopularMoviesContract.MovieTrailerEntry.COLUMN_MOVIE_ID
        );

    }

    //movie._id = ?
    private static final String sMovieByIdSelection =
            PopularMoviesContract.MovieEntry.TABLE_NAME + "." + PopularMoviesContract.MovieEntry._ID + " = ? ";




    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }



}

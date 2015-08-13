package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Flavio on 8/9/2015.
 */
public class PopularMoviesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PopularMoviesDBHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_REVIEW_AND_TRAILER = 101;
    static final int TRAILER = 200;
    static final int REVIEW = 300;

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

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopularMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PopularMoviesContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority,
                PopularMoviesContract.PATH_MOVIE + "/*",
                MOVIE_WITH_REVIEW_AND_TRAILER);
        matcher.addURI(authority, PopularMoviesContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, PopularMoviesContract.PATH_REVIEW, REVIEW);

        return matcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return PopularMoviesContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIE:
                long _id = db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = PopularMoviesContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
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

package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.example.android.popularmovies.data.PopularMoviesContract.MovieReviewEntry;
import com.example.android.popularmovies.data.PopularMoviesContract.MovieTrailerEntry;

/**
 * Created by Flavio on 8/9/2015.
 */
public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    /* Change the version after each database schema change */
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "popularmovies.db";

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final  String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY,"
                + MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_POPULARITY + " REAL NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_RELASE_DATE + " STRING NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_VOTE_AVG + " REAL NOT NULL, "
                + MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " REAL NOT NULL "
                + ");" ;

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " +
                MovieTrailerEntry.TABLE_NAME + " ("
                + MovieTrailerEntry._ID + " TEXT PRIMARY KEY,"
                + MovieTrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieTrailerEntry.COLUM_TRAILER_KEY + " TEXT NOT NULL, "
                + MovieTrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, "

                + " FOREIGN KEY (" + MovieTrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ")); ";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                MovieReviewEntry.TABLE_NAME + " ("
                + MovieReviewEntry._ID + " TEXT PRIMARY KEY,"
                + MovieReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, "
                + MovieReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, "
                + MovieReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "

                + " FOREIGN KEY (" + MovieReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ")); ";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

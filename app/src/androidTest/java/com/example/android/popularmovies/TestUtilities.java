package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.android.popularmovies.data.PopularMoviesContract;
import com.example.android.popularmovies.data.PopularMoviesDBHelper;

/**
 * Created by Flavio on 8/13/2015.
 */
public class TestUtilities extends AndroidTestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    static ContentValues createTerminatorGenisysMovieValues() {

        ContentValues testValues = new ContentValues();

        testValues.put(PopularMoviesContract.MovieEntry._ID, 87101);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, "/bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, "Terminator Genisys");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, "The year is 2029 ...");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_RELASE_DATE, "2015-07-01");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POPULARITY, 53.681297);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, "Terminator Genisys");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVG, 6.3);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, 717);

        return testValues;
    }

    static ContentValues createTerminatorGenisysReviewValues() {

        ContentValues testValues = new ContentValues();

        testValues.put(PopularMoviesContract.MovieReviewEntry._ID,"55a58e46c3a3682bb2000065");
        testValues.put(PopularMoviesContract.MovieReviewEntry.COLUMN_MOVIE_ID,87101);
        testValues.put(PopularMoviesContract.MovieReviewEntry.COLUMN_REVIEW_AUTHOR,"Flavio Francisco");
        testValues.put(PopularMoviesContract.MovieReviewEntry.COLUMN_REVIEW_CONTENT,"Nice movie");

        return testValues;
    }

    static ContentValues createTerminatorGenisysTrailersValues() {

        ContentValues testValues = new ContentValues();

        testValues.put(PopularMoviesContract.MovieTrailerEntry.COLUMN_MOVIE_ID, 87101);
        testValues.put(PopularMoviesContract.MovieTrailerEntry._ID, "552e46439251413f9c000592");
        testValues.put(PopularMoviesContract.MovieTrailerEntry.COLUM_TRAILER_KEY, "62E4FJTwSuc");
        testValues.put(PopularMoviesContract.MovieTrailerEntry.COLUMN_TRAILER_NAME, "Video 01");

        return testValues;
    }

    public void testInsertMovieData() {

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTerminatorGenisysMovieValues();

        db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, testValues);

        testValues = TestUtilities.createTerminatorGenisysReviewValues();

        db.insert(PopularMoviesContract.MovieReviewEntry.TABLE_NAME, null, testValues);

        testValues = TestUtilities.createTerminatorGenisysTrailersValues();

        db.insert(PopularMoviesContract.MovieTrailerEntry.TABLE_NAME, null, testValues);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursorMovies = db.query(
                PopularMoviesContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue("No records for movie table", cursorMovies.moveToFirst());

        Cursor cursorReviews = db.query(
                PopularMoviesContract.MovieReviewEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue("No records for review table", cursorReviews.moveToFirst());

        Cursor cursorTrailers = db.query(
                PopularMoviesContract.MovieTrailerEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertTrue("No records for trailer table", cursorTrailers.moveToFirst());

        cursorMovies.close();
        cursorTrailers.close();
        cursorReviews.close();

        db.close();
    }
    }

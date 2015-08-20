package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.android.popularmovies.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by Flavio on 8/13/2015.
 */
public class TestUtilities extends AndroidTestCase {

    private long movieId = 8000;

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    static ContentValues createTerminatorGenisysMovieValues(long movieId) {

        ContentValues testValues = new ContentValues();

        testValues.put(PopularMoviesContract.MovieEntry._ID, movieId);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, "/bIlYH4l2AyYvEysmS2AOfjO7Dn8.jpg");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, "Terminator Genisys");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, "The year is 2029 ...");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_RELASE_DATE, "2015-07-01");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POPULARITY, 53.6);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, "Terminator Genisys");
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVG, 6.3);
        testValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, 717);

        return testValues;
    }

    static ContentValues createTerminatorGenisysReviewValues(long movieId) {

        ContentValues testValues = new ContentValues();

        testValues.put(PopularMoviesContract.MovieReviewEntry._ID,Long.toString(movieId));
        testValues.put(PopularMoviesContract.MovieReviewEntry.COLUMN_MOVIE_ID,movieId);
        testValues.put(PopularMoviesContract.MovieReviewEntry.COLUMN_REVIEW_AUTHOR,"Flavio Francisco");
        testValues.put(PopularMoviesContract.MovieReviewEntry.COLUMN_REVIEW_CONTENT,"Nice movie");

        return testValues;
    }

    static ContentValues createTerminatorGenisysTrailersValues(long movieId) {

        ContentValues testValues = new ContentValues();

        testValues.put(PopularMoviesContract.MovieTrailerEntry.COLUMN_MOVIE_ID, movieId);
        testValues.put(PopularMoviesContract.MovieTrailerEntry._ID, Long.toString(movieId));
        testValues.put(PopularMoviesContract.MovieTrailerEntry.COLUM_TRAILER_KEY, "62E4FJTwSuc");
        testValues.put(PopularMoviesContract.MovieTrailerEntry.COLUMN_TRAILER_NAME, "Video 01");

        return testValues;
    }

    public void testInsertMovieData() {

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTerminatorGenisysMovieValues(movieId);

        db.insert(PopularMoviesContract.MovieEntry.TABLE_NAME, null, testValues);

        testValues = TestUtilities.createTerminatorGenisysReviewValues(movieId);

        db.insert(PopularMoviesContract.MovieReviewEntry.TABLE_NAME, null, testValues);

        testValues = TestUtilities.createTerminatorGenisysTrailersValues(movieId);

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

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

}


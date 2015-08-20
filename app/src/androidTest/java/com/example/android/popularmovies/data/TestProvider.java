package com.example.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Flavio on 8/16/2015.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    private long movieId = 8000;

    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                PopularMoviesContract.MovieTrailerEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                PopularMoviesContract.MovieReviewEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                PopularMoviesContract.MovieTrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from Trailer table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                PopularMoviesContract.MovieReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );


        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

    }

   public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                PopularMoviesProvider.class.getName());
        try {

            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: PopularMoviesProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + PopularMoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, PopularMoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {

        String type = mContext.getContentResolver().getType(PopularMoviesContract.MovieEntry.CONTENT_URI);

        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                PopularMoviesContract.MovieEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(PopularMoviesContract.MovieEntry.CONTENT_URI);

        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                PopularMoviesContract.MovieEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(PopularMoviesContract.MovieTrailerEntry.CONTENT_URI);

        assertEquals("Error: the MovieTrailerEntry CONTENT_URI should return MovieTrailerEntry.CONTENT_TYPE",
                PopularMoviesContract.MovieTrailerEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(PopularMoviesContract.MovieReviewEntry.CONTENT_URI);

        assertEquals("Error: the MovieReviewEntry CONTENT_URI should return MovieReviewEntry.CONTENT_TYPE",
                PopularMoviesContract.MovieReviewEntry.CONTENT_TYPE, type);
    }



public void testUpdateMovie() {
    ContentValues values = TestUtilities.createTerminatorGenisysMovieValues(54321);

    Uri movieUri = mContext.getContentResolver().insert(PopularMoviesContract.MovieEntry.CONTENT_URI, values);

    long movieRowId = ContentUris.parseId(movieUri);

    assertTrue(movieRowId != -1);
    Log.d(LOG_TAG, "New row id: " + movieRowId);

    ContentValues updatedValues = new ContentValues(values);
    updatedValues.put(PopularMoviesContract.MovieEntry._ID, movieRowId);
    updatedValues.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, "Elite Squad");

    Cursor locationCursor = mContext.getContentResolver().query(PopularMoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);

    TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
    locationCursor.registerContentObserver(tco);

    int count = mContext.getContentResolver().update(
            PopularMoviesContract.MovieEntry.CONTENT_URI, updatedValues, PopularMoviesContract.MovieEntry._ID + "= ?",
            new String[] { Long.toString(movieRowId)});
    assertEquals(count, 1);

    // Test to make sure our observer is called.  If not, we throw an assertion.
    //
    // Students: If your code is failing here, it means that your content provider
    // isn't calling getContext().getContentResolver().notifyChange(uri, null);
    tco.waitForNotificationOrFail();

    locationCursor.unregisterContentObserver(tco);
    locationCursor.close();

    // A cursor is your primary interface to the query results.
    Cursor cursor = mContext.getContentResolver().query(
            PopularMoviesContract.MovieEntry.CONTENT_URI,
            null,   // projection
            PopularMoviesContract.MovieEntry._ID + " = " + movieRowId,
            null,   // Values for the "where" clause
            null    // sort order
    );

    TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
            cursor, updatedValues);

    cursor.close();
}

    public void testInsertReadProvider() {

        ContentValues testValues = TestUtilities.createTerminatorGenisysMovieValues(movieId);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PopularMoviesContract.MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(PopularMoviesContract.MovieEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieId = ContentUris.parseId(movieUri);

        assertTrue(movieId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursor, testValues);

        ContentValues reviewValues = TestUtilities.createTerminatorGenisysReviewValues(movieId);

        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(PopularMoviesContract.MovieReviewEntry.CONTENT_URI, true, tco);

        Uri reviewInsertUri = mContext.getContentResolver()
                .insert(PopularMoviesContract.MovieReviewEntry.CONTENT_URI, reviewValues);
        assertTrue(reviewInsertUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        Cursor reviewCursor = mContext.getContentResolver().query(
                PopularMoviesContract.MovieReviewEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ReviewEntry insert.",
                reviewCursor, reviewValues);
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver reviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PopularMoviesContract.MovieReviewEntry.CONTENT_URI, true, reviewObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PopularMoviesContract.MovieEntry.CONTENT_URI, true, movieObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        reviewObserver.waitForNotificationOrFail();
        movieObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(reviewObserver);
        mContext.getContentResolver().unregisterContentObserver(movieObserver);
    }

}

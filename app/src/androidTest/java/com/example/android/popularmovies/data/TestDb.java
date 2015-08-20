package com.example.android.popularmovies.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Flavio on 8/13/2015.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(PopularMoviesDBHelper.DATABASE_NAME);
    }

    public void setUp() throws Exception {
        super.setUp();
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(PopularMoviesContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(PopularMoviesContract.MovieReviewEntry.TABLE_NAME);
        tableNameHashSet.add(PopularMoviesContract.MovieTrailerEntry.TABLE_NAME);

        mContext.deleteDatabase(PopularMoviesDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new PopularMoviesDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + PopularMoviesContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry._ID);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POPULARITY);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_RELASE_DATE);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVG);
        movieColumnHashSet.add(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }

    public void tearDown() throws Exception {

    }
}
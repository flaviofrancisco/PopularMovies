package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieItem;
import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.data.MovieTrailer;
import com.example.android.popularmovies.data.PopularMoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Flavio on 7/12/2015.
 */
public class DetailFragment extends Fragment {

    private Button btnMarkAsFavorite;
    private MovieCursorAdapter mMovieCursorAdapter;
    private MovieItem mSelectedMovie;

    private final String SELECTED_MOVIE_KEY = "selected_movie";

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieCursorAdapter = new MovieCursorAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        mSelectedMovie = (MovieItem)intent.getSerializableExtra(SELECTED_MOVIE_KEY);

        String movieTitle = mSelectedMovie.getOriginalTitle();
        String moviePoster = mSelectedMovie.getMoviePosterThumbnail();
        String releaseDate = mSelectedMovie.getReleaseDate();
        String vote_average = mSelectedMovie.getRating();
        String overview = mSelectedMovie.getSynopsis();

        ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieTitle);
        ImageView imageView = ((ImageView) rootView.findViewById(R.id.movie_poster));
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w92//" + moviePoster).into(imageView);
        ((TextView) rootView.findViewById(R.id.release_date)).setText(releaseDate);
        ((TextView) rootView.findViewById(R.id.users_rating)).setText(vote_average);
        ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(overview);

        btnMarkAsFavorite = (Button) rootView.findViewById(R.id.favorites_btn);

        btnMarkAsFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(isAlreadyFavorite()){
                    deleteFavoriteMovie();
                }
                insertNewFavoriteMovie();
            }

        });

        return rootView;
    }

    private void deleteFavoriteMovie() {

        int delete = getActivity().getContentResolver().delete(
                PopularMoviesContract.MovieTrailerEntry.CONTENT_URI,
                PopularMoviesContract.MovieTrailerEntry.COLUMN_MOVIE_ID + " = " + mSelectedMovie.getId(),
                null
        );

        delete = getActivity().getContentResolver().delete(
                PopularMoviesContract.MovieReviewEntry.CONTENT_URI,
                PopularMoviesContract.MovieReviewEntry.COLUMN_MOVIE_ID + " = " + mSelectedMovie.getId(),
                null
        );

        delete = getActivity().getContentResolver().delete(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                PopularMoviesContract.MovieEntry._ID + " = " + mSelectedMovie.getId(),
                null
        );

    }

    private boolean isAlreadyFavorite() {
        // A cursor is your primary interface to the query results.
        Cursor cursor = getActivity().getContentResolver().query(
                PopularMoviesContract.MovieEntry.CONTENT_URI,
                null,   // projection
                PopularMoviesContract.MovieEntry._ID + " = " + mSelectedMovie.getId(),
                null,   // Values for the "where" clause
                null    // sort order
        );

        if(cursor.getCount() == 0){
            return false;
        }

        return true;
    }

    private void insertNewFavoriteMovie(){

        ContentValues movieContentValues = getMovieContentValues();

        Uri insertedUri = getActivity().getContentResolver().insert(PopularMoviesContract.MovieEntry.CONTENT_URI, movieContentValues);

        getActivity().getContentResolver().notifyChange(insertedUri, null);

        for(MovieReview movieReview : mSelectedMovie.getMovieReviews()){

            ContentValues reviewsContentValues = getMovieReviewsContentValues(movieReview);

            insertedUri = getActivity().getContentResolver().insert(PopularMoviesContract.MovieReviewEntry.CONTENT_URI, reviewsContentValues);

            getActivity().getContentResolver().notifyChange(insertedUri, null);
        }

        for(MovieTrailer movieTrailer : mSelectedMovie.getMovieTrailers()){

            ContentValues trailersContentValues = getMovieTrailersContentValues(movieTrailer);

            insertedUri = getActivity().getContentResolver().insert(PopularMoviesContract.MovieTrailerEntry.CONTENT_URI, trailersContentValues);

            getActivity().getContentResolver().notifyChange(insertedUri, null);
        }

    }

    private ContentValues getMovieTrailersContentValues(MovieTrailer movieTrailer) {
        ContentValues data = new ContentValues();

        data.put(PopularMoviesContract.MovieTrailerEntry._ID, movieTrailer.getId());
        data.put(PopularMoviesContract.MovieTrailerEntry.COLUM_TRAILER_KEY, movieTrailer.getKey());
        data.put(PopularMoviesContract.MovieTrailerEntry.COLUMN_MOVIE_ID, mSelectedMovie.getId());
        data.put(PopularMoviesContract.MovieTrailerEntry.COLUMN_TRAILER_NAME, movieTrailer.getName());

        return data;
    }

    private ContentValues getMovieContentValues() {
        ContentValues data = new ContentValues();

        data.put(PopularMoviesContract.MovieEntry._ID,mSelectedMovie.getId());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,mSelectedMovie.getMoviePosterThumbnail());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_TITLE,mSelectedMovie.getTitle());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_POPULARITY,mSelectedMovie.getPopularity());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, mSelectedMovie.getVoteCount());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, mSelectedMovie.getBackdropPath());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, mSelectedMovie.getOriginalTitle());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mSelectedMovie.getSynopsis());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_RELASE_DATE, mSelectedMovie.getReleaseDate());
        data.put(PopularMoviesContract.MovieEntry.COLUMN_MOVIE_VOTE_AVG, mSelectedMovie.getRating());

        return data;
    }

    private ContentValues getMovieReviewsContentValues(MovieReview movieReview) {
        ContentValues data = new ContentValues();

        data.put(PopularMoviesContract.MovieReviewEntry._ID, movieReview.getId());
        data.put(PopularMoviesContract.MovieReviewEntry.COLUMN_MOVIE_ID, mSelectedMovie.getId());
        data.put(PopularMoviesContract.MovieReviewEntry.COLUMN_REVIEW_CONTENT, movieReview.getContent());
        data.put(PopularMoviesContract.MovieReviewEntry.COLUMN_REVIEW_AUTHOR, movieReview.getAuthor());

        return data;
    }

}

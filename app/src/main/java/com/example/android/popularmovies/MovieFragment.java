package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.data.MovieItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Flavio on 7/11/2015.
 */
public class MovieFragment extends Fragment {

    private GridView mGridViewMovies;
    MovieArrayAdapter mMovieArrayAdapter;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public MovieFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridViewMovies = (GridView) rootView.findViewById(R.id.grid_movie_images);

        updateMoviesList();

        mGridViewMovies.setAdapter(mMovieArrayAdapter);

        mGridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                MovieItem movieInfo = (MovieItem) mMovieArrayAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);

                intent.putExtra(MovieItem.ORIGINAL_TITLE, movieInfo.getOriginalTitle());
                intent.putExtra(MovieItem.POSTER_PATH, movieInfo.getMoviePosterThumbnail());
                intent.putExtra(MovieItem.OVERVIEW, movieInfo.getSynopsis());
                intent.putExtra(MovieItem.RELEASE_DATE, movieInfo.getReleaseDate());
                intent.putExtra(MovieItem.VOTE_AVERAGE, movieInfo.getRating());
                mPosition = position;
                startActivity(intent);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return  rootView;
    }

    private void updateMoviesList() {
        FetchMoviesTask movieTask = new FetchMoviesTask(getActivity());

        ArrayList<MovieItem> moveItem = new ArrayList<MovieItem>();

        String sortMethod = Utils.getPreferredSortMethod(getActivity());

        try {
            moveItem = movieTask.execute(sortMethod).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mMovieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.list_item_movie_image, moveItem);
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMoviesList();
        mGridViewMovies.setAdapter(mMovieArrayAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

}

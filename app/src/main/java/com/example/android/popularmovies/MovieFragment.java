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
    private int mPosition = GridView.INVALID_POSITION;
    MovieAdapter mMovieAdapter;
    MovieArrayAdapter mMovieArrayAdapter;

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

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridViewMovies = (GridView) rootView.findViewById(R.id.grid_movie_images);

        updateMoviesList();

        mGridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                MovieItem movieInfo = (MovieItem) mMovieArrayAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);

                    intent.putExtra(MovieItem.ORIGINAL_TITLE, movieInfo.originalTitle);
                    intent.putExtra(MovieItem.POSTER_PATH, movieInfo.moviePosterThumbnail);
                    intent.putExtra(MovieItem.OVERVIEW, movieInfo.synopsis);
                    intent.putExtra(MovieItem.RELEASE_DATE, movieInfo.releaseDate);
                    intent.putExtra(MovieItem.VOTE_AVERAGE, movieInfo.rating);

                mPosition = position;

                startActivity(intent);
            }
        });

        mGridViewMovies.setAdapter(mMovieArrayAdapter);

        return  rootView;
    }

    private void updateMoviesList() {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());

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

}

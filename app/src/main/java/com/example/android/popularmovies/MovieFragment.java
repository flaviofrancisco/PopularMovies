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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Flavio on 7/11/2015.
 */
public class MovieFragment extends Fragment {

    private boolean mUseTwoPanesLayout;
    private GridView mGridViewMovies;

    private MovieArrayAdapter mMovieArrayAdapter;

    private ArrayList<MovieItem> mMoveItem;

    private int mPosition = GridView.INVALID_POSITION;
    private final String MOVIE_LIST_KEY = "movie_list_key";
    private final String SELECTED_KEY="selected_position";
    private final String SELECTED_MOVIE_KEY = "selected_movie";

    public MovieFragment(){ }

    public void setUseTwoPanesLayout(boolean useTwoPanesLayout){
        mUseTwoPanesLayout = useTwoPanesLayout;
        if(mUseTwoPanesLayout){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState == null)
        {
            updateMoviesList();

        } else {
            if(savedInstanceState.containsKey(MOVIE_LIST_KEY)){
                mMoveItem = (ArrayList<MovieItem>) savedInstanceState.getSerializable(MOVIE_LIST_KEY);
            }
            if(savedInstanceState.containsKey(SELECTED_KEY)){
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridViewMovies = (GridView) rootView.findViewById(R.id.grid_movie_images);

        mMovieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.list_item_movie_image, mMoveItem);

            mGridViewMovies.setAdapter(mMovieArrayAdapter);

            mGridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    MovieItem movieInfo = (MovieItem) mMovieArrayAdapter.getItem(position);

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(SELECTED_MOVIE_KEY, movieInfo);

                    startActivity(intent);
                }
            });

        mGridViewMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mPosition = firstVisibleItem;
            }
        });

        return  rootView;
    }

    private void updateMoviesList() {

        try{

            FetchMoviesTask movieTask = new FetchMoviesTask(getActivity());

            String sortMethod = Utils.getPreferredSortMethod(getActivity());

            mMoveItem = movieTask.execute(sortMethod).get();

            if(mMoveItem.size()==0)
            {
                if(sortMethod == getActivity().getString(R.string.pref_sort_by_value_favorites)){
                    Toast.makeText(getActivity(), R.string.info_no_favorite_movies,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), R.string.info_no_connection_available,
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_LIST_KEY, mMoveItem);
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mGridViewMovies.smoothScrollToPosition(mPosition);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(SELECTED_KEY)){
                mPosition = savedInstanceState.getInt(SELECTED_KEY, 0);
            }
            if(savedInstanceState.containsKey(MOVIE_LIST_KEY)){
                mMoveItem = (ArrayList<MovieItem>)savedInstanceState.getSerializable(MOVIE_LIST_KEY);
            }
        }
    }
}

package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Flavio on 7/12/2015.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        String movieTitle = intent.getStringExtra(MovieItem.ORIGINAL_TITLE);
        ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieTitle);

        String moviePoster = intent.getStringExtra(MovieItem.POSTER_PATH);
        ImageView imageView = ((ImageView) rootView.findViewById(R.id.movie_poster));
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w92//" + moviePoster).into(imageView);

        String releaseDate = intent.getStringExtra(MovieItem.RELEASE_DATE);
        ((TextView) rootView.findViewById(R.id.release_date)).setText(releaseDate);

        String vote_average = intent.getStringExtra(MovieItem.VOTE_AVERAGE);
        ((TextView) rootView.findViewById(R.id.users_rating)).setText(vote_average);

        String overview = intent.getStringExtra(MovieItem.OVERVIEW);
        ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(overview);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }
}

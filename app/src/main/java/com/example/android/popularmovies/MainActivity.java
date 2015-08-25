package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.data.MovieItem;


public class MainActivity extends AppCompatActivity implements Callback {

    //private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private String mSortMethod;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSortMethod = Utils.getPreferredSortMethod(this);

        MovieFragment movieFragment = null;
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();

                movieFragment =
                        ((MovieFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie));

               this.onItemSelected(movieFragment.get_selectedMovie());
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        movieFragment =
                ((MovieFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie));
        movieFragment.setUseTwoPanesLayout(mTwoPane);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortMethod = Utils.getPreferredSortMethod(this);

        if(sortMethod != null && !sortMethod.equals(mSortMethod)){
            MovieFragment mf = (MovieFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie);
            if(null != mf){

                if(mf.get_selectedMovie() != null){
                    onItemSelected(mf.get_selectedMovie());
                }

            }

            mSortMethod = sortMethod;
        }

    }


    @Override
    public void onItemSelected(MovieItem movieItem) {

        if(mTwoPane){

            Bundle args = new Bundle();
            args.putSerializable(DetailFragment.SELECTED_MOVIE_KEY, movieItem);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailFragment.SELECTED_MOVIE_KEY, movieItem);
            startActivity(intent);
        }
    }
}

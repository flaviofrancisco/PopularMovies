package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.data.MovieItem;
import com.example.android.popularmovies.data.MovieReview;
import com.example.android.popularmovies.data.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Flavio on 7/11/2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


    private static final String JSON_RESULTS = "results";

    private static final String QUERY_API_KEY="api_key";
    private static final String PARAM_API_KEY ="d258b0a29b796a86edcd444ea07c951b";

    Context mContext;

    public FetchMoviesTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        try{

            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";

            String QUERY_SORT_BY = "sort_by";


            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_SORT_BY, params[0])
                    .appendQueryParameter(QUERY_API_KEY, PARAM_API_KEY).build();

            String jsonResult = getJSONData(builtUri);

            return getMovieDataFromJson(jsonResult);

        } catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private String getJSONData(Uri builtUri)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = null;

        try{

            URL url = new URL(builtUri.toString());

            CheckConnectivity checkConnectivity = new CheckConnectivity();

            if(!checkConnectivity.isNetworkConnected(mContext) || checkConnectivity.isInternetAvailable(builtUri.toString())) {
                return null;
            }

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            result = buffer.toString();

           return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }

    private ArrayList<MovieItem> getMovieDataFromJson(String moviesJsonStr) throws JSONException {

        try {

            if(moviesJsonStr == null)
                return new ArrayList<>();

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(JSON_RESULTS);

            if(resultsArray == null)
                return new ArrayList<>();

            int count = resultsArray.length();

            MovieItem[] movieItems = new MovieItem[count];


            for(int i = 0; i <= count - 1; i++) {

                JSONObject jsonObjectMovie = resultsArray.getJSONObject(i);

                String id = jsonObjectMovie.getString(MovieItem.ID);
                String poster_path = jsonObjectMovie.getString(MovieItem.POSTER_PATH);
                String originalTitle = jsonObjectMovie.getString(MovieItem.ORIGINAL_TITLE);
                String overview = jsonObjectMovie.getString(MovieItem.OVERVIEW);
                String vote_average = mContext.getString(R.string.format_full_friendly_rate, jsonObjectMovie.getString(MovieItem.VOTE_AVERAGE));
                String release_date = jsonObjectMovie.getString(MovieItem.RELEASE_DATE);

                movieItems[i] = new MovieItem(id, originalTitle, poster_path,overview, vote_average, release_date);
                movieItems[i].setMovieReviews(getMovieReviews(id));
                movieItems[i].setMovieTrailers(getMovieTrailers(id));

            }

            return new ArrayList<>(Arrays.asList(movieItems));

        } catch (JSONException e) {
           Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private ArrayList<MovieTrailer> getMovieTrailers(String movieId) throws JSONException {

        ArrayList<MovieTrailer> result = new ArrayList<>();

        final String MOVIES_BASE_URL =
                "https://api.themoviedb.org/3/movie/";

        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(movieId).appendPath("videos")
                .appendQueryParameter(QUERY_API_KEY, PARAM_API_KEY).build();

        String jsonResult = getJSONData(builtUri);

        try {
            result = getMovieTrailersDataFromJson(jsonResult, movieId);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return result;
    }

    private ArrayList<MovieTrailer> getMovieTrailersDataFromJson(String jsonResult, String movieId) throws JSONException {

        if(jsonResult == null)
            return new ArrayList<>();

        JSONObject moviesJson = new JSONObject(jsonResult);
        JSONArray resultsArray = moviesJson.getJSONArray(JSON_RESULTS);

        if(resultsArray == null)
            return new ArrayList<>();

        int count = resultsArray.length();

        if(count == 0)
            return new ArrayList<>();

        MovieTrailer[] movieTrailers = new MovieTrailer[count];

        for(int i = 0; i <= count - 1; i++) {

            JSONObject jsonObjectMovie = resultsArray.getJSONObject(i);

            String id = jsonObjectMovie.getString(MovieTrailer.ID);
            String name = jsonObjectMovie.getString(MovieTrailer.NAME);
            String key = jsonObjectMovie.getString(MovieTrailer.KEY);

            movieTrailers[i] = new MovieTrailer(movieId, id, name,key);

        }

        return new ArrayList<>(Arrays.asList(movieTrailers));
    }

    private ArrayList<MovieReview> getMovieReviews(String movieId) throws JSONException {

        ArrayList<MovieReview> result = new ArrayList<>();

        final String MOVIES_BASE_URL =
                "https://api.themoviedb.org/3/movie/";

        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendPath(movieId).appendPath("reviews")
                .appendQueryParameter(QUERY_API_KEY, PARAM_API_KEY).build();

        String jsonResult = getJSONData(builtUri);

        try {
            result = getMovieReviewsDataFromJson(jsonResult, movieId);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return result;
    }

    private ArrayList<MovieReview> getMovieReviewsDataFromJson(String jsonResult, String movieId) throws JSONException {

        JSONObject moviesJson = new JSONObject(jsonResult);
        JSONArray resultsArray = moviesJson.getJSONArray(JSON_RESULTS);

        if(resultsArray == null)
            return new ArrayList<>();

        int count = resultsArray.length();

        if(count == 0)
            return new ArrayList<>();

        MovieReview[] movieReviews = new MovieReview[count];

        for(int i = 0; i <= count - 1; i++) {

            JSONObject jsonObjectMovie = resultsArray.getJSONObject(i);

            String id = jsonObjectMovie.getString(MovieReview.ID);
            String author = jsonObjectMovie.getString(MovieReview.AUTHOR);
            String content = jsonObjectMovie.getString(MovieReview.CONTENT);

            movieReviews[i] = new MovieReview(author, id, content,movieId);

        }

        return new ArrayList<>(Arrays.asList(movieReviews));

    }
}

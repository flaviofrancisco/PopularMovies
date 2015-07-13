package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmovies.data.MovieItem;

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
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {

    private final Context mContext;

    public FetchMovieTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try{

            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";

            String QUERY_SORT_BY = "sort_by";
            String QUERY_API_KEY="api_key";
            String PARAM_API_KEY ="d258b0a29b796a86edcd444ea07c951b";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_SORT_BY, params[0])
                    .appendQueryParameter(QUERY_API_KEY, PARAM_API_KEY).build();

            URL url = new URL(builtUri.toString());

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
            moviesJsonStr = buffer.toString();

            return getMovieDataFromJson(moviesJsonStr);

        } catch (JSONException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }

    private ArrayList<MovieItem> getMovieDataFromJson(String moviesJsonStr) throws JSONException {

        final String MOVIES_RESULTS = "results";

        try {

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(MOVIES_RESULTS);

            int count = resultsArray.length();
            MovieItem[] movieItems = new MovieItem[count];


            for(int i = 0; i <= count - 1; i++) {

                JSONObject jsonObjectMovie = resultsArray.getJSONObject(i);
                String poster_path = jsonObjectMovie.getString(MovieItem.POSTER_PATH);
                String originalTitle = jsonObjectMovie.getString(MovieItem.ORIGINAL_TITLE);
                String overview = jsonObjectMovie.getString(MovieItem.OVERVIEW);
                String vote_average = jsonObjectMovie.getString(MovieItem.VOTE_AVERAGE);
                String release_date = jsonObjectMovie.getString(MovieItem.RELEASE_DATE);

                movieItems[i] = new MovieItem(originalTitle, poster_path,overview, vote_average, release_date);

            }

            return new ArrayList<MovieItem>(Arrays.asList(movieItems));

        } catch (JSONException e) {
//        Log.e(LOG_TAG, e.getMessage(), e);
        e.printStackTrace();
    }

        return new ArrayList<MovieItem>();
    }
}

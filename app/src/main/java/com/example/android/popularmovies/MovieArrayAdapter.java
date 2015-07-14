package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.data.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Flavio on 7/12/2015.
 */
public class MovieArrayAdapter extends ArrayAdapter {

    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<MovieItem> mMoviesInfo;

    public MovieArrayAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);

        mContext = context;
        mMoviesInfo = data;
        mLayoutResourceId = layoutResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.list_item_movie_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185//" + mMoviesInfo.get(position).getMoviePosterThumbnail()).into(holder.image);

        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}

package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Flavio on 7/11/2015.
 */
public class MovieAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final ImageView movieImageView;

        public ViewHolder(View view) {
            movieImageView = (ImageView) view.findViewById(R.id.list_item_movie_image);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}

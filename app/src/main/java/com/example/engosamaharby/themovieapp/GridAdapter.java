package com.example.engosamaharby.themovieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Eng Osama Harby
 */
public class GridAdapter extends BaseAdapter {
    List<MovieDetails> movies;
    Context context;
    public GridAdapter(Context context, List<MovieDetails> movies){
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        Holder holder = null;
        if(item == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new Holder(item);
            item.setTag(holder);
        }
        else {
            holder = (Holder) item.getTag();
        }

        //Toast.makeText(context, movies.get(position).id + "", Toast.LENGTH_SHORT).show();
        Picasso.with(context).load( movies.get(position).posterPath).into(holder.imageView);
        return item;
    }
    class Holder{
        ImageView imageView;
        public Holder(View v){
            imageView = (ImageView) v.findViewById(R.id.gridItem);
        }
    }

}

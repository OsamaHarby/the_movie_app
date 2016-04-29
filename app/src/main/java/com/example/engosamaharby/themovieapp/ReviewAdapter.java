package com.example.engosamaharby.themovieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eng Osama Harby on 29/04/2016.
 */
public class ReviewAdapter extends BaseAdapter{
    List<Review> reviews;
    Context context;
    public ReviewAdapter(Context context,List<Review>reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Holder holder = null;
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_item,parent,false);
            holder = new Holder(view);
            view.setTag(holder);
        }
        else{
            holder = (Holder) view.getTag();
        }
        holder.author.setText(reviews.get(position).author);
        holder.content.setText(reviews.get(position).content);

        return view;
    }

    class Holder{
        TextView author;
        TextView content ;
        public Holder(View v){
            author = (TextView) v.findViewById(R.id.author);
            content = (TextView) v.findViewById(R.id.content);
        }
    }
}

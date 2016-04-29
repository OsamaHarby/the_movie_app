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
public class TrailerAdapter extends BaseAdapter{
    List<Trailer> trailers;
    Context context;
    public TrailerAdapter(Context context,List<Trailer> trailers){
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
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
            view = inflater.inflate(R.layout.trailer_item,parent,false);
            holder = new Holder(view);
            view.setTag(holder);
        }
        else{
            holder = (Holder) view.getTag();
        }
        holder.title.setText(trailers.get(position).title);

        return view;
    }

    class Holder{
        TextView title ;
        public Holder(View v){
            title = (TextView) v.findViewById(R.id.trailerTitle);
        }
    }
}

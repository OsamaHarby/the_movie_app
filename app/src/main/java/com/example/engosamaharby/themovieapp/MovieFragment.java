package com.example.engosamaharby.themovieapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener {
    SendMovieDetails sendMovieDetails;
    List<MovieDetails> movies;
    GridView gridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        //  movies = new ArrayList<>();
        gridView = (GridView) v.findViewById(R.id.movieGridView);
        gridView.setOnItemClickListener(this);
        movies = null;
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        sendOnStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isNetworkConnected()) {

            DownLoadTask task = new DownLoadTask(getActivity(), this);

            task.execute("popular");
        } else {
            Toast.makeText(getActivity(), "no connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pop) {

            if (isNetworkConnected()) {
                DownLoadTask task = new DownLoadTask(getActivity(), this);
                task.execute("popular");
                sendOnStart();
            } else {
                Toast.makeText(getActivity(), "no connection", Toast.LENGTH_SHORT).show();
            }

        }

        if (item.getItemId() == R.id.topRated) {
            if (isNetworkConnected()) {
                DownLoadTask task = new DownLoadTask(getActivity(), this);
                task.execute("top_rated");
                sendOnStart();
            } else {
                Toast.makeText(getActivity(), "no connection", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.favorite) {
            if (isNetworkConnected()) {
                DataBaseAdapter dbAdabter = new DataBaseAdapter(getActivity());
                movies = dbAdabter.getFavourites();
                if (movies != null && movies.size() > 0) {
                    sendOnStart();
                }
                GridAdapter gridAdapter = new GridAdapter(getActivity(), movies);
                gridView.setAdapter(gridAdapter);
            } else {
                Toast.makeText(getActivity(), "no connection", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public void sendOnStart() {
        Log.d("hiiiiiiii", "1");
        if (isNetworkConnected()) {
            Log.d("hiiiiiiii", "2");
            DetailsFragment detailsFragment = (DetailsFragment) ((MovieActivity) getActivity()).manager.findFragmentById(R.id.details_fragment);
            if (detailsFragment != null && movies != null) {
                Log.d("hiiiiiiii", "3");
                sendMovieDetails.sendData(movies.get(0).id);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sendMovieDetails.sendData(movies.get(position).id);

    }

    public void setSendMovieDetails(SendMovieDetails sendMovieDetails) {
        this.sendMovieDetails = sendMovieDetails;
    }

    public interface SendMovieDetails {
        public void sendData(int id);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManagerCompat = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManagerCompat.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}

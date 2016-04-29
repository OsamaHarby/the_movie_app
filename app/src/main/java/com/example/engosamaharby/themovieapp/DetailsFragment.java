package com.example.engosamaharby.themovieapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Eng Osama Harby
 */
public class DetailsFragment extends Fragment implements View.OnClickListener, LinearListView.OnItemClickListener {
    DataBaseAdapter dbAdapter;
    MovieDetails myMovie;
    TextView title;
    ImageView poster;
    TextView releasedDate;
    TextView time;
    TextView rate;
    Button favourite;
    TextView overView;
    LinearListView trailerList, reviewList;
    List<Trailer> trailers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        dbAdapter = new DataBaseAdapter(getActivity());
        title = (TextView) view.findViewById(R.id.movieName);
        poster = (ImageView) view.findViewById(R.id.imageMovie);
        releasedDate = (TextView) view.findViewById(R.id.year);
        time = (TextView) view.findViewById(R.id.releasedTime);
        rate = (TextView) view.findViewById(R.id.rate);
        favourite = (Button) view.findViewById(R.id.favouriteButton);
        favourite.setOnClickListener(this);
        overView = (TextView) view.findViewById(R.id.overview);

        trailerList = (LinearListView) view.findViewById(R.id.trailersList);
        trailerList.setOnItemClickListener(this);
        reviewList = (LinearListView) view.findViewById(R.id.reviewsList);
        reviewList.setOnItemClickListener(this);
        return view;

    }

    public void setLayout(int id) {
        DownLoadDetails fetch = new DownLoadDetails();
        myMovie = new MovieDetails();
        try {

            myMovie = fetch.execute(id).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        myMovie.id = id;
        Picasso.with(getActivity()).load(myMovie.posterPath).into(poster);
        title.setText(myMovie.title);
        releasedDate.setText(myMovie.releasedDate.substring(0,4));
        time.setText(myMovie.time+" min ");
        rate.setText(myMovie.rate);
        overView.setText(myMovie.overView);

        boolean found = dbAdapter.searchMovie(myMovie.id);
        if (found){
            //dbAdapter.insertMovie(myMovie.id,myMovie.posterPath);
            favourite.setBackgroundColor(Color.CYAN);
            favourite.setText("unfavourite ");
        }
        else {
            //dbAdapter.deleteMovie(myMovie.id);
            favourite.setBackgroundColor(Color.GREEN);
            favourite.setText("make favourite ");
        }

        setTrailers();

        setReviews();

    }

    private void setReviews() {
        DownLoadReviews downLoadReviews = new DownLoadReviews();
        downLoadReviews.execute(myMovie.id);
    }

    public void setTrailers() {
        DownLoadTrailers downLoadTrailers = new DownLoadTrailers();
        try {
            trailers = downLoadTrailers.execute(myMovie.id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        boolean found = dbAdapter.searchMovie(myMovie.id);
        if (!found){
            dbAdapter.insertMovie(myMovie.id,myMovie.posterPath);
            favourite.setBackgroundColor(Color.CYAN);
            favourite.setText("unfavourite ");
        }
        else {
            dbAdapter.deleteMovie(myMovie.id);
            favourite.setBackgroundColor(Color.GREEN);
            favourite.setText("make favourite ");
        }
    }

    @Override
    public void onItemClick(LinearListView parent, View view, int position, long id) {
        if(parent.getId() == R.id.trailersList){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(position).url));
            startActivity(Intent.createChooser(intent, ""));

        }
    }

    class DownLoadDetails extends AsyncTask<Integer, Void, MovieDetails> {


        @Override
        protected MovieDetails doInBackground(Integer... params) {

            String json = getJSON("https://api.themoviedb.org/3/movie/" + params[0] + "?api_key=25685b17a9e1b83f14f8051033a21a43");
            MovieDetails myMovie = new MovieDetails();
            try {

                JSONObject jsonObject = new JSONObject(json);
                myMovie.title = jsonObject.getString("original_title");
                myMovie.id = jsonObject.getInt("id");
                myMovie.overView = jsonObject.getString("overview");
                myMovie.releasedDate = jsonObject.getString("release_date");
                myMovie.time = jsonObject.getString("runtime");
                myMovie.rate = jsonObject.getString("vote_average");
                myMovie.posterPath = "http://image.tmdb.org/t/p/w320" + jsonObject.getString("poster_path");

                return myMovie;

            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        public String getJSON(String url) {
            URL baseUrl = null;
            HttpURLConnection connection = null;
            String jsonStr = null;
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                baseUrl = new URL(url);
                connection = (HttpURLConnection) baseUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                String line;
                StringBuffer strBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    strBuffer.append(line + "\n");
                }
                jsonStr = strBuffer.toString();
                return jsonStr;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }


    }

    class DownLoadTrailers extends AsyncTask<Integer,Void,List<Trailer>>{

        @Override
        protected List<Trailer> doInBackground(Integer... params) {

            String json = getJSON("https://api.themoviedb.org/3/movie/" + params[0]+ "/videos" + "?api_key=25685b17a9e1b83f14f8051033a21a43");

            return getTrailers(json);
        }

        public List<Trailer> getTrailers(String json){
            List<Trailer> trailers =new ArrayList<>();
            try {
                JSONObject trailerObjet = new JSONObject(json);
                JSONArray res = trailerObjet.getJSONArray("results");
                for (int i =0; i<res.length(); i++){
                    JSONObject resObject = res.getJSONObject(i);
                    String key = resObject.getString("key");
                    String title = resObject.getString("name");
                    trailers.add(new Trailer(title,key));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailers;
        }

        public String getJSON(String url) {
            URL baseUrl = null;
            HttpURLConnection connection = null;
            String jsonStr = null;
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                baseUrl = new URL(url);
                connection = (HttpURLConnection) baseUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                String line;
                StringBuffer strBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    strBuffer.append(line + "\n");
                }
                jsonStr = strBuffer.toString();
                return jsonStr;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), trailers);
            trailerList.setAdapter(trailerAdapter);

        }
    }

    class DownLoadReviews extends AsyncTask<Integer,Void,List<Review>>{

        @Override
        protected List<Review> doInBackground(Integer... params) {

            String json = getJSON("https://api.themoviedb.org/3/movie/" + params[0]+ "/reviews" + "?api_key=25685b17a9e1b83f14f8051033a21a43");

            return getReviews(json);
        }

        public List<Review> getReviews(String json){
            List<Review> reviews =new ArrayList<>();
            try {
                JSONObject reviewObjet = new JSONObject(json);
                JSONArray res = reviewObjet.getJSONArray("results");
                for (int i =0; i<res.length(); i++){
                    JSONObject resObject = res.getJSONObject(i);
                    String author = resObject.getString("author");
                    String content = resObject.getString("content");
                    reviews.add(new Review(author,content));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviews;
        }

        public String getJSON(String url) {
            URL baseUrl = null;
            HttpURLConnection connection = null;
            String jsonStr = null;
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                baseUrl = new URL(url);
                connection = (HttpURLConnection) baseUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                String line;
                StringBuffer strBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    strBuffer.append(line + "\n");
                }
                jsonStr = strBuffer.toString();
                return jsonStr;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Review>reviews) {
            super.onPostExecute(reviews);
            ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), reviews);
            reviewList.setAdapter(reviewAdapter);

        }
    }


}

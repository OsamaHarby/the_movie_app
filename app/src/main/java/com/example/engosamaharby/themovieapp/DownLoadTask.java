package com.example.engosamaharby.themovieapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by Eng Osama Harby
 */
public class DownLoadTask extends AsyncTask<String, Void, List<MovieDetails> > {

    List<MovieDetails> movies = new ArrayList<>();
    Context context;
    public DownLoadTask(Context context){
        this.context = context;
    }
    @Override
    protected List<MovieDetails> doInBackground(String... params) {
        String json = getJSON("https://api.themoviedb.org/3/movie/"+params[0]+"?api_key=25685b17a9e1b83f14f8051033a21a43");
        movies = getPoster(json);
        Log.i("nnnjj", movies.get(0).id + "");
        return movies;
    }

    public String getJSON(String url){
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
            while ((line = reader.readLine())!= null){
                strBuffer.append(line + "\n");
            }
            jsonStr = strBuffer.toString();
            return jsonStr;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public List<MovieDetails> getPoster(String json){
        JSONObject jsonObject = null;
        List<MovieDetails> movies = new ArrayList<>();
        try {
            jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i=0; i<results.length(); i++){
                JSONObject object = results.getJSONObject(i);
                String posterPath = object.getString("poster_path");
                int id = object.getInt("id");
                MovieDetails movie = new MovieDetails();
                movie.id = id;
                movie.posterPath = "http://image.tmdb.org/t/p/w320"+posterPath;
                movies.add(movie);
            }
                return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
package com.example.engosamaharby.themovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eng Osama Harby
 */
public class DataBaseAdapter {
    Context context;
    DataBaseHelper helper;
    SQLiteDatabase db;

    public DataBaseAdapter(Context context) {
        this.context = context;
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertMovie(int id, String url) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.idColum, id);
        contentValues.put(helper.urlMovie, url);
        db.insert(helper.TableName, null, contentValues);
    }

    public void deleteMovie(int id) {
        String[] arrID = {id + ""};
        db.delete(helper.TableName, helper.idColum + "=?", arrID);
    }
    public boolean searchMovie(int id){

        String[] arrID = {id + ""};
        String [] arrayColumns = {helper.idColum};
        Cursor cursor = db.query(helper.TableName, arrayColumns,helper.idColum + "=?",arrID, null,null,null);
        if(cursor.moveToNext()){
            return true;
        }
        return false;
    }

    public List<MovieDetails> getFavourites(){
        List<MovieDetails> movies = new ArrayList<>();
        String [] columns = {helper.idColum, helper.urlMovie};
        Cursor cursor = db.query(helper.TableName, columns,null,null,null, null,null);
        while (cursor.moveToNext()){
            MovieDetails movie = new MovieDetails();
            movie.id = cursor.getInt(cursor.getColumnIndex(helper.idColum));
            movie.posterPath = cursor.getString(cursor.getColumnIndex(helper.urlMovie));
            movies.add(movie);
        }
        return movies;
    }

}


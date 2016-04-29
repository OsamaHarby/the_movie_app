package com.example.engosamaharby.themovieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by Eng Osama Harby
 *
 * */
public class DataBaseHelper extends SQLiteOpenHelper {

     static final String dataBaseName = "movieDataBase";
     static final String TableName = "movieTable";
     static final int version = 2;
     static final String idColum = "id";
    static final String urlMovie = "url";

    private static final String createTable = "create table " + TableName + "(" + idColum +  " Integer, " + urlMovie + " text);" ;
    private static final String deleteTable = "Drop table if exists " + TableName;

    private Context context;


    public DataBaseHelper(Context context) {
        super(context, dataBaseName, null, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(deleteTable);
        onCreate(db);
    }
}

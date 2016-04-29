package com.example.engosamaharby.themovieapp;

/**
 * Created by Eng Osama Harby on 29/04/2016.
 */
public class Trailer {
    String title;
    String url;
    public Trailer(String title, String url){
        this.title = title;
        this.url = "https://www.youtube.com/watch?v=" + url;
    }
}

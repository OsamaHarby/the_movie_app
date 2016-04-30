package com.example.engosamaharby.themovieapp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MovieActivity extends AppCompatActivity implements MovieFragment.SendMovieDetails{

    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        manager = getSupportFragmentManager();
        MovieFragment movieFragment = (MovieFragment) manager.findFragmentById(R.id.fragmentMovie);
        movieFragment.setSendMovieDetails(this);
    }

    @Override
    public void sendData(int id) {
        DetailsFragment detailsFragment = (DetailsFragment) manager.findFragmentById(R.id.details_fragment);
        if(detailsFragment == null) {
            Intent intent = new Intent(this, DetailsActivity.class);

            intent.putExtra("id", id);
            startActivity(intent);
        }else{
            detailsFragment.setLayout(id);
        }

    }
}

package com.example.engosamaharby.themovieapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Eng Osama Harby
 */
public class DetailsActivity extends AppCompatActivity {
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id",0);
        manager = getSupportFragmentManager();
        DetailsFragment detailsFragment= (DetailsFragment) manager.findFragmentById(R.id.details_fragment);
        detailsFragment.setLayout(id);

    }

}

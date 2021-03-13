package com.smartelectronics.filimo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.adapter.MovieAdapter;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class FilmArchiveActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private List<MovieModel> movieList;
    private RecyclerView filmArchiveRecycler;
    private MovieAdapter newMovieAdapter;
    private MovieModel movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_archive);

        init();
    }


    private void initToolBar(){

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        switch (getCategoryId()){

            case "1":
                toolbar.setTitle("تازه های فیلم");
                break;

            case "2":
                toolbar.setTitle("فیلم های ایرانی");
                break;

            case "3":
                toolbar.setTitle("سریال های ایرانی");
                break;
        }
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initRecycler(){

        movieList = new ArrayList<>();

        filmArchiveRecycler = findViewById(R.id.movie_more);
        newMovieAdapter = new MovieAdapter(this, movieList, MovieAdapter.SMALL_VIEW_TYPE);
        filmArchiveRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        filmArchiveRecycler.setAdapter(newMovieAdapter);

        prepareMovieData();
    }
    private void prepareMovieData(){

        ParseQuery<ParseObject> parseNewFilms = ParseQuery.getQuery("Films");
        parseNewFilms.whereEqualTo("CategoryID", getCategoryId());
        parseNewFilms.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null)
                    for(int i=0; i<objects.size(); i++){

                        Log.i("movies", "done: " + objects.get(i).getString("Name"));
                        movie = new MovieModel(objects.get(i).getString("ImageUrl"),
                                objects.get(i).getString("Name"),
                                objects.get(i).getString("EnglishName"),
                                objects.get(i).getString("Year"),
                                objects.get(i).getString("ArchiveId"),
                                objects.get(i).getObjectId());

                        movieList.add(movie);
                    }

                newMovieAdapter.notifyDataSetChanged();
                filmArchiveRecycler.scheduleLayoutAnimation();
            }
        });


    }
    private String getCategoryId(){

        Intent intent = getIntent();
        return intent.getStringExtra("CatId");
    }
    private void init(){

        initToolBar();
        initRecycler();
    }

}

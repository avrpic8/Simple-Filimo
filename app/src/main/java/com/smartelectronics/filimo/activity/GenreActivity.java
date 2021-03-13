package com.smartelectronics.filimo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.adapter.MovieAdapter;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class GenreActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView filmsRecycler;
    private List<MovieModel> filmsList;
    private MovieAdapter movieAdapter;
    private MovieModel  movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        init();
    }


    private void initToolBar(){

        toolbar = findViewById(R.id.toolbar);
        switch (getArchiveId()){

            case "1":
                toolbar.setTitle("اکشن");
                break;

            case "2":
                toolbar.setTitle("تاریخی|مذهبی");
                break;

            case "3":
                toolbar.setTitle("عاشقانه");
                break;

            case "4":
                toolbar.setTitle("علمی تخیلی");
                break;
        }
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    private void initRecyclerView(){

        filmsRecycler = findViewById(R.id.films_recycler);
        filmsList     = new ArrayList<>();
        movieAdapter  = new MovieAdapter(this, filmsList, MovieAdapter.SMALL_VIEW_TYPE);

        filmsRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        filmsRecycler.setAdapter(movieAdapter);

        showFilmsByCat();
    }
    private String getArchiveId(){

        Intent intent = getIntent();
        return intent.getStringExtra("archiveId");
    }
    private void showFilmsByCat(){

        final ParseQuery<ParseObject> films = ParseQuery.getQuery("Films");
        films.whereEqualTo("ArchiveId", getArchiveId());
        films.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                for(int i= 0; i< objects.size(); i++){

                    movie = new MovieModel(objects.get(i).getString("ImageUrl"),
                                           objects.get(i).getString("Name"),
                                           objects.get(i).getString("EnglishName"),
                                           objects.get(i).getString("Year"),
                                           objects.get(i).getString("ArchiveId"),
                                           objects.get(i).getObjectId());
                    filmsList.add(movie);
                }

                movieAdapter.notifyDataSetChanged();
                filmsRecycler.scheduleLayoutAnimation();
            }
        });

    }
    private void init(){

        initToolBar();
        initRecyclerView();
    }
}

package com.smartelectronics.filimo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.adapter.MovieAdapter;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView search_recycler;
    private List<MovieModel> movieList;
    private MovieAdapter movieAdapter;
    private MovieModel movie;

    private AppCompatImageView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
    }


    private void initToolBar(){

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("نتایج جستجو");
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
    private void initImageView(){

        noData = findViewById(R.id.no_data);
    }
    private void initRecycler(){

        search_recycler = findViewById(R.id.search_recycler_view);
        movieList   = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList, MovieAdapter.SMALL_VIEW_TYPE);
        search_recycler.setLayoutManager(new GridLayoutManager(this, 3));
        search_recycler.setAdapter(movieAdapter);

        showSearchedData(MainActivity.searchedWord);
    }
    private void showSearchedData(String searchWord){

        final ParseQuery<ParseObject> searchedFilms = ParseQuery.getQuery("Films");
        searchedFilms.whereContains("Name", searchWord);

        searchedFilms.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){

                    for(int i =0; i <objects.size(); i++){

                        movie = new MovieModel(objects.get(i).getString("ImageUrl"),
                                objects.get(i).getString("Name"),
                                objects.get(i).getString("EnglishName"),
                                objects.get(i).getString("Year"),
                                objects.get(i).getString("ArchiveId"),
                                objects.get(i).getObjectId());

                        movieList.add(movie);
                    }

                    movieAdapter.notifyDataSetChanged();
                    search_recycler.scheduleLayoutAnimation();
                }

                if (movieList.size() == 0){
                    noData.setVisibility(View.VISIBLE);
                    search_recycler.setVisibility(View.GONE);
                }
            }
        });
    }

    private void init(){

        initToolBar();
        initImageView();
        initRecycler();
    }
}

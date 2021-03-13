package com.smartelectronics.filimo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.halilibo.bvpkotlin.BetterVideoPlayer;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.adapter.MovieAdapter;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class SingleMovieActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView txtFilmName, txtFilmNameEn, txtRanking, txtSummary;
    private Button btnBuy;
    private ImageView imgCover, imgImdb;
    private MovieModel movie, similarMovies;

    // toolbar icon definations
    private AppCompatImageView imgBack, imgShare, imgDownload, imgTerrent;
    private TextView txtToolBarTitle;

    // similar video recyclerView
    private RecyclerView similarVideoList;
    private List<MovieModel> movieLists;
    private MovieAdapter movieAdapter;

    // video player
    private BetterVideoPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        init();
    }

    @Override
    public void onBackPressed() {

        this.finishAfterTransition();
    }

    private void initToolBar(){

        imgBack     = findViewById(R.id.img_back);
        imgShare    = findViewById(R.id.img_share);
        imgDownload = findViewById(R.id.img_download);
        imgTerrent  = findViewById(R.id.img_love_list);

        txtToolBarTitle = findViewById(R.id.txt_toolbar_title);
        txtToolBarTitle.setText("");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initAppBar(){

        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if(Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    txtToolBarTitle.setText(movie.getPersianMovieTitle());
                    txtToolBarTitle.setTextColor(getResources().getColor(R.color.black));

                    // change color toolbar icons
                    Drawable drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_back, null);

                    drawable.setTint(getResources().getColor(R.color.black));
                    imgBack.setImageDrawable(drawable);

                    drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_share, null);
                    drawable.setTint(getResources().getColor(R.color.black));
                    imgShare.setImageDrawable(drawable);

                    drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_cloud_download, null);
                    drawable.setTint(getResources().getColor(R.color.black));
                    imgDownload.setImageDrawable(drawable);

                    drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_turned_in_not, null);
                    drawable.setTint(getResources().getColor(R.color.black));
                    imgTerrent.setImageDrawable(drawable);
                }
                else {
                    txtToolBarTitle.setText("");

                    // change color toolbar icons
                    Drawable drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_back, null);

                    drawable.setTint(getResources().getColor(R.color.white));
                    imgBack.setImageDrawable(drawable);

                    drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_share, null);
                    drawable.setTint(getResources().getColor(R.color.white));
                    imgShare.setImageDrawable(drawable);

                    drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_cloud_download, null);
                    drawable.setTint(getResources().getColor(R.color.white));
                    imgDownload.setImageDrawable(drawable);

                    drawable = VectorDrawableCompat.create(getBaseContext().getResources(), R.drawable.ic_turned_in_not, null);
                    drawable.setTint(getResources().getColor(R.color.white));
                    imgTerrent.setImageDrawable(drawable);

                }
            }
        });
    }
    private void initButton(){

        btnBuy = findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), VideoPlayActivity.class);
                intent.putExtra("VideoUrl", movie.getVideoUrl());

                if (movie.getVideoUrl() != null)
                    startActivity(intent);
            }
        });
    }
    private void initImageView(){

        imgCover = findViewById(R.id.img_movie_cover);
        imgImdb  = findViewById(R.id.img_imDb);

        //getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        imgCover.setTransitionName("film_cover");
    }
    private void initTextViews(){

        txtFilmName   = findViewById(R.id.txt_film_name);
        txtFilmNameEn = findViewById(R.id.txt_film_name_en);
        txtRanking    = findViewById(R.id.txt_ranking);
        txtSummary    = findViewById(R.id.txt_film_summary);
    }
    private void initRecyclerViews(){

        similarVideoList = findViewById(R.id.similar_movie_list);
        movieLists = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieLists, MovieAdapter.NORMAL_VIEW_TYPE);
        
        similarVideoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        similarVideoList.setAdapter(movieAdapter);
    }
    private void prepareSingleMovieData(){

        Log.i("objectId", "prepareSingleMovieData: " + getMovieID());

        ParseQuery<ParseObject> film = ParseQuery.getQuery("Films");
        film.getInBackground(getMovieID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject objects, ParseException e) {

                if(e == null) {
                    movie = new MovieModel(objects.getString("ImageUrl"),
                            objects.getString("Name"),
                            objects.getString("EnglishName"),
                            "",
                            objects.getString("ArchiveId"),
                            objects.getObjectId(),
                            objects.getString("Ranking"),
                            objects.getString("FilmSummary"),
                            objects.getString("VideoUrl"));
                }

                Glide.with(getBaseContext()).load(movie.getImageUrl()).into(imgCover);
                txtFilmName.setText(movie.getPersianMovieTitle());
                txtFilmNameEn.setText(movie.getEnglishMovieTitle());
                txtRanking.setText(movie.getRanking() + "/10");
                txtSummary.setText(movie.getFilmSummary());

                if(movie.getRanking() == null){
                    imgImdb.setVisibility(View.INVISIBLE);
                    txtRanking.setVisibility(View.INVISIBLE);
                }else
                    imgImdb.setVisibility(View.VISIBLE);

                Log.i("Url", "done: " + movie.getVideoUrl());
            }
        });


    }
    private void showSimilarFilms(){

        Intent intent = getIntent();
        String archiveId = intent.getStringExtra("archiveId");

        final ParseQuery<ParseObject> similarFilms = ParseQuery.getQuery("Films");
        similarFilms.whereEqualTo("ArchiveId", archiveId);
        Log.i("ArchiveId", "showSimilarFilms: " + archiveId);

        similarFilms.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){

                    for (int i= 0; i< objects.size(); i++){

                        similarMovies = new MovieModel(objects.get(i).getString("ImageUrl"),
                                                      objects.get(i).getString("Name"),
                                                      objects.get(i).getString("EnglishName"),
                                                      objects.get(i).getString("Year"),
                                                      objects.get(i).getString("ArchiveId"),
                                                      objects.get(i).getObjectId(),
                                                      objects.get(i).getString("Ranking"),
                                                      objects.get(i).getString("FilmSummary"),
                                                      "");

                        if(!similarMovies.getObjectID().equals(getMovieID()))
                            movieLists.add(similarMovies);
                    }

                    movieAdapter.notifyDataSetChanged();
                    similarVideoList.scheduleLayoutAnimation();
                }
            }
        });
    }
    private String getMovieID(){

        Intent intent = getIntent();
        return intent.getStringExtra("uniqId");
    }
    private void init(){

        initAppBar();
        initToolBar();
        initImageView();
        initButton();
        initTextViews();
        initRecyclerViews();
        prepareSingleMovieData();
        showSimilarFilms();
    }
}

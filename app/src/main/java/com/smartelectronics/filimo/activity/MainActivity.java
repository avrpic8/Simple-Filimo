package com.smartelectronics.filimo.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.adapter.MovieAdapter;
import com.smartelectronics.filimo.model.MovieModel;
import com.smartelectronics.filimo.util.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener{

    // to avoid exit from program
    private static final int TIME_DELAY = 1000;
    private static long back_pressed;

    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;

    // Movie list
    private List<MovieModel> movieList, persianFilmsList, persianSeriesList, animList;
    private RecyclerView newMove, persianFilms, persianSeries, animMove;
    private MovieAdapter newMovieAdapter, persianFilmsAdapter, persianSeriesAdapter, animationAdapter;

    // Banner covers
    private SliderLayout slider;

    // receiver for internet connection
    private ConnectivityReceiver connectivityReceiver;


    // Movies objects
    private MovieModel movie;


    // text views
    private TextView txtMoreNewFilms , txtMorePerianFilms, txtMorePersianSeries;

    // search films
    private SearchView searchView;
    public static String searchedWord;

    // category id for FilmArchiveActivity activity
    private String catId;


    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            }else
                showSnackMessage(R.drawable.ic_exit_to_app,"لطفا کلید خروج را دوباره فشار دهید.");

            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);

        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("جستجو...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if( ! searchView.isIconified())
                    searchView.setIconified(true);

                myActionMenuItem.collapseActionView();

                searchedWord = query;
                startActivity(new Intent(MainActivity.this, SearchActivity.class));

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {

            TransitionManager.beginDelayedTransition((ViewGroup) this.findViewById(R.id.toolbar));
            MenuItemCompat.expandActionView(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(MainActivity.this, SubjectiveArchiveActivity.class));
        } else if (id == R.id.nav_shopping) {
            startActivity(new Intent(MainActivity.this, BuyAccountActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        //MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        slider.stopAutoCycle();

        // disable receiver
        unregisterReceiver(connectivityReceiver);
    }

    private void initToolBar(){

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);
    }
    private void initFab(){

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });
    }
    private void initDrawer(){

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        //navigationView.setItemTextColor(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
    private void initTextViews(){

        txtMoreNewFilms = findViewById(R.id.txt_more_new_move);
        txtMoreNewFilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catId = "1";
                Intent intent = new Intent(MainActivity.this, FilmArchiveActivity.class);
                intent.putExtra("CatId", catId);
                startActivity(intent);
            }
        });


        txtMorePerianFilms = findViewById(R.id.txt_more_new_movePersian);
        txtMorePerianFilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catId = "2";
                Intent intent = new Intent(MainActivity.this, FilmArchiveActivity.class);
                intent.putExtra("CatId", catId);
                startActivity(intent);
            }
        });


        txtMorePersianSeries = findViewById(R.id.txt_more_serial);
        txtMorePersianSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                catId = "3";
                Intent intent = new Intent(MainActivity.this, FilmArchiveActivity.class);
                intent.putExtra("CatId", catId);
                startActivity(intent);
            }
        });

    }
    private void initRecyclerView(){

        movieList = new ArrayList<>();
        persianFilmsList = new ArrayList<>();
        persianSeriesList = new ArrayList<>();
        animList  = new ArrayList<>();

        /// new movie list
        newMove = findViewById(R.id.new_movie_list);
        newMovieAdapter = new MovieAdapter(this, movieList, MovieAdapter.NORMAL_VIEW_TYPE);
        RecyclerView.LayoutManager layoutManagerNewMovie = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newMove.setLayoutManager(layoutManagerNewMovie);
        newMove.setAdapter(newMovieAdapter);


        /// persian films list
        persianFilms = findViewById(R.id.persian_movie_list);
        persianFilmsAdapter = new MovieAdapter(this, persianFilmsList, MovieAdapter.NORMAL_VIEW_TYPE);
        RecyclerView.LayoutManager layoutManagerPersianMovie = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        persianFilms.setLayoutManager(layoutManagerPersianMovie);
        persianFilms.setAdapter(persianFilmsAdapter);


        /// persian series list
        persianSeries = findViewById(R.id.persian_series_list);
        persianSeriesAdapter = new MovieAdapter(this, persianSeriesList, MovieAdapter.NORMAL_VIEW_TYPE);
        RecyclerView.LayoutManager layoutManagerPersianSeries = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        persianSeries.setLayoutManager(layoutManagerPersianSeries);
        persianSeries.setAdapter(persianSeriesAdapter);


        /// animations list
        animMove = findViewById(R.id.animation_list);
        animationAdapter = new MovieAdapter(this, animList, MovieAdapter.NORMAL_VIEW_TYPE);
        RecyclerView.LayoutManager layoutManagerAnim = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        animMove.setLayoutManager(layoutManagerAnim);
        animMove.setAdapter(animationAdapter);

        prepareMovieData();
    }
    private void bannerInit(){


        slider = findViewById(R.id.Slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("T-Finder", R.drawable.pic1);
        file_maps.put("First box", R.drawable.pic2);
        file_maps.put("Second box", R.drawable.pic3);
        file_maps.put("Coil 14 inch", R.drawable.pic4);
        file_maps.put("Coil 20 inch", R.drawable.pic5);
        file_maps.put("Coil 1x1 m", R.drawable.pic6);
        file_maps.put("Handle", R.drawable.pic7);

        for(String name:file_maps.keySet()){

            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            slider.addSlider(textSliderView);
        }

        slider.setPresetTransformer(1);
        //slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
    }
    private void prepareMovieData(){

        ParseQuery<ParseObject> parseFilms = ParseQuery.getQuery("Films");

        parseFilms.whereEqualTo("CategoryID", "1");
        parseFilms.findInBackground(new FindCallback<ParseObject>() {
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
                                objects.get(i).getObjectId(),
                                objects.get(i).getString("Ranking"),
                                objects.get(i).getString("FilmSummary"),
                                "");

                        movieList.add(movie);
                    }

                newMovieAdapter.notifyDataSetChanged();
                newMove.scheduleLayoutAnimation();
            }
        });


        ParseQuery<ParseObject> parsePersianFilms = ParseQuery.getQuery("Films");

        parsePersianFilms.whereEqualTo("CategoryID", "2");
        parsePersianFilms.findInBackground(new FindCallback<ParseObject>() {
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
                                objects.get(i).getObjectId(),
                                objects.get(i).getString("Ranking"),
                                objects.get(i).getString("FilmSummary"),
                                "");

                        persianFilmsList.add(movie);
                    }

                persianFilmsAdapter.notifyDataSetChanged();
                persianFilms.scheduleLayoutAnimation();
            }
        });


        ParseQuery<ParseObject> parsePersianSeries = ParseQuery.getQuery("Films");

        parsePersianSeries.whereEqualTo("CategoryID", "3");
        parsePersianSeries.findInBackground(new FindCallback<ParseObject>() {
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
                                objects.get(i).getObjectId(),
                                objects.get(i).getString("Ranking"),
                                objects.get(i).getString("FilmSummary"),
                                "");

                        persianSeriesList.add(movie);
                    }

                persianSeriesAdapter.notifyDataSetChanged();
                persianSeries.scheduleLayoutAnimation();
            }
        });

    }
    private void showSnackMessage(int icon, String message){

        Typeface typeface;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            typeface = getResources().getFont(R.font.samim_fd);
        else
            typeface = Typeface.createFromAsset(getAssets(), "fonts/samim_fd.ttf");


        Snackbar snackbar = Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(typeface);

        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.medium_margin));

        snackbar.show();
    }
    private void initReceivers(){

        connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.setConnectivityReceiverListener(this);

        registerReceiver(connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }
    private Transition enterTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(5000);

        return bounds;
    }
    private Transition returnTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(5000);

        return bounds;
    }

    private void init(){


        coordinatorLayout = findViewById(R.id.crd_layout);
        initToolBar();
        initFab();
        initDrawer();
        initTextViews();
        initRecyclerView();
        bannerInit();
        initReceivers();

        ParseInstallation.getCurrentInstallation().saveInBackground();

       getWindow().setSharedElementEnterTransition(enterTransition());
        getWindow().setSharedElementReturnTransition(returnTransition());
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        Log.i("internet", "onNetworkConnectionChanged: " + isConnected);
        if(isConnected)
            showSnackMessage(R.drawable.ic_wifi_on, this.getString(R.string.connect_to_internet));
        else
            showSnackMessage(R.drawable.ic_wifi_off, this.getString(R.string.disconnect_from_internet));

    }
}

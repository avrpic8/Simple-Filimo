package com.smartelectronics.filimo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.adapter.ArchiveMovieAdapter;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectiveArchiveActivity extends AppCompatActivity {


    private Toolbar toolbar;

    private RecyclerView subjectiveRecycler;
    private List<MovieModel> listArchive;
    private ArchiveMovieAdapter archiveAdapter;
    private MovieModel       archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjective_archive);

        init();
    }


    private void initToolBar(){

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("آرشیو موضوعی");
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
    private void initRecycler(){

        subjectiveRecycler = findViewById(R.id.list_archive);
        listArchive = new ArrayList<>();
        archiveAdapter = new ArchiveMovieAdapter(this, listArchive);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        subjectiveRecycler.setLayoutManager(layoutManager);

        subjectiveRecycler.setAdapter(archiveAdapter);
        prepareData();
    }
    private void prepareData(){

        ParseQuery<ParseObject> parseArchive = ParseQuery.getQuery("SubjectiveArchive");

        parseArchive.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null)
                    for(int i= 0; i< objects.size(); i++){

                        Log.i("Archive", "done: " + objects.get(i).getString("ArchiveName"));
                        archive = new MovieModel(objects.get(i).getString("ArchiveImage"),
                                                 objects.get(i).getString("ArchiveName"),
                                                 objects.get(i).getString("ArchiveName_en"),
                                                 objects.get(i).getString("ArchiveId"));

                        listArchive.add(archive);
                    }

                archiveAdapter.notifyDataSetChanged();
                subjectiveRecycler.scheduleLayoutAnimation();

            }
        });
    }
    private void init(){

        initToolBar();
        initRecycler();
    }



}

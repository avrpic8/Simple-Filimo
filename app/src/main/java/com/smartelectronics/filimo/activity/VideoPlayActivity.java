package com.smartelectronics.filimo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.halilibo.bvpkotlin.BetterVideoPlayer;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.smartelectronics.filimo.R;

public class VideoPlayActivity extends AppCompatActivity {

    private BetterVideoPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlayer.stop();
    }



    private String getVideoUrl(){

        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("VideoUrl");
        return videoUrl;
    }
    private void initVideoPlayer(){

        videoPlayer = findViewById(R.id.player);
        videoPlayer.setAutoPlay(false);

        if(getVideoUrl() != null)
            videoPlayer.setSource(Uri.parse(getVideoUrl()));
        videoPlayer.start();
    }
    private void init(){

        initVideoPlayer();
    }
}

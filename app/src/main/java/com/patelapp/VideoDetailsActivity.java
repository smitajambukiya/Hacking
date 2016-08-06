package com.patelapp;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.adapter.PhotoPagerAdapter;

import java.util.ArrayList;

/**
 * Created by AndroidDevloper on 2/21/2016.
 */


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.patelapp.utils.StatusBarUtils;

public class VideoDetailsActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,YouTubePlayer.OnFullscreenListener,YouTubePlayer.PlayerStateChangeListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private ImageView ivBack;

    EntityGallery entityGallery;
    String gallery_id;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer youTubePlayer;
    //ref
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDesc;

    Typeface face;
    public VideoDetailsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grid_item_video);

        initViews();

        if (getIntent().getExtras() != null) {
            gallery_id = getIntent().getExtras().getString("key_gallery_id");

            entityGallery = DataAccessManager.getGalleryRecords(gallery_id);

            tvTitle.setText(entityGallery.getTitle());
            tvDesc.setText(entityGallery.getG_desc());
            tvLocation.setText(entityGallery.getLocation());
            youTubeView.initialize(Const.YOUTUBE_API_KEY, this);
        }


    }


    private void initViews() {

        face = Typeface.createFromAsset(getAssets(),
                "fonts/"+getString(R.string.font_guj));
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvDesc = (TextView) findViewById(R.id.tvDesc);

        tvTitle.setTypeface(face);
        tvLocation.setTypeface(face);
        tvDesc.setTypeface(face);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            this.youTubePlayer = player;
            youTubePlayer.setOnFullscreenListener(this);
            youTubePlayer.setPlayerStateChangeListener(this);


            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(entityGallery.getImg_video());

            // Hiding player controls
            player.setPlayerStyle(PlayerStyle.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(entityGallery.getImg_video(), this);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (youTubePlayer != null)
                youTubePlayer.setFullscreen(true);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (youTubePlayer != null)
                youTubePlayer.setFullscreen(false);
        }

    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }


    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;


    @Override
    public void onFullscreen(boolean fullScreen) {
        if (fullScreen)
            setRequestedOrientation(LANDSCAPE_ORIENTATION);
        else
            setRequestedOrientation(PORTRAIT_ORIENTATION);

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        StatusBarUtils.hide(this);
    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
    }
}





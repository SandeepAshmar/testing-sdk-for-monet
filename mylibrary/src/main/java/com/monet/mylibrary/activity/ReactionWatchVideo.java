package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.video.VideoPojo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.utils.SdkUtils.convertVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.progressDialog;


public class ReactionWatchVideo extends AppCompatActivity {

    private ImageView img_toolbarBack;
    private RecyclerView recyclerView;
    private VideoView videoView;
    private SeekBar seekBarVideo;
    private TextView tv_timeVideoView;
    private Handler handler;
    private Runnable runnable;
    boolean doubleBackToExitPressedOnce = false;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_watch_video);

        img_toolbarBack = findViewById(R.id.img_toolbarBack);
        img_toolbarBack.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerViewReaction);
        videoView = findViewById(R.id.videoView);
        seekBarVideo = findViewById(R.id.seekBarVideo);
        tv_timeVideoView = findViewById(R.id.tv_timeVideoView);
        seekBarVideo.setEnabled(false);

        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        handler = new Handler();
        getVideoUrlFromLink("https://www.youtube.com/watch?v=n7-QjBAmlSM");
//        getVideoUrlFromLink(getVideoUrl(this));

    }

    private void getVideoUrlFromLink(String url) {
        progressDialog(this, "Please wait...", true);
        apiInterface.getMp4VideoUrl(url)
                .enqueue(new Callback<VideoPojo>() {
                    @Override
                    public void onResponse(Call<VideoPojo> call, Response<VideoPojo> response) {
                        progressDialog(ReactionWatchVideo.this, "Please wait...", false);
                        if(response.body().getCode().equals("200")){
                            for (int i = 0; i < response.body().getResponse().size(); i++) {
                                if(response.body().getResponse().get(i).getQuality().equals("medium")){
                                    playVideo(response.body().getResponse().get(i).getUrl());
                                    break;
                                }if(i == response.body().getResponse().size()){
                                    playVideo(response.body().getResponse().get(0).getUrl());
                                    break;
                                }
                            }
                        }else{
                            Toast.makeText(ReactionWatchVideo.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoPojo> call, Throwable t) {
                        progressDialog(ReactionWatchVideo.this, "Please wait...", false);
                    }
                });
    }

    private void playVideo(String url) {
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                seekBarVideo.setMax(videoView.getDuration());
                setSeekBar();
            }
        });
    }

    private void setSeekBar() {
        seekBarVideo.setProgress(videoView.getCurrentPosition());
        if (videoView.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    tv_timeVideoView.setText(convertVideoTime((videoView.getDuration() - videoView.getCurrentPosition())));
                    setSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 0);
    }


}

package com.monet.mylibrary.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.video.VideoPojo;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.utils.SdkPreferences.getVideoUrl;
import static com.monet.mylibrary.utils.SdkUtils.convertVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.progressDialog;

public class PlayVideoAndRecordScreen extends AppCompatActivity implements ConnectCheckerRtmp {

    private ImageView img_toolbarBack,img_detect;
    private VideoView videoViewEmotion;
    private ProgressBar pb_emotion, pb_emotionRound;
    private TextView tv_videoTimeEmotion;
    private SurfaceView surfaceViewEmotion;
    private String video_Url;
    private ApiInterface apiInterface;
    private RtmpCamera1 rtmpCamera1;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_and_record_screen);

        img_toolbarBack = findViewById(R.id.img_toolbarBack);
        videoViewEmotion = findViewById(R.id.videoViewEmotion);
        pb_emotion = findViewById(R.id.pb_emotion);
        tv_videoTimeEmotion = findViewById(R.id.tv_videoTimeEmotion);
        surfaceViewEmotion = findViewById(R.id.surfaceViewEmotion);
        img_detect = findViewById(R.id.img_detect);
        pb_emotionRound = findViewById(R.id.pb_emotionRound);

        handler = new Handler();
        img_toolbarBack.setVisibility(View.GONE);

        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        getVideoUrlFromLink(getVideoUrl(this));

    }

    private void getVideoUrlFromLink(String url) {
        progressDialog(this, "Please wait...", true);
        apiInterface.getMp4VideoUrl(url)
                .enqueue(new Callback<VideoPojo>() {
                    @Override
                    public void onResponse(Call<VideoPojo> call, Response<VideoPojo> response) {
                        progressDialog(PlayVideoAndRecordScreen.this, "Please wait...", false);
                        if (response.body().getCode().equals("200")) {
                            for (int i = 0; i < response.body().getResponse().size(); i++) {
                                if (response.body().getResponse().get(i).getQuality().equals("medium")) {
                                    playVideo(response.body().getResponse().get(i).getUrl());
                                    break;
                                }
                                if (i == response.body().getResponse().size()) {
                                    playVideo(response.body().getResponse().get(0).getUrl());
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(PlayVideoAndRecordScreen.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoPojo> call, Throwable t) {
                        progressDialog(PlayVideoAndRecordScreen.this, "Please wait...", false);
                    }
                });
    }

    private void playVideo(String url) {

        Uri uri = Uri.parse(url);
        videoViewEmotion.setVideoURI(uri);
        videoViewEmotion.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoViewEmotion.start();
                pb_emotion.setMax(videoViewEmotion.getDuration());
                setProgressBar();
            }
        });

        videoViewEmotion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    private void setProgressBar() {
        pb_emotion.setProgress(videoViewEmotion.getCurrentPosition());
        if (videoViewEmotion.isPlaying()) {
            pb_emotionRound.setVisibility(View.GONE);
            runnable = new Runnable() {
                @Override
                public void run() {
                    tv_videoTimeEmotion.setText(convertVideoTime((videoViewEmotion.getDuration() - videoViewEmotion.getCurrentPosition())));
                    setProgressBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }else{
            if(!convertVideoTime(videoViewEmotion.getCurrentPosition()).equalsIgnoreCase("00:00")){
                pb_emotionRound.setVisibility(View.VISIBLE);
            }else{
                pb_emotionRound.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onConnectionSuccessRtmp() {
        
    }

    @Override
    public void onConnectionFailedRtmp(String s) {

    }

    @Override
    public void onDisconnectRtmp() {

    }

    @Override
    public void onAuthErrorRtmp() {

    }

    @Override
    public void onAuthSuccessRtmp() {

    }
}
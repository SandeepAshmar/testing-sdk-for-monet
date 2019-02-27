package com.monet.mylibrary.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.utils.SdkPreferences.getVideoUrl;
import static com.monet.mylibrary.utils.SdkUtils.progressDialog;

public class PlayVideoAndRecordScreen extends AppCompatActivity  {

    private ImageView img_toolbarBack,img_detect;
    private VideoView videoViewEmotion;
    private ProgressBar pb_emotion;
    private TextView tv_videoTimeEmotion;
    private SurfaceView surfaceViewEmotion;
    private String video_Url;
    private ApiInterface apiInterface;

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
            }
        });

        videoViewEmotion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }


}
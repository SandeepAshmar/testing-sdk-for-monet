package com.monet.mylibrary.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.pedro.encoder.input.video.Camera1ApiManager;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;
import static com.monet.mylibrary.utils.SdkConstant.RTMP_PASSWORD;
import static com.monet.mylibrary.utils.SdkConstant.RTMP_URL;
import static com.monet.mylibrary.utils.SdkConstant.RTMP_USERNAME;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLength;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.getVideoUrl;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.setQuestionType;
import static com.monet.mylibrary.utils.SdkUtils.convertVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.progressDialog;

public class PlayVideoAndRecordScreen extends AppCompatActivity implements ConnectCheckerRtmp {

    private ImageView img_toolbarBack, img_detect;
    private VideoView videoViewEmotion;
    private ProgressBar pb_emotion, pb_emotionRound;
    private TextView tv_videoTimeEmotion;
    private SurfaceView surfaceViewEmotion;
    private String video_Url;
    private ApiInterface apiInterface;
    private RtmpCamera1 rtmpCamera1;
    private Handler handler;
    private Runnable runnable;
    private short flag = 0;

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

        rtmpCamera1 = new RtmpCamera1(surfaceViewEmotion, this);
        rtmpCamera1.startPreview();
        playVideo();
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
                                    video_Url = response.body().getResponse().get(i).getUrl();
                                    break;
                                }
                                if (i == response.body().getResponse().size()) {
                                    video_Url = response.body().getResponse().get(0).getUrl();
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

    private void playVideo() {
        if (!rtmpCamera1.isStreaming()) {
            rtmpCamera1.setAuthorization(RTMP_USERNAME, RTMP_PASSWORD);
        }
        Uri uri = Uri.parse(video_Url);
        videoViewEmotion.setVideoURI(uri);
        videoViewEmotion.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoViewEmotion.start();
                pb_emotion.setMax(videoViewEmotion.getDuration());
                setProgressBar();
                recordingStart();
            }
        });

        videoViewEmotion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                rtmpCamera1.stopStream();
                rtmpCamera1.disableFaceDetection();
                rtmpCamera1.stopRecord();
                rtmpCamera1.stopPreview();
                setScreen();
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
        } else {
            if (!convertVideoTime(videoViewEmotion.getCurrentPosition()).equalsIgnoreCase("00:00")) {
                pb_emotionRound.setVisibility(View.VISIBLE);
            } else {
                pb_emotionRound.setVisibility(View.GONE);
            }
        }
    }

    private void recordingStart(){
        if(rtmpCamera1.isRecording() || prepareEncoders()){
            String rtmpUrl = RTMP_URL+getCfId(this);
            rtmpCamera1.startStream(rtmpUrl);
            if(flag == 0){
                try{
                    rtmpCamera1.switchCamera();
                }catch (CameraOpenException e){
                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                flag++;
            }

            try{
                rtmpCamera1.enableFaceDetection(new Camera1ApiManager.FaceDetectorCallback() {
                    @Override
                    public void onGetFaces(Camera.Face[] faces) {
                        changeImage(faces.length);
                    }
                });
            }catch (Exception e){
                Log.d("TAG", "Exception found in face detection"+e.getMessage());
            }
        }else{
            Toast.makeText(this, "Error preparing stream, this device can not do this....", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean prepareEncoders(){
        int width = 320;
        int height = 240;

        return rtmpCamera1.prepareVideo(width, height, 30, 153600,
                false, CameraHelper.getCameraOrientation(this));

    }

    private void changeImage(int length) {
    }

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideoAndRecordScreen.this, "onConnectionSuccessRtmp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtmp(String s) {
        rtmpCamera1.stopStream();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideoAndRecordScreen.this, "onConnectionFailedRtmp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideoAndRecordScreen.this, "onDisconnectRtmp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideoAndRecordScreen.this, "onAuthErrorRtmp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideoAndRecordScreen.this, "onAuthSuccessRtmp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setScreen() {
        int count = Integer.valueOf(getCmpLength(this));
        int i = getCmpLengthCount(this);

        if (count == 1) {
            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    try {
                        stagingJson.put("2", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
                try {
                    stagingJson.put("6", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        } else {
            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    try {
                        stagingJson.put("2", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
                try {
                    stagingJson.put("6", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        }
    }
}
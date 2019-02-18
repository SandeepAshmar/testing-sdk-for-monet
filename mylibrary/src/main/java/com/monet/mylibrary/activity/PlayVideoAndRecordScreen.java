package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.vimeoModel.VimeoPojo;
import com.monet.mylibrary.utils.SdkConstant;
import com.monet.mylibrary.utils.SdkPreferences;
import com.pedro.encoder.input.video.Camera1ApiManager;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;

public class PlayVideoAndRecordScreen extends AppCompatActivity implements ConnectCheckerRtmp {

    private VideoView video;
    private SurfaceView cameraView;
    private RtmpCamera1 rtmpCamera1;
    private ImageView camera_icon, img_detect;
    private boolean count = true, detecting = false;
    private boolean connectionStatus;
    boolean faceDetect = false;
    boolean doubleBackToExitPressedOnce = false;
    private Handler handler1;
    private String bitrate = "150";
    private Runnable runnable1;
    private int flag = 0;
    private int detectedTime = 0;
    String video_url = "";
    RelativeLayout videoView;
    ProgressBar progressbar;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean flagForInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_and_record_screen);
        video = findViewById(R.id.video);
        cameraView = findViewById(R.id.surfaceView);
        img_detect = findViewById(R.id.img_detect);
        videoView = findViewById(R.id.videoView);
        progressbar = findViewById(R.id.progressbar);
        camera_icon = findViewById(R.id.camera_icon);

        handler1 = new Handler();

        video_url = SdkPreferences.getVideoUrl(this);

        rtmpCamera1 = new RtmpCamera1(cameraView, this);
        rtmpCamera1.startPreview();

        if (video_url.contains("vimeo")) {
            video_url = video_url.replace("https://vimeo.com/", "");
            getVimeoMPFour();
        } else {
            playVideo();
        }
        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVisibility(View.VISIBLE);
                camera_icon.setVisibility(View.GONE);
                hideView();
            }
        });
    }

    private void getVimeoMPFour() {
        ApiInterface apiInterface = BaseUrl.getVimeoClient().create(ApiInterface.class);
        Call<VimeoPojo> pojoCall = apiInterface.getVideoDetails(video_url);

        pojoCall.enqueue(new Callback<VimeoPojo>() {
            @Override
            public void onResponse(Call<VimeoPojo> call, Response<VimeoPojo> response) {
                if (response.body() != null) {
                    int size = response.body().getVimeoReq().getVimeoFiles().getProgressives().size();
                    int i = 0;
                    for (; i < size; i++) {
                        if (response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getQuality().contains("480")) {
                            video_url = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getUrl();
                            break;
                        } else if (response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getQuality().contains("540")) {
                            video_url = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getUrl();
                            break;
                        } else if (response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getQuality().contains("720")) {
                            video_url = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getUrl();
                            break;
                        } else if (i == size - 1) {
                            video_url = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(0).getUrl();
                        }
                    }
                    playVideo();
                }
            }
            @Override
            public void onFailure(Call<VimeoPojo> call, Throwable t) {
                Toast.makeText(PlayVideoAndRecordScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playVideo() {

        if (!rtmpCamera1.isStreaming()) {
            rtmpCamera1.setAuthorization(SdkConstant.RTMP_USERNAME, SdkConstant.RTMP_PASSWORD);
        }
        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);
        video.start();
        recordingStart();

        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayVideoAndRecordScreen.this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PlayVideoAndRecordScreen.this, "Button Clicked...", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                progressbar.setVisibility(View.GONE);
                return true;
            }
        });
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setProgressBar();
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(PlayVideoAndRecordScreen.this, "video Finish", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        });

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                rtmpCamera1.stopStream();
                rtmpCamera1.disableFaceDetection();
                setScreen();
            }
        });
    }

    // @SuppressLint("NewApi")
    private void setProgressBar() {

        runnable = new Runnable() {
            @Override
            public void run() {

                if (!networkIsAvailable()) {
                    progressbar.setVisibility(View.VISIBLE);
                    if (flagForInternet == false) {
                        Toast.makeText(PlayVideoAndRecordScreen.this, "Your internet connection is Interrupted", Toast.LENGTH_SHORT).show();
                    }
                    flagForInternet = true;
                } else {
                    if (video.isPlaying()) {
                        progressbar.setVisibility(View.GONE);
                        flagForInternet = false;
                    } else {

                    }
                }

                handler.postDelayed(runnable, 1000);
            }
        };
        runnable.run();

    }

    private boolean networkIsAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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

    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "RTMP Connected...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onConnectionFailedRtmp(String s) {
        runOnUiThread(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                rtmpCamera1.stopStream();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PlayVideoAndRecordScreen.this);
                builder.setMessage("Your internet connection is slow, Please try again");
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //  startActivity(new Intent(PlayVideoAndRecordScreen.this, FaceDetectInstructions.class));
                        Toast.makeText(PlayVideoAndRecordScreen.this, "RTMP Connection Failed. Handle this.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "RTMP Disconnected...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "RTMP Auth Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "RTMP Auth Success...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recordingStart() {

        if (rtmpCamera1.isRecording() || prepareEncoders()) {
            String rtmpUrl = SdkConstant.RTMP_URL + SdkPreferences.getCfId(this);
            rtmpCamera1.startStream(rtmpUrl);
            if (flag == 0) {
                try {
                    rtmpCamera1.switchCamera();
                } catch (final CameraOpenException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                flag++;
            }
            try {
                rtmpCamera1.enableFaceDetection(new Camera1ApiManager.FaceDetectorCallback() {
                    @Override
                    public void onGetFaces(Camera.Face[] faces) {

                        changeImage(faces.length);
                    }
                });
            } catch (Exception e) {
                Log.d("MainActivity", "Exception found in face detection..." + e.getMessage());
            }

        } else {
            Toast.makeText(this, "Error preparing stream, This device can not do it...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean prepareEncoders() {
        int width = 320;
        int height = 240;
        return rtmpCamera1.prepareVideo(width, height, Integer.parseInt("30"),
                Integer.parseInt(bitrate) * 1024,  // bitrate
                false, CameraHelper.getCameraOrientation(this));
    }

    private void changeImage(int i) {
        faceDetect = true;
        if (i == 0) {
            img_detect.setImageResource(R.drawable.ic_red_back);
            count = true;
            detecting = true;
            notDetectDialog();
        } else {
            img_detect.setImageResource(R.drawable.ic_green_back);
            if (count) {
                countPercentage();
                detecting = false;
            }
        }
    }

    private void notDetectDialog() {
        if (detecting) {
            runnable1 = new Runnable() {
                @Override
                public void run() {
                    if (detecting) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PlayVideoAndRecordScreen.this);
                        builder.setMessage("It seems you are not in front of the camera from last 5 seconds, Please align yourself and play video again.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
            };
            handler.postDelayed(runnable1, 5000);
        } else {
            handler1.removeCallbacks(runnable1);
        }
    }

    private void countPercentage() {
        count = false;
        runnable = new Runnable() {
            @Override
            public void run() {
                detectedTime = detectedTime + 1;
                count = true;
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void hideView() {

        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {

                videoView.setVisibility(View.INVISIBLE);
                camera_icon.setVisibility(View.VISIBLE);

            }
        }, 3000);
    }

    private void setScreen() {
        int count = Integer.valueOf(SdkPreferences.getCmpLength(this));
        int i = SdkPreferences.getCmpLengthCount(this);

        if (count == 1) {
            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    SdkPreferences.setQuestionType(this, "pre");
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    SdkPreferences.setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = SdkPreferences.getCmpLengthCountFlag(this);
                SdkPreferences.setCmpLengthCountFlag(this, flag + 1);
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
                    SdkPreferences.setQuestionType(this, "pre");
                    try {
                        stagingJson.put("2", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    SdkPreferences.setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = SdkPreferences.getCmpLengthCountFlag(this);
                SdkPreferences.setCmpLengthCountFlag(this, flag + 1);
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

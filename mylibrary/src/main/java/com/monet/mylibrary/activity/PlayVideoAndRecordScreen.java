package com.monet.mylibrary.activity;

import android.app.AlertDialog;
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
import android.telephony.TelephonyManager;
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

import java.util.concurrent.TimeUnit;

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
    private Runnable runnable1;
    private Handler handler;
    private Handler handler1;
    private Runnable runnable;
    private short flag = 0, bitrate = 150;
    private int detectedTime = 0, minVisionTime = 0;
    private boolean count = true, detecting = false, faceDetect = false, doubleBackToExitPressedOnce = false, connectionStatus;

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
        handler1 = new Handler();
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
                                    video_Url = response.body().getResponse().get(i).getUrl();
                                    break;
                                }
                                if (i == response.body().getResponse().size()) {
                                    video_Url = response.body().getResponse().get(0).getUrl();
                                    break;
                                }
                            }
                            rtmpCamera1 = new RtmpCamera1(surfaceViewEmotion, PlayVideoAndRecordScreen.this);
                            rtmpCamera1.startPreview();
                            playVideo();
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
                recordingStart();
                videoViewEmotion.start();
                pb_emotion.setMax(videoViewEmotion.getDuration());
                minVisionTime = (videoViewEmotion.getDuration() * 70) / 100;
                minVisionTime = (int) TimeUnit.MILLISECONDS.toSeconds(minVisionTime);
                setProgressBar();
            }
        });

        videoViewEmotion.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(PlayVideoAndRecordScreen.this, "There is something went wrong please try again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PlayVideoAndRecordScreen.this, LandingPage.class));
                finish();
                return false;
            }
        });

        videoViewEmotion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                detecting = false;
                try {
                    stagingJson.put("4", "5");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rtmpCamera1.stopStream();
                rtmpCamera1.stopPreview();
                rtmpCamera1.disableFaceDetection();
                if (detectedTime >= minVisionTime) {
                    setScreen();
                    finish();
                } else {
                    if (faceDetect) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PlayVideoAndRecordScreen.this);
                        builder1.setCancelable(false);
                        builder1.setMessage("Sorry you were not in the frame");
                        builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                detectedTime = 0;
                                playVideo();
                                startActivity(new Intent(PlayVideoAndRecordScreen.this, PlayVideoAndRecordScreen.class));
                                finish();
                            }
                        });
                        builder1.show();
                    } else {
                        setScreen();
                        finish();
                    }

                }
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

    private void recordingStart() {
        checkNetworkType();
        if (rtmpCamera1.isRecording() || prepareEncoders()) {
            String rtmpUrl = RTMP_URL + getCfId(this);
            Log.d("TAG", "recordingStart: rtmpurl " + rtmpUrl);
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

    private void checkNetworkType() {

        connectionStatus = isConnectedFast(getApplicationContext());
        if (!connectionStatus) {
            bitrate = 150;
            Log.d("Connection Status", "POOR CONNECTION");

        } else {
            Log.d("Connection Status", "GOOD CONNECTION");
            bitrate = 200;

        }

    }

    private boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    private NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    private boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;

        } else if (type == ConnectivityManager.TYPE_MOBILE) {

            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps

                // Above API level 7, make sure to set android:targetSdkVersion to appropriate level to use these

                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private boolean prepareEncoders() {
        return rtmpCamera1.prepareVideo(320, 240, 30,
                bitrate * 1024, false, CameraHelper.getCameraOrientation(this));
    }

    private void changeImage(int length) {
        faceDetect = true;
        if (length == 0) {
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
                    if(detecting){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayVideoAndRecordScreen.this);
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
        videoViewEmotion.stopPlayback();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideoAndRecordScreen.this, "There is something went wrong please try again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PlayVideoAndRecordScreen.this, LandingPage.class));
                finish();
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
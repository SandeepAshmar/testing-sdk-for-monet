package com.monet.mylibrary.activity;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.monet.mylibrary.BuildConfig;
import com.monet.mylibrary.R;
import com.pedro.encoder.input.video.Camera1ApiManager;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import androidx.appcompat.app.AppCompatActivity;
import static com.monet.mylibrary.activity.LandingPage.sequenceList;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLength;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.getPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.getVideoUrl;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.setPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.setQuestionType;
import static com.monet.mylibrary.utils.SdkUtils.convertVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

public class PlayVideoAndRecordScreen extends AppCompatActivity implements ConnectCheckerRtmp {

    private ImageView img_toolbarBack, img_detect;
    private VideoView videoViewEmotion;
    private LinearLayout ll_surfaceView;
    private ProgressBar pb_emotion, pb_emotionRound;
    private TextView tv_videoTimeEmotion;
    private SurfaceView surfaceViewEmotion;
    private RtmpCamera1 rtmpCamera1;
    private Handler handler;
    private Runnable runnable;
    private short flag = 0, bitrate = 150;
    private boolean doubleBackToExitPressedOnce = false, connectionStatus;

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
        ll_surfaceView = findViewById(R.id.ll_surfaceView);

        rtmpCamera1 = new RtmpCamera1(surfaceViewEmotion,PlayVideoAndRecordScreen.this);


        handler = new Handler();
        img_toolbarBack.setVisibility(View.GONE);

        playVideo(getVideoUrl(PlayVideoAndRecordScreen.this));

        img_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_surfaceView.setVisibility(View.VISIBLE);
                img_detect.setVisibility(View.INVISIBLE);
                changeView();
            }
        });

        String pageStage = getPageStage(PlayVideoAndRecordScreen.this);
        pageStage = pageStage.replace("emotion=face-detection-complete", "emotion=face-recodring-start");
        setPageStage(PlayVideoAndRecordScreen.this, pageStage);
        sendStagingData(PlayVideoAndRecordScreen.this, 0);
    }


    private void playVideo(String video_Url) {
        Uri uri = Uri.parse(video_Url);
        videoViewEmotion.setVideoURI(uri);
        videoViewEmotion.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                recordingStart();
                videoViewEmotion.start();
                pb_emotion.setMax(videoViewEmotion.getDuration());
                setProgressBar();
                changeView();
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
                rtmpCamera1.stopStream();
                rtmpCamera1.stopPreview();
                rtmpCamera1.disableFaceDetection();
                String pageStage = getPageStage(PlayVideoAndRecordScreen.this);
                pageStage = pageStage.replace("emotion=face-recodring-start", "emotion=face-recodring-complete");
                setPageStage(PlayVideoAndRecordScreen.this, pageStage);
                sendStagingData(PlayVideoAndRecordScreen.this, 0);
                setScreen();
                finish();
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
            String rtmpUrl = BuildConfig.rtmpUrl + getCfId(this);
//            Log.d("TAG", "recordingStart: rtmpurl " + rtmpUrl);
            rtmpCamera1.startStream(rtmpUrl);
            if (flag == 0) {
                try {
                    rtmpCamera1.startPreview();
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
//                        changeImage(faces.length);
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

    private void changeView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_surfaceView.setVisibility(View.INVISIBLE);
                img_detect.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    @Override
    public void onConnectionSuccessRtmp() {
    }

    @Override
    public void onConnectionFailedRtmp(String s) {
        rtmpCamera1.stopStream();
        videoViewEmotion.stopPlayback();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String pageStage = getPageStage(PlayVideoAndRecordScreen.this);
                pageStage = pageStage.replace("emotion=face-recodring-start", "emotion=face-recording-problem");
                setPageStage(PlayVideoAndRecordScreen.this, pageStage);
                sendStagingData(PlayVideoAndRecordScreen.this, 0);
                Toast.makeText(PlayVideoAndRecordScreen.this, "There is something went wrong please try again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PlayVideoAndRecordScreen.this, LandingPage.class));
                finish();
            }
        });
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

    private void setScreen() {
        int count = getCmpLength(this);
        int i = getCmpLengthCount(this);

        if (count == 1) {
            if (sequenceList.size() > i) {
                if (sequenceList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        } else {
            if (sequenceList.size() > i) {
                if (sequenceList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
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
package com.monet.mylibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.utils.SdkConstant.YOUTUTBE_KEY;

public class PlayVideoAndRecordScreen extends AppCompatActivity implements YouTubePlayer.PlayerStateChangeListener,ConnectCheckerRtmp {

    private VideoView video;
    private YouTubePlayerView youTubePlayerView;
    private SurfaceView cameraView;
    private RtmpCamera1 rtmpCamera1;
    public static ImageView camera_icon, img_detect;
    private boolean count = true, detecting = false;
    private boolean connectionStatus;
    boolean faceDetect = false;
    boolean doubleBackToExitPressedOnce = false;
    private Handler handler1;
    private String bitrate = "150";
    private Runnable runnable1;
    private int flag = 0;
    private int detectedTime = 0;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    //    String video_url = "http://dev.monetrewards.com/uploads/netflix/campaign_video/1871.mp4";
    // String video_url = "https://www.youtube.com/watch?v=TqFzNOxyk4w";
//    String video_url = "https://vimeo.com/146033641";
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
        youTubePlayerView = findViewById(R.id.youtube_view);
        video = findViewById(R.id.video);
        cameraView = findViewById(R.id.surfaceView);
        img_detect = findViewById(R.id.img_detect);
        videoView = findViewById(R.id.videoView);
        progressbar = findViewById(R.id.progressbar);
        camera_icon = findViewById(R.id.camera_icon);

        handler1 = new Handler();

        video_url = SdkPreferences.getVideoUrl(this);

        rtmpCamera1 = new RtmpCamera1(cameraView, this);
        if (!rtmpCamera1.isStreaming()) {
            rtmpCamera1.setAuthorization(SdkConstant.RTMP_USERNAME, SdkConstant.RTMP_PASSWORD);
        }
        rtmpCamera1.startPreview();

        if (video_url.contains("youtube")) {
            playYouTube();
            video_url = video_url.replace("https://www.youtube.com/watch?v=", "");
            youTubePlayerView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        } else if (video_url.contains("vimeo")) {
            youTubePlayerView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            video_url = video_url.replace("https://vimeo.com/", "");
            getVimeoMPFour();
        } else {
            playMonetVideo();
            youTubePlayerView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
        }

        if (checkCameraPermission()) {
            Log.e("Tag", "Camera and External storage Permission's are allowed");
        } else {
            requestCameraPermission();
        }

    }

    private boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale
                (this, Manifest.permission.CAMERA)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
            alertDialog.setMessage("You Have To Give Permission From Your Device Setting To go in Setting Please Click on Settings Button");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1012);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.DialogTheme);
            alertDialog.setMessage("You Have To Give Permission From Your Device Setting To go in Setting Please Click on Settings Button");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }else{

        }

    }

    private void getVimeoMPFour() {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
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

                    playVimeoPlayer();
                }
            }

            @Override
            public void onFailure(Call<VimeoPojo> call, Throwable t) {

            }
        });
    }

    private void playMonetVideo() {
        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);
        video.start();

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
                Toast.makeText(PlayVideoAndRecordScreen.this, "Monet Video Finish", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        });

        recordingStart();

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                rtmpCamera1.stopStream();
            }
        });
    }

    // @SuppressLint("NewApi")
    private void setProgressBar() {


        runnable = new Runnable() {
            @Override
            public void run() {

                if(!networkIsAvailable()){
                    progressbar.setVisibility(View.VISIBLE);
                    if(flagForInternet == false){
                        Toast.makeText(PlayVideoAndRecordScreen.this, "Your internet connection is Interrupted", Toast.LENGTH_SHORT).show();
                    }
                    flagForInternet = true;
                }else {
                    if(video.isPlaying()){
                        progressbar.setVisibility(View.GONE);
                        flagForInternet = false;
                    }else {

                    }
                }

                handler.postDelayed(runnable, 1000);
            }
        };
        runnable.run();

    }
    private void playVimeoPlayer() {
        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);
        video.start();
        Log.d("MainActivity","Video is playing " + video.isPlaying());
        video.isEnabled();
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
                Toast.makeText(PlayVideoAndRecordScreen.this, "Monet Video Finished.", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        });
        recordingStart();

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                rtmpCamera1.stopStream();
            }
        });
    }

    private void playYouTube() {
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                youTubePlayer.loadVideo(video_url);youTubePlayer.setPlayerStateChangeListener(PlayVideoAndRecordScreen.this);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
                Toast.makeText(PlayVideoAndRecordScreen.this, result.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        youTubePlayerView.initialize(YOUTUTBE_KEY, onInitializedListener);
    }

    private boolean networkIsAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        recordingStart();
    }

    @Override
    public void onVideoEnded() {
        rtmpCamera1.stopStream();
        Toast.makeText(this, "Youtube Video Ended Now", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Toast.makeText(this, "" + errorReason, Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(PlayVideoAndRecordScreen.this, MainActivity.class));
        finish();
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
                        Log.d("MainActivity","");
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
            String rtmpUrl = SdkConstant.RTMP_URL + "myStr";
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

                        checkNetworkType();
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

    private void checkNetworkType() {

        connectionStatus = isConnectedFast(getApplicationContext());
        if (!connectionStatus) {
            bitrate = "150";
            // connection is slow
            Log.d("Connection Status", "POOR CONNECTION");

        } else {
            // connection is fast
            Log.d("Connection Status", "GOOD CONNECTION");
            bitrate = "200";

        }
    }

    private boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype(), context));
    }

    private NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    private boolean isConnectionFast(int type, int subType, Context context) {
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
                    if(detecting){
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
}

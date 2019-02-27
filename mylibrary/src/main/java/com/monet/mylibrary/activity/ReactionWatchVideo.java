package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.ReactionIconsAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.listner.IOnItemClickListener;
import com.monet.mylibrary.model.reaction.ReactionPost;
import com.monet.mylibrary.model.reaction.ReactionResponse;
import com.monet.mylibrary.model.video.VideoPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;
import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getCamEval;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLength;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.getUserId;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.setQuestionType;
import static com.monet.mylibrary.utils.SdkUtils.convertVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.progressDialog;


public class ReactionWatchVideo extends AppCompatActivity {

    private ImageView img_toolbarBack;
    private RecyclerView recyclerView;
    private VideoView videoView;
    private ProgressBar progressBarVideo, pb_recactionVideo;
    private TextView tv_timeVideoView;
    private Handler handler;
    private Runnable runnable;
    boolean doubleBackToExitPressedOnce = false;
    private ApiInterface apiInterface;
    private ArrayList<String> iconNameList = new ArrayList<>();
    private ReactionIconsAdapter reactionIconsAdapter;
    private Dialog dialog;
    private EditText edt_dialogcoment;
    private TextView tv_dialogTitle;
    private ImageView img_dialog;
    private Button btn_dialogSubmit;
    private String dialog_name, coment, token, cf_id, user_id, cmp_id;
    private int videoStopTime = 0;
    boolean intrupt = false;

    private JSONArray jsonArray1 = new JSONArray();
    private JSONArray jsonArray2 = new JSONArray();
    private JSONArray jsonArray3 = new JSONArray();
    private JSONArray jsonArray4 = new JSONArray();
    private JSONArray jsonArray5 = new JSONArray();
    private JSONArray jsonArray6 = new JSONArray();
    private JSONArray jsonArray7 = new JSONArray();
    private JSONArray jsonArray8 = new JSONArray();
    private JSONArray jsonArray9 = new JSONArray();
    private JSONObject jsonObject;
    private JSONObject reactionMainObject = new JSONObject();

    private IOnItemClickListener iClickListener = new IOnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            dialog_name = iconNameList.get(position);
            showDialog();
            setDialogIcon(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_watch_video);

        img_toolbarBack = findViewById(R.id.img_toolbarBack);
        img_toolbarBack.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerViewReaction);
        videoView = findViewById(R.id.vv_reaction);
        progressBarVideo = findViewById(R.id.pb_reaction);
        tv_timeVideoView = findViewById(R.id.tv_timeVideoReaction);
        pb_recactionVideo = findViewById(R.id.pb_recactionVideo);
        progressBarVideo.setEnabled(false);
        dialog = new Dialog(ReactionWatchVideo.this);

        setReactionIcons();

        token = getApiToken(this);
        cf_id = getCfId(this);
        user_id = getUserId(this);
        cmp_id = getCmpId(this);

        try {
            stagingJson.put("5", "4");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        handler = new Handler();
        playVideo("https://r2---sn-ci5gup-qxar.googlevideo.com/videoplayback?lmt=1520252625302191&key=yt6&itag=43&beids=9466588&fvip=2&signature=515C5DF01F6D560FBD21731CA96008ED20448116.81EC927CA9C6905BCF03B8A23A3FB9A983E0F319&ratebypass=yes&c=WEB&clen=2063291&source=youtube&sparams=clen%2Cdur%2Cei%2Cgir%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cexpire&expire=1551272822&dur=0.000&initcwndbps=266250&ei=Fjd2XND5IIiqowOZ4YHIDA&requiressl=yes&ip=182.73.8.45&mv=m&mt=1551251151&ms=au%2Conr&mn=sn-ci5gup-qxar%2Csn-cvh7kney&gir=yes&mm=31%2C26&pl=23&id=o-ABi71PpoD84EtFJ3lWAHLwiKDW1mFWAvg0-mB4ikICPf&mime=video%2Fwebm&ipbits=0");
//        getVideoUrlFromLink(getVideoUrl(this));

    }

    private void getVideoUrlFromLink(String url) {
        progressDialog(this, "Please wait...", true);
        apiInterface.getMp4VideoUrl(url)
                .enqueue(new Callback<VideoPojo>() {
                    @Override
                    public void onResponse(Call<VideoPojo> call, Response<VideoPojo> response) {
                        progressDialog(ReactionWatchVideo.this, "Please wait...", false);
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
                            Toast.makeText(ReactionWatchVideo.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                progressBarVideo.setMax(videoView.getDuration());
                setSeekBar();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    stagingJson.put("5", "5");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                iconNameList.clear();
                dialog.dismiss();
                submitReactionPart();
            }
        });
    }

    private void setSeekBar() {
        progressBarVideo.setProgress(videoView.getCurrentPosition());
        if (videoView.isPlaying()) {
            pb_recactionVideo.setVisibility(View.GONE);
            runnable = new Runnable() {
                @Override
                public void run() {
                    tv_timeVideoView.setText(convertVideoTime((videoView.getDuration() - videoView.getCurrentPosition())));
                    setSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }else{
            if(!convertVideoTime(videoView.getCurrentPosition()).equalsIgnoreCase("00:00")){
                pb_recactionVideo.setVisibility(View.VISIBLE);
            }else{
                pb_recactionVideo.setVisibility(View.GONE);
            }
        }
    }

    private void setReactionIcons() {

        String cmpList = getCamEval(this);

        if (cmpList.contains("a") || cmpList.contains("1")) {
            iconNameList.add("Like");
        }
        if (cmpList.contains("b") || cmpList.contains("2")) {
            iconNameList.add("Love");
        }
        if (cmpList.contains("c") || cmpList.contains("3")) {
            iconNameList.add("Want");
        }
        if (cmpList.contains("d") || cmpList.contains("4")) {
            iconNameList.add("Memorable");
        }
        if (cmpList.contains("e") || cmpList.contains("5")) {
            iconNameList.add("Dislike");
        }
        if (cmpList.contains("f") || cmpList.contains("6")) {
            iconNameList.add("Accurate");
        }
        if (cmpList.contains("g") || cmpList.contains("7")) {
            iconNameList.add("Misleading");
        }
        if (cmpList.contains("h") || cmpList.contains("8")) {
            iconNameList.add("Engaging");
        }
        if (cmpList.contains("i") || cmpList.contains("9")) {
            iconNameList.add("Boring");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        reactionIconsAdapter = new ReactionIconsAdapter(this, iClickListener, iconNameList);
        recyclerView.setAdapter(reactionIconsAdapter);
    }

    @SuppressLint("NewApi")
    public void showDialog() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i)) &&
                            !Character.toString(source.charAt(i)).equals("_") &&
                            !Character.toString(source.charAt(i)).equals("-") &&
                            !Character.toString(source.charAt(i)).equals(" ")) {
                        return "";
                    }
                }
                return null;
            }
        };

        pauseVideo();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);
        dialog.getWindow().setLayout(width, height);
        edt_dialogcoment = dialog.findViewById(R.id.edt_dialogComment);
        img_dialog = dialog.findViewById(R.id.img_dialog);
        tv_dialogTitle = dialog.findViewById(R.id.tv_dialogTitle);
        btn_dialogSubmit = dialog.findViewById(R.id.btn_dialogSubmit);

        edt_dialogcoment.setFilters(new InputFilter[]{filter});

        setDialogHint();
        dialog.show();

        btn_dialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_dialogcoment.getText().toString().isEmpty()) {
                    Toast.makeText(ReactionWatchVideo.this, "Please enter your review", Toast.LENGTH_SHORT).show();
                } else {
                    coment = edt_dialogcoment.getText().toString();
                    dialog.dismiss();
                    try {
                        setReactionJson();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    videoStopTime = videoView.getCurrentPosition();
                    playVideoAgain();
                }
            }
        });
    }

    private void submitReactionPart() {
        if (reactionMainObject.length() == 0) {
            setScreen();
        } else {
            progressDialog(this, "Please wait...", true);
            ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
            ReactionPost reactionPost = new ReactionPost("" + reactionMainObject, cf_id, user_id, cmp_id);
            Log.d("send json object", "json" + reactionMainObject);
            Call<ReactionResponse> responseCall = apiInterface.submitReactionPart(token, reactionPost);
            responseCall.enqueue(new Callback<ReactionResponse>() {
                @Override
                public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                    progressDialog(ReactionWatchVideo.this, "Please wait...", false);
                    if (response.body() == null) {
                        Toast.makeText(ReactionWatchVideo.this, response.raw().message(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body().getCode().equals("200")) {
                            setScreen();
                        } else {
                            Toast.makeText(ReactionWatchVideo.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReactionResponse> call, Throwable t) {
                    progressDialog(ReactionWatchVideo.this, "Please wait...", false);
                    Toast.makeText(ReactionWatchVideo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void pauseVideo() {
        videoView.pause();
    }

    private void playVideoAgain() {
        videoView.seekTo(videoStopTime);
        videoView.start();
        setSeekBar();
    }

    private void setDialogIcon(int position) {

        if (iconNameList.get(position).equalsIgnoreCase("like")) {
            img_dialog.setImageResource(R.drawable.ic_like);
        }
        if (iconNameList.get(position).equalsIgnoreCase("love")) {
            img_dialog.setImageResource(R.drawable.ic_love);
        }
        if (iconNameList.get(position).equalsIgnoreCase("Want")) {
            img_dialog.setImageResource(R.drawable.ic_want);
        }
        if (iconNameList.get(position).equalsIgnoreCase("memorable")) {
            img_dialog.setImageResource(R.drawable.ic_memorable);
        }
        if (iconNameList.get(position).equalsIgnoreCase("dislike")) {
            img_dialog.setImageResource(R.drawable.ic_dislike);
        }
        if (iconNameList.get(position).equalsIgnoreCase("accurate")) {
            img_dialog.setImageResource(R.drawable.ic_helpful);
        }
        if (iconNameList.get(position).equalsIgnoreCase("misleading")) {
            img_dialog.setImageResource(R.drawable.ic_not_helpful);
        }
        if (iconNameList.get(position).equalsIgnoreCase("engaging")) {
            img_dialog.setImageResource(R.drawable.ic_surprise);
        }
        if (iconNameList.get(position).equalsIgnoreCase("boring")) {
            img_dialog.setImageResource(R.drawable.ic_boring);
        }
    }

    private void setDialogHint() {
        if (dialog_name.equalsIgnoreCase("like")) {
            tv_dialogTitle.setText("You like this part.... Why?");
        } else if (dialog_name.equalsIgnoreCase("love")) {
            tv_dialogTitle.setText("You love this part.... Why?");
        } else if (dialog_name.equalsIgnoreCase("Want")) {
            tv_dialogTitle.setText("You want this product..... Why?");
        } else if (dialog_name.equalsIgnoreCase("memorable")) {
            tv_dialogTitle.setText("This part was memorable to you.... Why?");
        } else if (dialog_name.equalsIgnoreCase("dislike")) {
            tv_dialogTitle.setText("You did not like this part.... Why?");
        } else if (dialog_name.equalsIgnoreCase("accurate")) {
            tv_dialogTitle.setText("This part helps you make a decision about whether or not you might like this product.... Why?");
        } else if (dialog_name.equalsIgnoreCase("misleading")) {
            tv_dialogTitle.setText("This part is misleading... Why?");
        } else if (dialog_name.equalsIgnoreCase("engaging")) {
            tv_dialogTitle.setText("This part specially captures your attention.... Why?");
        } else if (dialog_name.equalsIgnoreCase("boring")) {
            tv_dialogTitle.setText("This part is not interesting.... Why?");
        }

    }

    private void setReactionJson() throws JSONException {

        videoStopTime = videoView.getCurrentPosition();

        if (dialog_name.equalsIgnoreCase("Love")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray1.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray1);
        } else if (dialog_name.equalsIgnoreCase("Like")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray2.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray2);
        } else if (dialog_name.equalsIgnoreCase("Want to watch / buy")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray3.put(jsonObject);
            reactionMainObject.put("Want", jsonArray3);
        } else if (dialog_name.equalsIgnoreCase("Memorable")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray4.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray4);
        } else if (dialog_name.equalsIgnoreCase("Dislike")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray5.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray5);
        } else if (dialog_name.equalsIgnoreCase("Accurate")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray6.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray6);
        } else if (dialog_name.equalsIgnoreCase("Misleading")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray7.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray7);
        } else if (dialog_name.equalsIgnoreCase("Engaging")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray8.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray8);
        } else if (dialog_name.equalsIgnoreCase("Boring")) {
            jsonObject = new JSONObject();
            jsonObject.put("time", videoStopTime);
            jsonObject.put("coment", coment);
            jsonArray9.put(jsonObject);
            reactionMainObject.put(dialog_name, jsonArray9);
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (intrupt) {
            playVideoAgain();
            intrupt = false;
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        videoStopTime = videoView.getCurrentPosition();
        videoView.pause();
        intrupt = true;
    }
}

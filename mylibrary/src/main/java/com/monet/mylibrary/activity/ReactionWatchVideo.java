package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.IconsAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.listner.IOnItemClickListener;
import com.monet.mylibrary.model.Item;
import com.monet.mylibrary.model.reaction.ReactionPost;
import com.monet.mylibrary.model.reaction.ReactionResponse;
import com.monet.mylibrary.model.vimeoModel.VimeoPojo;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_A;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_B;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_C;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_D;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_E;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_F;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_G;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_H;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_I;


public class ReactionWatchVideo extends AppCompatActivity {

    private String video_id = "", token, cf_id, user_id, cmp_id, coment;
    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView icons_recycler;
    private ArrayList<Item> iconList = new ArrayList<>();
    private IconsAdapter iconsAdapter;
    private GridLayoutManager gridLayoutManager;
    private String dialog_name, dialog_image;
    private Dialog dialog;
    private ProgressDialog pd;
    private int videoStopTime = 0;
    private VideoView videoView;
    private ProgressBar reaction_progress;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean flagForInternet = false;

    private JSONObject reactionMainObject = new JSONObject();
    JSONArray jsonArray1 = new JSONArray();
    JSONArray jsonArray2 = new JSONArray();
    JSONArray jsonArray3 = new JSONArray();
    JSONArray jsonArray4 = new JSONArray();
    JSONArray jsonArray5 = new JSONArray();
    JSONArray jsonArray6 = new JSONArray();
    JSONArray jsonArray7 = new JSONArray();
    JSONArray jsonArray8 = new JSONArray();
    JSONArray jsonArray9 = new JSONArray();
    JSONObject jsonObject;

    IOnItemClickListener iOnItemClickListener = new IOnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            dialog_name = iconList.get(position).getReactionName();
            dialog_image = iconList.get(position).getImageUrl();
            videoStopTime = videoView.getCurrentPosition();
            showDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_watch_video);

        icons_recycler = findViewById(R.id.icons_recycler);
        videoView = findViewById(R.id.video_reaction);
        reaction_progress = findViewById(R.id.reaction_progress);

        dialog = new Dialog(ReactionWatchVideo.this, R.style.Theme_Dialog);
        video_id = SdkPreferences.getVideoUrl(this);

        token = SdkPreferences.getApiToken(this);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage("Loading...");

        if(video_id.contains("vimeo")){
            video_id = video_id.replace("https://vimeo.com/", "");
            getVimeoMPFour();
        }else{
            playVideo();
        }

        setReactionIcons();
    }

    private void getVimeoMPFour() {
        ApiInterface apiInterface = BaseUrl.getVimeoClient().create(ApiInterface.class);
        Call<VimeoPojo> pojoCall = apiInterface.getVideoDetails(video_id);

        pojoCall.enqueue(new Callback<VimeoPojo>() {
            @Override
            public void onResponse(Call<VimeoPojo> call, Response<VimeoPojo> response) {
                if (response.body() != null) {
                    int size = response.body().getVimeoReq().getVimeoFiles().getProgressives().size();
                    int i = 0;
                    for (; i < size; i++) {
                        if (response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getQuality().contains("480")) {
                            video_id = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getUrl();
                            break;
                        } else if (response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getQuality().contains("540")) {
                            video_id = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getUrl();
                            break;
                        } else if (response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getQuality().contains("720")) {
                            video_id = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(i).getUrl();
                            break;
                        } else if (i == size - 1) {
                            video_id = response.body().getVimeoReq().getVimeoFiles().getProgressives().get(0).getUrl();
                        }
                    }
                    playVideo();
                }
            }
            @Override
            public void onFailure(Call<VimeoPojo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playVideo() {
        Uri uri = Uri.parse(video_id);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Button Clicked...", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                reaction_progress.setVisibility(View.GONE);
                return true;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setProgressBar();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(getApplicationContext(), "video Finish", Toast.LENGTH_SHORT).show();
                reaction_progress.setVisibility(View.GONE);
                submitReactionPart();
            }
        });
    }

    private void submitReactionPart() {
        if (reactionMainObject.length() == 0) {
            setScreen();
        } else {
            pd.show();
            ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
            ReactionPost reactionPost = new ReactionPost("" + reactionMainObject, cf_id, user_id, cmp_id);
            Log.d("send json object", "json" + reactionMainObject);
            Call<ReactionResponse> responseCall = apiInterface.submitReactionPart(token, reactionPost);
            responseCall.enqueue(new Callback<ReactionResponse>() {
                @Override
                public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                    pd.dismiss();
                    if (response.body() == null) {
                        Toast.makeText(ReactionWatchVideo.this, response.raw().message(), Toast.LENGTH_SHORT).show();
                        playVideo();
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
                    pd.dismiss();
                    Toast.makeText(ReactionWatchVideo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // @SuppressLint("NewApi")
    private void setProgressBar() {

        runnable = new Runnable() {
            @Override
            public void run() {

                if (!networkIsAvailable()) {
                    reaction_progress.setVisibility(View.VISIBLE);
                    if (flagForInternet == false) {
                        Toast.makeText(getApplicationContext(), "Your internet connection is Interrupted", Toast.LENGTH_SHORT).show();
                    }
                    flagForInternet = true;
                } else {
                    if (videoView.isPlaying()) {
                        reaction_progress.setVisibility(View.GONE);
                        flagForInternet = false;
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

        videoView.pause();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView tv_dialogName;
        final EditText edt_dialogcoment;
        ImageView img_dialog;
        Button btn_dialog;

        tv_dialogName = dialog.findViewById(R.id.tv_dialogName);
        edt_dialogcoment = dialog.findViewById(R.id.edt_dialogComment);
        img_dialog = dialog.findViewById(R.id.img_dialog);
        btn_dialog = dialog.findViewById(R.id.btn_dialog);

        edt_dialogcoment.setFilters(new InputFilter[]{filter});

        tv_dialogName.setText(dialog_name);
        Glide.with(ReactionWatchVideo.this).load(dialog_image).into(img_dialog);

        setHint(edt_dialogcoment);
        dialog.show();

        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_dialogcoment.getText().toString().isEmpty()) {
                    Toast.makeText(ReactionWatchVideo.this, "Please enter your review", Toast.LENGTH_SHORT).show();
                } else {
                    coment = edt_dialogcoment.getText().toString();
                    dialog.dismiss();
                    videoView.start();
                    try {
                        setReactionJson();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void setHint(EditText editText) {

        if (dialog_name.equalsIgnoreCase("like")) {
            editText.setHint("You like this part.... Why?");
        } else if (dialog_name.equalsIgnoreCase("love")) {
            editText.setHint("You love this part.... Why?");
        } else if (dialog_name.equalsIgnoreCase("Want to watch / buy")) {
            editText.setHint("You want this product..... Why?");
        } else if (dialog_name.equalsIgnoreCase("memorable")) {
            editText.setHint("This part was memorable to you.... Why?");
        } else if (dialog_name.equalsIgnoreCase("dislike")) {
            editText.setHint("You did not like this part.... Why?");
        } else if (dialog_name.equalsIgnoreCase("accurate")) {
            editText.setHint("This part helps you make a decision about whether or not you might like this product.... Why?");
        } else if (dialog_name.equalsIgnoreCase("misleading")) {
            editText.setHint("This part is misleading... Why?");
        } else if (dialog_name.equalsIgnoreCase("engaging")) {
            editText.setHint("This part specially captures your attention.... Why?");
        } else if (dialog_name.equalsIgnoreCase("boring")) {
            editText.setHint("This part is not interesting.... Why?");
        }

    }

    private void setReactionJson() throws JSONException {

//        videoStopTime = (videoStopTime / 1000) % 60;

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
        } else if (dialog_name.equalsIgnoreCase("engaging")) {
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

    private void setReactionIcons() {

        String cmpList = SdkPreferences.getCamEval(this);

        if (cmpList.contains("a") || cmpList.contains("1")) {
            iconList.add(new Item("like", REACTION_A));
        }
        if (cmpList.contains("b") || cmpList.contains("2")) {
            iconList.add(new Item("love", REACTION_B));
        }
        if (cmpList.contains("c") || cmpList.contains("3")) {
            iconList.add(new Item("Want", REACTION_C));
        }
        if (cmpList.contains("d") || cmpList.contains("4")) {
            iconList.add(new Item("memorable", REACTION_D));
        }
        if (cmpList.contains("e") || cmpList.contains("5")) {
            iconList.add(new Item("dislike", REACTION_E));
        }
        if (cmpList.contains("f") || cmpList.contains("6")) {
            iconList.add(new Item("accurate", REACTION_F));
        }
        if (cmpList.contains("g") || cmpList.contains("7")) {
            iconList.add(new Item("misleading", REACTION_G));
        }
        if (cmpList.contains("h") || cmpList.contains("8")) {
            iconList.add(new Item("engaging", REACTION_H));
        }
        if (cmpList.contains("i") || cmpList.contains("9")) {
            iconList.add(new Item("boring", REACTION_I));
        }

        gridLayoutManager = new GridLayoutManager(this, 5);
        icons_recycler.setLayoutManager(gridLayoutManager);
        iconsAdapter = new IconsAdapter(iconList, this, iOnItemClickListener);
        icons_recycler.setAdapter(iconsAdapter);
    }
}

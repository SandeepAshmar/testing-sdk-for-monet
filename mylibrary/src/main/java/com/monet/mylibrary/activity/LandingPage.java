package com.monet.mylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.LandAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.YoutubePojo;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Pojo;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Response;
import com.monet.mylibrary.model.question.SdkPojo;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.QuestionActivity.postQuestions;
import static com.monet.mylibrary.activity.QuestionActivity.preQuestions;
import static com.monet.mylibrary.utils.SdkPreferences.clearAllPreferences;

public class LandingPage extends AppCompatActivity {

    private static final String TAG = "MonetAndroidSdk";
    private ImageView img_toolbarBack;
    private static TextView tv_land_watch;
    private static RecyclerView mRecycler;
    private static LandAdapter mAdapter;
    private static ArrayList<GetCampDetails_Response> detailsResponses = new ArrayList<>();
    protected static ArrayList<String> postQuestion = new ArrayList<>();
    private static Button btn_landProceed;
    private static CheckBox land_chack;
    private static String cmp_Id, user_Id, cf_id, apiToken;
    public static ArrayList<String> cmpSequance = new ArrayList<>();
    public static ArrayList<String> arrayList = new ArrayList<String>();
    private static String videoUrl = "";
    public static JSONObject stagingJson = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mRecycler = findViewById(R.id.recyler_land);
        tv_land_watch = findViewById(R.id.tv_land_watch);
        btn_landProceed = findViewById(R.id.btn_landAgree);
        land_chack = findViewById(R.id.land_chack);
        img_toolbarBack = findViewById(R.id.img_toolbarBack);

        mAdapter = new LandAdapter(LandingPage.this, detailsResponses);
        mRecycler.setAdapter(mAdapter);

        arrayList.clear();

        img_toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_toolbarBack.setClickable(false);
                onBackPressed();
            }
        });

        land_chack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (land_chack.isChecked()) {
                    btn_landProceed.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_landProceed.setEnabled(true);
                } else {
                    btn_landProceed.setBackgroundResource(R.drawable.btn_pro_gray);
                    btn_landProceed.setEnabled(false);
                }
            }
        });

        btn_landProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_landProceed.setClickable(false);
                if(videoUrl.contains("youtube")){
                    getVideoDetails();
                }else{
                    setScreen();
                }
            }
        });
    }

    public static void startCampaign(Activity activity, String cmpId, String userId) {
        activity.startActivity(new Intent(activity, LandingPage.class));
        detailsResponses.clear();
        clearAllPreferences(activity);
        preQuestions.clear();
        postQuestion.clear();
        arrayList.clear();
        cmp_Id = cmpId;
        user_Id = userId;
        getCmpFlow(activity, cmpId);
    }

    private void getVideoDetails() {
        ApiInterface apiInterface = BaseUrl.getYoutubeRetrofit().create(ApiInterface.class);
        Call<List<YoutubePojo>> listCall = apiInterface.getYoutubeUrl("?url=" + videoUrl);
        listCall.enqueue(new Callback<List<YoutubePojo>>() {
            @Override
            public void onResponse(Call<List<YoutubePojo>> call, Response<List<YoutubePojo>> response) {
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().get(0).getReason() == null) {
                        int size = response.body().size();
                        for (int i = 0; i < size; i++) {
                            if (response.body().get(i).getQuality().contains("720")) {
                                videoUrl = response.body().get(i).getUrl();
                            } else if (i == size - 1) {
                                videoUrl = response.body().get(0).getUrl();
                            }
                        }
                        if (videoUrl.contains("=us")) {
                            Toast.makeText(getApplicationContext(), "Sorry, This video can't play in your country", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            SdkPreferences.setVideoUrl(getApplicationContext(), videoUrl);
                            setScreen();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().get(0).getReason(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<YoutubePojo>> call, Throwable t) {
                Toast.makeText(LandingPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void getCmpFlow(final Activity activity, final String cmpId) {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<SdkPojo> pojoCall = apiInterface.getSdk(cmp_Id, user_Id);
        pojoCall.enqueue(new Callback<SdkPojo>() {
            @Override
            public void onResponse(Call<SdkPojo> call, Response<SdkPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(activity, response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getSequence().size() == 0) {
                            Toast.makeText(activity, "No Campaign flow is found", Toast.LENGTH_SHORT).show();
                        } else {
                            saveDetails(activity ,response);
                        }
                    } else {
                        Toast.makeText(activity, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SdkPojo> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void saveDetails(Activity activity, Response<SdkPojo> response){
        SdkPreferences.setCmpId(activity, cmp_Id);
        SdkPreferences.setUserId(activity, user_Id);
        SdkPreferences.setCfId(activity, response.body().getCf_id());
        SdkPreferences.setApiToken(activity, "Bearer " + response.body().getApi_token());
        preQuestions.addAll(response.body().getPre().getQuestions());
        postQuestions.addAll(response.body().getPost().getQuestions());
        arrayList.addAll(response.body().getSequence());
        SdkPreferences.setCmpLength(activity, 1);
        SdkPreferences.setCamEval(activity, response.body().getCmp_eval());
        SdkPreferences.setVideoUrl(activity, response.body().getC_url());
        videoUrl = response.body().getC_url();
        apiToken = SdkPreferences.getApiToken(activity);
        getCampDetails(activity, apiToken, cmp_Id);
    }

    private static void getCampDetails(final Activity activity, String token, final String cmpId) {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<GetCampDetails_Pojo> pojoCall = apiInterface.getCampDetails(token, cmpId);
        pojoCall.enqueue(new Callback<GetCampDetails_Pojo>() {
            @Override
            public void onResponse(Call<GetCampDetails_Pojo> call, Response<GetCampDetails_Pojo> response) {
                if (response.body() == null) {
                    Toast.makeText(activity.getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getResponse().size() <= 1) {
                            mRecycler.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 1));
                            tv_land_watch.setText("watch " + response.body().getResponse().size() + " short clip");
                        } else {
                            mRecycler.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 2));
                            tv_land_watch.setText("watch " + response.body().getResponse().size() + " short clips");
                        }
                        detailsResponses.addAll(response.body().getResponse());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCampDetails_Pojo> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

package com.monet.mylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.LandAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Pojo;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Response;
import com.monet.mylibrary.model.question.SdkPojo;
import com.monet.mylibrary.utils.SdkPreferences;
import com.monet.mylibrary.utils.SdkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    public static ArrayList<String> arrayList = new ArrayList<String>();
    public static JSONObject stagingJson = new JSONObject();
    private static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mRecycler = findViewById(R.id.recyler_land);
        tv_land_watch = findViewById(R.id.tv_land_watch);
        btn_landProceed = findViewById(R.id.btn_landAgree);
        land_chack = findViewById(R.id.land_chack);
        img_toolbarBack = findViewById(R.id.img_toolbarBack);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LandAdapter(LandingPage.this, detailsResponses);
        mRecycler.setAdapter(mAdapter);
        apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        arrayList.clear();

        img_toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_toolbarBack.setClickable(false);
                onBackPressed();
            }
        });
        btn_landProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (land_chack.isChecked()) {
                    setScreen();
                } else {
                    Toast.makeText(LandingPage.this, "Please check terms and conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static void checkEmotionScreen(Activity activity) {
        activity.startActivity(new Intent(activity, EmotionScreen.class));
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
        getCmpFlow(activity);
    }

    private static void getCmpFlow(final Activity activity) {
        SdkUtils.progressDialog(activity, "Please wait...", true);
        Call<SdkPojo> pojoCall = apiInterface.getSdk(cmp_Id, user_Id);
        pojoCall.enqueue(new Callback<SdkPojo>() {
            @Override
            public void onResponse(Call<SdkPojo> call, Response<SdkPojo> response) {
                SdkUtils.progressDialog(activity, "Please wait...", false);
                if (response.body() == null) {
                    Toast.makeText(activity, response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getData().getSequence().size() == 0) {
                            Toast.makeText(activity, "No Campaign flow is found", Toast.LENGTH_SHORT).show();
                        } else {
                            saveDetails(activity, response);
                        }
                    } else {
                        Toast.makeText(activity, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SdkPojo> call, Throwable t) {
                SdkUtils.progressDialog(activity, "Please wait...", false);
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void saveDetails(Activity activity, Response<SdkPojo> response) {
        SdkPreferences.setCmpId(activity, cmp_Id);
        SdkPreferences.setUserId(activity, user_Id);
        SdkPreferences.setCfId(activity, response.body().getData().getCf_id());
        SdkPreferences.setApiToken(activity, "Bearer " + response.body().getData().getApi_token());
        preQuestions.addAll(response.body().getData().getPre().getQuestions());
        postQuestions.addAll(response.body().getData().getPost().getQuestions());
        arrayList.addAll(response.body().getData().getSequence());
        SdkPreferences.setCmpLength(activity, 1);
        SdkPreferences.setCamEval(activity, response.body().getData().getCmp_eval());
        SdkPreferences.setVideoUrl(activity, response.body().getData().getC_url());
        apiToken = SdkPreferences.getApiToken(activity);
        getCampDetails(activity, apiToken, cmp_Id);
    }

    private static void getCampDetails(final Activity activity, String token, final String cmpId) {
        SdkUtils.progressDialog(activity, "Please wait...", true);
        Call<GetCampDetails_Pojo> pojoCall = apiInterface.getCampDetails(token, cmpId);
        pojoCall.enqueue(new Callback<GetCampDetails_Pojo>() {
            @Override
            public void onResponse(Call<GetCampDetails_Pojo> call, Response<GetCampDetails_Pojo> response) {
                SdkUtils.progressDialog(activity, "Please wait...", false);
                if (response.body() == null) {
                    Toast.makeText(activity.getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getResponse().size() <= 1) {
                            tv_land_watch.setText("In the following study you will\n watch " + response.body().getResponse().size() + " short clip");
                        } else {
                            tv_land_watch.append("In the following study you will\n watch " + response.body().getResponse().size() + " short clips");
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
                SdkUtils.progressDialog(activity, "Please wait...", false);
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

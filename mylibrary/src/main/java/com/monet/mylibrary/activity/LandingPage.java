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
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.listner.CheckSuccessResponse;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Response;
import com.monet.mylibrary.model.sdk.PostQuestions;
import com.monet.mylibrary.model.sdk.PreQuestions;
import com.monet.mylibrary.model.sdk.SdkPojo;
import com.monet.mylibrary.utils.SdkPreferences;
import com.monet.mylibrary.utils.SdkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLength;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpName;
import static com.monet.mylibrary.utils.SdkPreferences.getThumbUrl;
import static com.monet.mylibrary.utils.SdkPreferences.getVideoTime;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpName;
import static com.monet.mylibrary.utils.SdkPreferences.setQuestionType;
import static com.monet.mylibrary.utils.SdkPreferences.setThumbUrl;
import static com.monet.mylibrary.utils.SdkPreferences.setVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.convertVideoTime;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

public class LandingPage extends AppCompatActivity {

    private static ImageView img_toolbarBack;
    private static ImageView img_currentShows;
    private static TextView tv_land_watch, tv_landCam, tv_vid_landTime;
    private static ArrayList<GetCampDetails_Response> detailsResponses = new ArrayList<>();
    public static ArrayList<PreQuestions> preQuestions = new ArrayList<>();
    public static ArrayList<PostQuestions> postQuestions = new ArrayList<>();
    private static Button btn_landProceed, btn_currentShows;
    private static CheckBox land_chack;
    private static String cmp_Id, user_Id, apiToken, client_license;
    public static ArrayList<String> arrayList = new ArrayList<String>();
    public static JSONObject stagingJson = new JSONObject();
    private static Activity landingActivity;
    private CheckSuccessResponse checkSuccessResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        tv_land_watch = findViewById(R.id.tv_land_watch);
        btn_landProceed = findViewById(R.id.btn_landAgree);
        land_chack = findViewById(R.id.land_chack);
        img_toolbarBack = findViewById(R.id.img_toolbarBack);
        img_currentShows = findViewById(R.id.img_currentShows);
        tv_landCam = findViewById(R.id.tv_nameCurrentShows);
        tv_vid_landTime = findViewById(R.id.tv_videoTimeCurrentShows);
        btn_currentShows = findViewById(R.id.btn_currentShows);
        try {
            stagingJson.put("0", "3");
            stagingJson.put("1", "1");
            stagingJson.put("2", "0");
            stagingJson.put("3", "0");
            stagingJson.put("4", "0");
            stagingJson.put("5", "0");
            stagingJson.put("6", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Picasso.with(this).load(getThumbUrl(this))
                .into(img_currentShows);
        tv_landCam.setText(getCmpName(this));
        tv_vid_landTime.setText(getVideoTime(this));

        String test = getCmpName(this);
        test = String.valueOf(test.charAt(0));
        btn_currentShows.setText(test);


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
                if (arrayList.size() != 0) {
                    if (land_chack.isChecked()) {
                        try {
                            stagingJson.put("1", "2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setScreen();
                        finish();
                    } else {
                        Toast.makeText(LandingPage.this, "Please check terms and conditions", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LandingPage.this, "There is something error, Please try again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void startCampaign(Activity activity, String cmpId, String userId, String license) {
        landingActivity = activity;
        activity.startActivity(new Intent(activity, LandingPage.class));
        detailsResponses.clear();
        preQuestions.clear();
        postQuestions.clear();
        arrayList.clear();
        cmp_Id = cmpId;
        user_Id = userId;
        client_license = license;
        getCmpFlow(activity);
    }

    public void getSuccessResponse(CheckSuccessResponse checkSuccessResponse, Activity activity, String cmpId, String userId, String license) {
//        activity.startActivity(new Intent(activity, LandingPage.class));
        this.checkSuccessResponse = checkSuccessResponse;
        detailsResponses.clear();
        preQuestions.clear();
        postQuestions.clear();
        arrayList.clear();
        cmp_Id = cmpId;
        user_Id = userId;
        client_license = license;
        getCmpFlow(activity);
    }

    private void getCmpFlow(final Activity activity) {
        SdkUtils.progressDialog(activity, "Please wait...", true);
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<SdkPojo> pojoCall = apiInterface.getSdk(cmp_Id, user_Id);
        pojoCall.enqueue(new Callback<SdkPojo>() {
            @Override
            public void onResponse(Call<SdkPojo> call, Response<SdkPojo> response) {
                SdkUtils.progressDialog(activity, "Please wait...", false);
                if (response.body() == null) {
                    checkSuccessResponse.onSDKResponse(false, response.raw().message());
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getData().getSequence() == null) {
                            checkSuccessResponse.onSDKResponse(false, response.body().getMessage());
                        } else {
                            activity.startActivity(new Intent(activity, LandingPage.class));
                            checkSuccessResponse.onSDKResponse(true, response.body().getMessage());
                            saveDetails(activity, response);
                        }
                    } else {
                        checkSuccessResponse.onSDKResponse(false, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SdkPojo> call, Throwable t) {
                SdkUtils.progressDialog(activity, "Please wait...", false);
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                checkSuccessResponse.onSDKResponse(false, t.getMessage());
            }
        });

    }

    private void saveDetails(Activity activity, Response<SdkPojo> response) {
        SdkPreferences.setCmpId(activity, cmp_Id);
        SdkPreferences.setUserId(activity, user_Id);
        SdkPreferences.setCfId(activity, response.body().getData().getUser_ex_id());
        SdkPreferences.setApiToken(activity, "Bearer " + response.body().getData().getToken());
        if (response.body().getData().getPre() != null) {
            preQuestions.addAll(response.body().getData().getPre().getQuestions());
            for (int i = 0; i < preQuestions.size(); i++) {
                if(preQuestions.get(i).getQuestionType().equals("grid")){
                    for (int j = 0; j < preQuestions.get(i).getOptions().size(); j++) {
                        preQuestions.get(i).getOptions().get(j).setValues(preQuestions.get(i).getValues());
                    }
                }
            }
        }
        if (response.body().getData().getPost() != null) {
            postQuestions.addAll(response.body().getData().getPost().getQuestions());
            for (int i = 0; i < postQuestions.size(); i++) {
                if(postQuestions.get(i).getQuestionType().equals("grid")){
                    for (int j = 0; j < postQuestions.get(i).getOptions().size(); j++) {
                        postQuestions.get(i).getOptions().get(j).setValues(postQuestions.get(i).getValues());
                    }
                }
            }
        }
        arrayList.addAll(response.body().getData().getSequence());
        SdkPreferences.setCmpLength(activity, 1);
        SdkPreferences.setCmpLengthCount(activity, 0);
        SdkPreferences.setCamEval(activity, response.body().getData().getReaction_inputs());
        SdkPreferences.setVideoUrl(activity, response.body().getData().getContent_url());
        apiToken = getApiToken(activity);
        setThumbUrl(activity, response.body().getData().getThumb_url());
        setCmpName(activity, response.body().getData().getCmp_name());
        String time = convertVideoTime(response.body().getData().getContent_length());
        setVideoTime(activity, time);

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        try {
            stagingJson.put("1", "3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendStagingData(landingActivity);
        finish();
        super.onBackPressed();
    }
}

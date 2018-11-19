package com.monet.mylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.LandAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Pojo;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Response;
import com.monet.mylibrary.model.question.PostQuestions;
import com.monet.mylibrary.model.question.PreQuestions;
import com.monet.mylibrary.model.question.SdkPojo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPage extends AppCompatActivity {

    private static final String TAG = "MonetAndroidSdk";
    private static TextView tv_land_watch;
    private static RecyclerView mRecycler;
    private static LandAdapter mAdapter;
    private static ArrayList<GetCampDetails_Response> detailsResponses = new ArrayList<>();
    private static PostQuestions postQuestion = new PostQuestions();
    private static PreQuestions preQuestions = new PreQuestions();
    private static Button btn_landExit, btn_landProceed;
    private static CheckBox land_chack;
    private static String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mRecycler = findViewById(R.id.recyler_land);
        tv_land_watch = findViewById(R.id.tv_land_watch);
        btn_landExit = findViewById(R.id.btn_landExit);
        btn_landProceed = findViewById(R.id.btn_landProceed);
        land_chack = findViewById(R.id.land_chack);

        mAdapter = new LandAdapter(this, detailsResponses);
        mRecycler.setAdapter(mAdapter);

        btn_landExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(new Intent(LandingPage.this, QuestionActivity.class));
            }
        });
    }

    public static void startCampaign(Activity activity, String userId, String cmpId) {
        activity.startActivity(new Intent(activity, LandingPage.class));
        detailsResponses.clear();
        preQuestions.getQuestion().clear();
        postQuestion.getQuestion().clear();
        getCmpFlow(activity, cmpId, userId);
    }

    private static void getCmpFlow(final Activity activity, final String cmpId, String userId) {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<SdkPojo> pojoCall = apiInterface.getSdk(cmpId, userId);
        pojoCall.enqueue(new Callback<SdkPojo>() {
            @Override
            public void onResponse(Call<SdkPojo> call, Response<SdkPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(activity, response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getSequence().size() == 0) {
                            Toast.makeText(activity, "No Campaign flow is found", Toast.LENGTH_SHORT).show();
                            activity.onBackPressed();
                        } else {
                            for (int i = 0; i < response.body().getPre().getQuestions().size(); i++) {
                                preQuestions.setQuestion(response.body().getPre().getQuestions().get(i).getQuestion());
                            }
                            for (int i = 0; i < response.body().getPost().getQuestions().size(); i++) {
                                postQuestion.setQuestion(response.body().getPost().getQuestions().get(i).getQuestion());
                            }
                            token = "Bearer "+ response.body().getApi_token();
                            getCampDetails(activity, token, cmpId);
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
}

package com.monet.mylibrary.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.monet.mylibrary.model.question.QuestionModelPostGrid;
import com.monet.mylibrary.model.question.QuestionModelPostOption;
import com.monet.mylibrary.model.question.QuestionModelPojo;
import com.monet.mylibrary.model.question.QuestionModelPostQuestions;
import com.monet.mylibrary.model.question.QuestionModelPre;
import com.monet.mylibrary.model.question.QuestionModelPreGrid;
import com.monet.mylibrary.model.question.QuestionModelPreOption;
import com.monet.mylibrary.model.question.QuestionModelPreQuestions;

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
    private static ArrayList<QuestionModelPostQuestions> postQuestions ;
    private static ArrayList<QuestionModelPostOption> postOptions ;
    private static ArrayList<QuestionModelPostGrid> postGrid ;
    private static ArrayList<QuestionModelPreQuestions> preQuestions;
    private static ArrayList<QuestionModelPreOption> preOptions ;
    private static ArrayList<QuestionModelPreGrid> preGrid;
    private static ProgressDialog pd;
    private static Button btn_landExit, btn_landProceed;
    private static CheckBox land_chack;

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

        preQuestions = new ArrayList<QuestionModelPreQuestions>();
        preOptions = new ArrayList<QuestionModelPreOption>();
        preGrid = new ArrayList<QuestionModelPreGrid>();

        postQuestions = new ArrayList<QuestionModelPostQuestions>();
        postOptions = new ArrayList<QuestionModelPostOption>();
        postGrid = new ArrayList<QuestionModelPostGrid>();

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

            }
        });
    }

    private static void getCampDetails(final Activity activity, String token, final String cmpId) {
        pd.show();
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<GetCampDetails_Pojo> pojoCall = apiInterface.getCampDetails(token, cmpId);
        pojoCall.enqueue(new Callback<GetCampDetails_Pojo>() {
            @Override
            public void onResponse(Call<GetCampDetails_Pojo> call, Response<GetCampDetails_Pojo> response) {
                pd.dismiss();
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
                pd.dismiss();
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startCampaign(Activity activity, String userId, String cmpId) {
        activity.startActivity(new Intent(activity, LandingPage.class));
        pd = new ProgressDialog(activity);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading...");
        if (detailsResponses.size() != 0) {
            detailsResponses.clear();
        }
        getCmpFlow(activity, cmpId, userId);
    }

    private static void getCmpFlow(final Activity activity, final String cmpId, String userId) {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<QuestionModelPojo> pojoCall = apiInterface.getSdk(cmpId, userId);
        pojoCall.enqueue(new Callback<QuestionModelPojo>() {
            @Override
            public void onResponse(Call<QuestionModelPojo> call, Response<QuestionModelPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(activity, response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getSequence().size() == 0) {
                            Toast.makeText(activity, "No Campaign flow is found", Toast.LENGTH_SHORT).show();
                        } else {
                            preQuestions.addAll(response.body().getPre().getQuestions());
                        }
                    } else {
                        Toast.makeText(activity, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<QuestionModelPojo> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

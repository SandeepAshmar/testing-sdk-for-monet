package com.monet.mylibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.monet.mylibrary.R;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.getCampignFlow.GetCampFlowPojo;
import com.monet.mylibrary.model.survay.SurvayPojo;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.getlicence_key;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;


public class ThankyouPage extends AppCompatActivity {

    private Button btn_cam_proceed;
    private TextView progress_text, tv_cam_first, tv_proceedText;
    private ImageView img_landCheck, img_toolbarBack;
    private Handler handler = new Handler();
    private int pStatus = 0;
    private int setMinValue = 0;
    private int setMaxValue = 0;
    private DonutProgress donutProgress;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_page);

        donutProgress = findViewById(R.id.donut_progress_lodging);
        progress_text = findViewById(R.id.progress_text);
        tv_cam_first = findViewById(R.id.tv_cam_first);
        tv_proceedText = findViewById(R.id.tv_proceedText);
        btn_cam_proceed = findViewById(R.id.btn_cam_proceed);
        img_landCheck = findViewById(R.id.img_landCheck);
        img_toolbarBack = findViewById(R.id.img_toolbarBack);

        img_toolbarBack.setVisibility(View.GONE);
        setDetails();

        btn_cam_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendFinalStagingData(3);
                    setMaxValue = 0;
                    setMinValue = 0;
                    pStatus = 0;

                    sendStagingData(ThankyouPage.this, 3);
                    finish();
            }
        });
    }

    public void sendFinalStagingData(int success) {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<SurvayPojo> pojoCall = apiInterface.updatePageStage(getApiToken(this),getPageStage(this), success);

        Log.d("TAG", "sendStagingData: " + getPageStage(this) + " success:- " + success);

        pojoCall.enqueue(new Callback<SurvayPojo>() {
            @Override
            public void onResponse(Call<SurvayPojo> call, Response<SurvayPojo> response) {
                if (response.body() == null) {
                    Log.d("TAG", "staging error: " + response.raw().message());
                } else {
                    if (response.body().getCode().equalsIgnoreCase("200")) {
                        Log.d("TAG", "staging success: " + response.body().getMessage());
                    } else {
                        Log.d("TAG", "staging code no match: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SurvayPojo> call, Throwable t) {
                Log.d("TAG", "staging through: " + t.getMessage());
            }
        });
    }

    private void setDetails() {

        if (setMaxValue == 0) {
            setMaxValue = SdkPreferences.getCmpLength(this);
        }
        setMinValue = SdkPreferences.getCmpLengthCountFlag(this);

        if (setMaxValue == 1) {
            SdkPreferences.setCmpLength(this, 0);
            SdkPreferences.setCmpLengthCountFlag(this, 0);
            SdkPreferences.setCmpLengthCount(this, 0);
        } else {
            if (setMinValue == 1) {
                tv_cam_first.setText(R.string.complete_first);
                tv_proceedText.setText(R.string.proceed_first);
                tv_proceedText.setTextSize(16);
                tv_cam_first.setTextSize(16);
                btn_cam_proceed.setText("Proceed");
            } else if (setMinValue == 2) {
                tv_cam_first.setText(R.string.complete_secound);
                tv_proceedText.setText(R.string.proceed_secound);
                tv_proceedText.setTextSize(16);
                tv_cam_first.setTextSize(16);
                btn_cam_proceed.setText("Proceed");
            } else if (setMinValue == 3) {
                tv_cam_first.setText(R.string.complete_third);
                tv_proceedText.setText(R.string.proceed_third);
                tv_proceedText.setTextSize(16);
                tv_cam_first.setTextSize(16);
                btn_cam_proceed.setText("Proceed");
            }
        }

        donutProgress.setText("");
        donutProgress.setMax(setMaxValue);

        runSeekBar();

        if (setMinValue == setMaxValue) {
            tv_cam_first.setText(R.string.complete_congrats);
            progress_text.setVisibility(View.GONE);
            img_landCheck.setVisibility(View.VISIBLE);
            tv_proceedText.setText(R.string.proceed_forth);
            tv_proceedText.setTextSize(16);
            tv_cam_first.setTextSize(16);
            btn_cam_proceed.setText("Finish");
        }
    }

    public void runSeekBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus < setMinValue) {
                    pStatus += 1;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            donutProgress.setDonut_progress("" + pStatus);
                            progress_text.setText(pStatus + "/" + setMaxValue);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        sendStagingData(this,0);
        finish();
        super.onBackPressed();
    }
}

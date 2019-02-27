package com.monet.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.monet.mylibrary.R;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.getCampignFlow.GetCampFlowPojo;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;


public class ThankyouPage extends AppCompatActivity {

    private Button btn_cam_proceed;
    private TextView progress_text, tv_cam_first, tv_proceedText;
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

        setDetails();

        btn_cam_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_cam_proceed.getText().equals("Finish")) {
                    try {
                        stagingJson.put("6", "2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setMaxValue = 0;
                    setMinValue = 0;
                    pStatus = 0;

                    sendStagingData(ThankyouPage.this);
                    finish();
                } else {
                    setScreen();
                }
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

    public void setScreen() {
        int count = Integer.valueOf(SdkPreferences.getCmpLength(this));
        int i = SdkPreferences.getCmpLengthCount(this);

        if (count == 1) {
            setDetails();
            runSeekBar();
            SdkPreferences.setCmpLength(this, 0);
            SdkPreferences.setCmpLengthCountFlag(this, 0);
            SdkPreferences.setCmpLengthCount(this, 0);
        } else {

            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    SdkPreferences.setQuestionType(this, "pre");
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    SdkPreferences.setQuestionType(this, "post");
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {

                int flag = SdkPreferences.getCmpLengthCountFlag(this);
                SdkPreferences.setCmpLengthCountFlag(this, flag + 1);
                getData();
            }

        }

    }

    private void getData() {
        String cmp_id = SdkPreferences.getCmpId(this);
        token = SdkPreferences.getApiToken(this);
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<GetCampFlowPojo> pojoCall = apiInterface.getCampFlow(token, cmp_id);
        pojoCall.enqueue(new Callback<GetCampFlowPojo>() {
            @Override
            public void onResponse(Call<GetCampFlowPojo> call, Response<GetCampFlowPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(ThankyouPage.this, response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        SdkPreferences.setCmpLength(ThankyouPage.this, setMaxValue);
                        SdkPreferences.setCmpLengthCountFlag(ThankyouPage.this, setMinValue);
                        arrayList.clear();
                        arrayList.addAll(response.body().getResponse().getCmp_sequence());
                        SdkPreferences.setCamEval(ThankyouPage.this, response.body().getResponse().getCmp_eval());

                        if (arrayList.get(0).equalsIgnoreCase("Pre")) {
                            SdkPreferences.setQuestionType(ThankyouPage.this, "pre");
                            startActivity(new Intent(ThankyouPage.this, QuestionActivity.class));
                            finish();
                        } else if (arrayList.get(0).equalsIgnoreCase("Post")) {
                            SdkPreferences.setQuestionType(ThankyouPage.this, "post");
                            startActivity(new Intent(ThankyouPage.this, QuestionActivity.class));
                            finish();
                        } else if (arrayList.get(0).equalsIgnoreCase("Emotion")) {
                            startActivity(new Intent(ThankyouPage.this, EmotionScreen.class));
                            finish();
                        } else if (arrayList.get(0).equalsIgnoreCase("Reaction")) {
                            startActivity(new Intent(ThankyouPage.this, ReactionScreen.class));
                            finish();
                        }

                    } else {
                        Toast.makeText(ThankyouPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCampFlowPojo> call, Throwable t) {
                Toast.makeText(ThankyouPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        sendStagingData(this);
        finish();
        super.onBackPressed();
    }
}

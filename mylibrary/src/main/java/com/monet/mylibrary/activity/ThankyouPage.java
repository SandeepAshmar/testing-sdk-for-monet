package com.monet.mylibrary.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.monet.mylibrary.R;
import com.monet.mylibrary.utils.SdkPreferences;

import androidx.appcompat.app.AppCompatActivity;

import static com.monet.mylibrary.activity.LandingPage.sequenceList;
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
                    setMaxValue = 0;
                    setMinValue = 0;
                    pStatus = 0;

                    if(sequenceList.size() == 4){
                        sendStagingData(ThankyouPage.this, 3);
                    }else if(sequenceList.contains("emotion")){
                        sendStagingData(ThankyouPage.this, 2);
                    }else {
                        sendStagingData(ThankyouPage.this, 1);
                    }

                    finish();
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

package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.RadioTypeAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkPojo;
import com.monet.mylibrary.model.question.SdkQuestions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_quesQCard;
    private LinearLayout ll_quesQCardBtn, ll_quesNextBtn, ll_question, grid_linearLayout;
    private Button btn_quesNext, btn_quesqa_exit, btn_quesqa_proceed;
    private RecyclerView rv_question;
    private TextView tv_questionNo, tv_question, tv_questionSelect;
    private EditText edt_questionType;
    private int questionNo = 0;
    private String cmp_Id, user_Id;
    private ArrayList<SdkQuestions> questions = new ArrayList<>();
    private RadioTypeAdapter radioTypeAdapter;
    private LinearLayoutManager radioLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        rl_quesQCard = findViewById(R.id.rl_quesQCard);
        ll_quesQCardBtn = findViewById(R.id.ll_quesQCardBtn);
        ll_quesNextBtn = findViewById(R.id.ll_quesNextBtn);
        ll_question = findViewById(R.id.ll_question);
        grid_linearLayout = findViewById(R.id.grid_linearLayout);
        btn_quesNext = findViewById(R.id.btn_quesNext);
        btn_quesqa_proceed = findViewById(R.id.btn_quesqa_proceed);
        rv_question = findViewById(R.id.rv_question);
        tv_questionNo = findViewById(R.id.tv_questionNo);
        tv_question = findViewById(R.id.tv_question);
        tv_questionSelect = findViewById(R.id.tv_questionSelect);
        edt_questionType = findViewById(R.id.edt_questionType);
        btn_quesqa_exit = findViewById(R.id.btn_quesqa_exit);
        btn_quesNext.setEnabled(false);

        btn_quesqa_proceed.setOnClickListener(this);
        btn_quesqa_exit.setOnClickListener(this);
        btn_quesNext.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        cmp_Id = extras.getString("cmpId");
        user_Id = extras.getString("userId");

        radioLayoutManager = new LinearLayoutManager(this);
        rv_question.setLayoutManager(radioLayoutManager);
        rv_question.setAdapter(radioTypeAdapter);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_quesqa_exit) {
            onBackPressed();
        } else if (i == R.id.btn_quesqa_proceed) {
            ll_quesNextBtn.setVisibility(View.VISIBLE);
            ll_quesQCardBtn.setVisibility(View.GONE);
            rl_quesQCard.setVisibility(View.GONE);
            ll_question.setVisibility(View.VISIBLE);
            getCmpFlow();
        }
    }

    private void getCmpFlow() {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<SdkPojo> pojoCall = apiInterface.getSdk(cmp_Id, user_Id);
        pojoCall.enqueue(new Callback<SdkPojo>() {
            @Override
            public void onResponse(Call<SdkPojo> call, Response<SdkPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getSequence().size() == 0) {
                            Toast.makeText(getApplicationContext(), "No Campaign flow is found", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            questions.addAll(response.body().getPre().getQuestions());
                            setQuestions(response);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SdkPojo> call, Throwable t) {

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setQuestions(final Response<SdkPojo> response) {
        RadioClickListner radioClickListner = new RadioClickListner() {
            @Override
            public void onItemClick(View view, int position, String quesId, String ansId) {
                Toast.makeText(QuestionActivity.this, questions.get(0).getOptions().get(position).getOption_value(), Toast.LENGTH_SHORT).show();
            }
        };

        radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, questions.get(0).getOptions(), radioClickListner);

        tv_question.setText(response.body().getPre().getQuestions().get(0).getQuestion());
        tv_questionNo.setText("Q" + (questionNo + 1) + ".");
    }


}
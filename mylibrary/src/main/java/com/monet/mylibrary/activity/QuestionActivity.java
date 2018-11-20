package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private int questionNo = 0, questionSize;
    private String cmp_Id, user_Id;
    private ArrayList<SdkQuestions> questions = new ArrayList<>();
    private RadioTypeAdapter radioTypeAdapter;
    private LinearLayoutManager radioLayoutManager;
    private JSONObject dataPostJson1 = new JSONObject();
    private JSONObject quesJson = new JSONObject();
    private JSONObject quesJsonGrid = new JSONObject();
    private JSONArray jsonArray2 = new JSONArray();
    private String selectedQuesId, selectedAnsId;

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
        if (i == R.id.btn_quesNext) {
            nextQuestion();
        }
    }

    private void getCmpFlow() {
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<SdkPojo> pojoCall = apiInterface.getSdk(cmp_Id, user_Id);
        pojoCall.enqueue(new Callback<SdkPojo>() {
            @Override
            public void onResponse(Call<SdkPojo> call, Response<SdkPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(getApplication().getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {
                        if (response.body().getSequence().size() == 0) {
                            Toast.makeText(getApplicationContext(), "No Campaign flow is found", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            questions.addAll(response.body().getPre().getQuestions());
                            questionSize = response.body().getPre().getQuestions().size();
                            SdkPreferences.setCmpLengthCount(QuestionActivity.this, response.body().getSequence().size());
                            setQuestions();
                        }
                    } else {
                        Toast.makeText(getApplication().getApplicationContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SdkPojo> call, Throwable t) {

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setQuestions() {
        RadioClickListner radioClickListner = new RadioClickListner() {
            @Override
            public void onItemClick(View view, int position, String quesId, String ansId) {
                Toast.makeText(getApplication().getApplicationContext(), questions.get(questionNo).getOptions().get(position).getOption_value(), Toast.LENGTH_SHORT).show();
                btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
                btn_quesNext.setEnabled(true);
                selectedQuesId = quesId;
                selectedAnsId = ansId;
            }
        };

        radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, questions.get(questionNo).getOptions(), radioClickListner);
        rv_question.setAdapter(radioTypeAdapter);

        if (questionNo == 0) {
            tv_questionNo.setText("Q1.");
        } else {
            tv_questionNo.setText("Q" + (questionNo + 1) + ".");
        }
        tv_question.setText(questions.get(questionNo).getQuestion());
        tv_questionSelect.setText("Please select one option");
    }

    private void nextQuestion() {
        questionNo = (questionNo + 1);

        if (questionNo < questionSize) {
            setQuestions();
            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_gray);
            btn_quesNext.setEnabled(false);

            try {
                setAnsJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            questionNo = (questionNo - 1);
            Toast.makeText(this, "Questions Complete", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnsJson() throws JSONException {
        dataPostJson1 = new JSONObject();
        quesJson.put(selectedQuesId, dataPostJson1);
        dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
        dataPostJson1.put("type", "1");

        Log.d("json", "json = "+dataPostJson1);

//        if (questions.get(questionNo).getQuestion_type().equalsIgnoreCase("1")) {
//            dataPostJson1 = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
//            dataPostJson1.put("type", "1");
//        } else if (questions.get(questionNo).getQuestion_type().equalsIgnoreCase("2")) {
//            dataPostJson1 = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("selectedOptions", jsonArray2);
//            for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
//                jsonArray2.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
//            }
//            dataPostJson1.put("type", "2");
//        } else if (questions.get(questionNo).getQuestion_type().equalsIgnoreCase("3")) {
//            dataPostJson1 = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
//            dataPostJson1.put("type", "3");
//        } else if (questions.get(questionNo).getQuestion_type().equalsIgnoreCase("4")) {
//            dataPostJson1 = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("options", edt_questionType.getText().toString());
//            dataPostJson1.put("type", "4");
//        } else if (questions.get(questionNo).getQuestion_type().equalsIgnoreCase("5")) {
//            dataPostJson1 = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("id", selectedAnsId);
//            dataPostJson1.put("text", typeFiveReason);
//            dataPostJson1.put("type", "5");
//        } else if (questions.get(questionNo).getQuestion_type().equalsIgnoreCase("6")) {
//            dataPostJson1 = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("options", quesJsonGrid);
//            for (int i = 0; i < savedQuesAndAnswers.getGridAnsIds().size(); i++) {
//                quesJsonGrid.put(savedQuesAndAnswers.getGridOptionIds().get(i), savedQuesAndAnswers.getGridAnsIds().get(i));
//            }
//            dataPostJson1.put("type", "6");
//        }
    }

}
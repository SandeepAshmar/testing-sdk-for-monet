package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.model.question.PostQuestions;
import com.monet.mylibrary.model.question.PreQuestions;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_quesQCard;
    private LinearLayout ll_quesQCardBtn, ll_quesNextBtn, ll_question, grid_linearLayout;
    private Button btn_quesNext, btn_quesqa_exit, btn_quesqa_proceed;
    private RecyclerView rv_question;
    private TextView tv_questionNo, tv_question, tv_questionSelect;
    private EditText edt_questionType;
    private PreQuestions preQuestions = new PreQuestions();
    private PostQuestions postQuestions = new PostQuestions();
    private int questionNo = 0;

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
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_quesqa_exit) {
            onBackPressed();
        }else if(i == R.id.btn_quesqa_proceed){
            ll_quesNextBtn.setVisibility(View.VISIBLE);
            ll_quesQCardBtn.setVisibility(View.GONE);
            rl_quesQCard.setVisibility(View.GONE);
            ll_question.setVisibility(View.VISIBLE);
            setQuestions();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setQuestions() {

        tv_question.setText(preQuestions.getQuestion().get(questionNo));
        tv_questionNo.setText("Q" + (questionNo + 1) + ".");

    }
}

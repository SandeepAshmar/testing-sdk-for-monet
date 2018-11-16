package com.monet.mylibrary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.monet.mylibrary.R;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_quesQCard;
    private LinearLayout ll_quesQCardBtn, ll_quesNextBtn;
    private Button btn_quesNext, btn_quesqa_exit, btn_quesqa_proceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        rl_quesQCard = findViewById(R.id.rl_quesQCard);
        ll_quesQCardBtn = findViewById(R.id.ll_quesQCardBtn);
        ll_quesNextBtn = findViewById(R.id.ll_quesNextBtn);
        btn_quesNext = findViewById(R.id.btn_quesNext);
        btn_quesqa_proceed = findViewById(R.id.btn_quesqa_proceed);
        btn_quesqa_exit = findViewById(R.id.btn_quesqa_exit);
        btn_quesNext.setEnabled(false);

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

        }
    }
}

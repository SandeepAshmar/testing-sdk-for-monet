package com.monet.mylibrary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.model.question.SdkPostQuestions;
import com.monet.mylibrary.model.question.SdkPreQuestions;
import com.monet.mylibrary.utils.SdkPreferences;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout cl_quesQCard, cl_questionLayout;
    private ImageView back;
    private Button btn_question;
    private RecyclerView rv_question;
    private TextView tv_questionNo, tv_question;
    private EditText edt_questionType;
    private boolean qCardVisible = true;
    public static int questionSize;
    public static String quesType = "";
    public static ArrayList<SdkPreQuestions> preQuestions = new ArrayList<>();
    public static ArrayList<SdkPostQuestions> postQuestions = new ArrayList<>();
    private int questionNo = 0;
    public static String token, type, cmp_id, cf_id, qusId, selectedAnsId, selectedQuesId, questionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        cl_quesQCard = findViewById(R.id.cl_questionQCard);
        cl_questionLayout = findViewById(R.id.cl_questionLayout);
        btn_question = findViewById(R.id.btn_question);
        rv_question = findViewById(R.id.rv_question);
        rv_question.setLayoutManager(new LinearLayoutManager(this));
        tv_questionNo = findViewById(R.id.tv_questionNo);
        tv_question = findViewById(R.id.tv_question);
        edt_questionType = findViewById(R.id.edt_questionType);
        back = findViewById(R.id.img_toolbarBack);

        btn_question.setOnClickListener(this);
        back.setOnClickListener(this);

        quesType = SdkPreferences.getQuestionType(this);

        if (quesType.equalsIgnoreCase("Pre")) {
            questionSize = preQuestions.size();
        } else {
            questionSize = postQuestions.size();
            Toast.makeText(this, "Post", Toast.LENGTH_SHORT).show();
        }
        setQuestion();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_toolbarBack) {
            finish();
        } else if (i == R.id.btn_question) {
            if (qCardVisible) {
                cl_quesQCard.setVisibility(View.GONE);
                cl_questionLayout.setVisibility(View.VISIBLE);
                qCardVisible = false;
            } else {
                Toast.makeText(this, "question", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setQuestion() {

        if (!qCardVisible) {
            if (questionNo == (questionSize - 1)) {
                btn_question.setText("submit");
            } else {
                btn_question.setText("Next");
            }
        }

        if (questionNo == 0) {
            tv_questionNo.setText("Q1.");
        } else {
            tv_questionNo.setText("Q" + (questionNo + 1) + ".");
        }

        if (quesType.equalsIgnoreCase("Pre")) {
            tv_question.setText(preQuestions.get(questionNo).getQuestion());
            qusId = preQuestions.get(questionNo).getQuestion_id();

            if (preQuestions.get(questionNo).getQuestion_type().equals("1")) {

            }
        } else {
            tv_question.setText(postQuestions.get(questionNo).getQuestion());
            qusId = postQuestions.get(questionNo).getQuestion_id();
        }


    }
}
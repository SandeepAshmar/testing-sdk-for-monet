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
import androidx.recyclerview.widget.RecyclerView;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout cl_quesQCard, cl_questionLayout;
    private ImageView back;
    private Button btn_question;
    private RecyclerView rv_question;
    private TextView tv_questionNo, tv_question;
    private EditText edt_questionType;
    public static int questionSize;
    public static String quesType = "";
    public static ArrayList<SdkPreQuestions> preQuestions = new ArrayList<>();
    public static ArrayList<SdkPostQuestions> postQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        cl_quesQCard = findViewById(R.id.cl_questionLayout);
        cl_questionLayout = findViewById(R.id.cl_questionLayout);
        btn_question = findViewById(R.id.btn_question);
        rv_question = findViewById(R.id.rv_question);
        tv_questionNo = findViewById(R.id.tv_questionNo);
        tv_question = findViewById(R.id.tv_question);
        edt_questionType = findViewById(R.id.edt_questionType);
        back = findViewById(R.id.img_toolbarBack);

        btn_question.setOnClickListener(this);
        back.setOnClickListener(this);

        quesType = SdkPreferences.getQuestionType(this);

        if (quesType.equalsIgnoreCase("Pre")) {
            questionSize = preQuestions.size();
            Toast.makeText(this, "Pre", Toast.LENGTH_SHORT).show();
        } else {
            questionSize = postQuestions.size();
            Toast.makeText(this, "Post", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_toolbarBack) {
            finish();
        } else if (i == R.id.btn_question) {
            if(cl_quesQCard.getVisibility() == View.VISIBLE){
                cl_quesQCard.setVisibility(View.GONE);
                cl_questionLayout.setVisibility(View.VISIBLE);
                btn_question.setText("Next");
            }else{
            }

        }
    }
}
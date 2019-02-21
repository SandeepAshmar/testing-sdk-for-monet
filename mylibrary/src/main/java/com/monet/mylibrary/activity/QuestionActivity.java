package com.monet.mylibrary.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.CheckBoxTypeAdapter;
import com.monet.mylibrary.adapter.RadioTypeAdapter;
import com.monet.mylibrary.listner.CheckBoxClickListner;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkPostQuestions;
import com.monet.mylibrary.model.question.SdkPreQuestions;
import com.monet.mylibrary.utils.AnswerSavedClass;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpId;

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
    public static String token, type, cmp_id, cf_id, qusId, selectedAnsId, selectedQuesId, questionType, typeFiveReason = "";
    public static AnswerSavedClass savedQuesAndAnswers = new AnswerSavedClass();
    private JSONObject dataPostJson1 = new JSONObject();
    private JSONObject quesJson = new JSONObject();
    private JSONObject quesJsonGrid = new JSONObject();
    private JSONArray jsonArray2 = new JSONArray();
    private RadioTypeAdapter radioTypeAdapter;
    private CheckBoxTypeAdapter checkBoxTypeAdapter;

    RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String quesId, String ansId) {
            selectedQuesId = quesId;
            selectedAnsId = ansId;

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getRadioQuesIds().size() == 0 || savedQuesAndAnswers.getRadioAnsIds().size() == 0) {
                savedQuesAndAnswers.setRadioQuesIds(quesId);
                savedQuesAndAnswers.setRadioAnsIds(ansId);
            } else {
                if (savedQuesAndAnswers.getRadioQuesIds().contains(quesId)) {
                    int pos = savedQuesAndAnswers.getRadioQuesIds().indexOf(quesId);
                    savedQuesAndAnswers.getRadioAnsIds().set(pos, ansId);
                } else {
                    savedQuesAndAnswers.setRadioQuesIds(quesId);
                    savedQuesAndAnswers.setRadioAnsIds(ansId);
                }
            }

            if (questionType.equalsIgnoreCase("5")) {
                if (quesType.equalsIgnoreCase("pre")) {
                    String yes = preQuestions.get(questionNo).getOptions().get(position).getOption_value();
                    if (yes.equalsIgnoreCase("Yes") || yes.equalsIgnoreCase("Yes ")) {
//                        showDialog();
                    } else {
                        typeFiveReason = "";
                    }
                } else {
                    String yes = postQuestions.get(questionNo).getOptions().get(position).getOption_value();
                    if (yes.equalsIgnoreCase("Yes") || yes.equalsIgnoreCase("Yes ")) {
//                        showDialog();
                    } else {
                        typeFiveReason = "";
                    }
                }

            }
        }
    };

    private CheckBoxClickListner checkBoxClickListner = new CheckBoxClickListner() {
        @Override
        public void onItemCheckBoxClick(View view, int position, String quesId, String ansId) {

            selectedQuesId = quesId;

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getCheckQuesId().size() == 0 || savedQuesAndAnswers.getCheckAnsId().size() == 0) {
                savedQuesAndAnswers.setCheckQuesId(quesId);
                savedQuesAndAnswers.setCheckAnsId(ansId);
            } else {
                if (savedQuesAndAnswers.getCheckAnsId().contains(ansId)) {
                    int pos = savedQuesAndAnswers.getCheckAnsId().indexOf(ansId);
                    savedQuesAndAnswers.getCheckAnsId().remove(pos);
                } else {
                    savedQuesAndAnswers.setCheckAnsId(ansId);
                }
            }
        }
    };

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

        token = getApiToken(this);
        cmp_id = getCmpId(this);
        cf_id = getCfId(this);

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
            setPreviousQuestion();
        } else if (i == R.id.btn_question) {
            if (qCardVisible) {
                cl_quesQCard.setVisibility(View.GONE);
                cl_questionLayout.setVisibility(View.VISIBLE);
                qCardVisible = false;
            } else {
                nextQuestion();
            }
        }
    }

    private void setQuestion() {

        if (!qCardVisible) {
            if (questionNo == (questionSize - 1)) {
                btn_question.setText("Submit");
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
                rv_question.setVisibility(View.VISIBLE);
                edt_questionType.setVisibility(View.GONE);
                questionType = "1";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("2")) {
                rv_question.setVisibility(View.VISIBLE);
                edt_questionType.setVisibility(View.GONE);
                questionType = "2";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(this, checkBoxClickListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            }
        } else {
            tv_question.setText(postQuestions.get(questionNo).getQuestion());
            qusId = postQuestions.get(questionNo).getQuestion_id();

        }
    }

    private void nextQuestion() {
        try {
            setAnsJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questionNo = (questionNo + 1);
        if (questionNo < questionSize) {
            setQuestion();
        } else {
            questionNo = (questionNo - 1);
        }
    }

    private void setPreviousQuestion() {
        questionNo = (questionNo - 1);

        if (questionNo == -1) {
            clearValues();
            finish();
        } else {
            setQuestion();
        }
    }

    private void setAnsJson() throws JSONException {

        if (quesType.equalsIgnoreCase("Pre")) {
            if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("1")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "1");
            } else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("2")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray2);
                for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
                    jsonArray2.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
                }
                dataPostJson1.put("type", "2");
            } else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("3")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "3");
            } else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("4")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", edt_questionType.getText().toString());
                dataPostJson1.put("type", "4");
            } else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("5")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("id", selectedAnsId);
                dataPostJson1.put("text", typeFiveReason);
                dataPostJson1.put("type", "5");
            }
//        else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("6")) {
//            dataPostJson1 = new JSONObject();
//            quesJsonGrid = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("options", quesJsonGrid);
//            for (int i = 0; i < savedQuesAndAnswers.getGridAnsIds().size(); i++) {
//                quesJsonGrid.put(savedQuesAndAnswers.getGridOptionIds().get(i), savedQuesAndAnswers.getGridAnsIds().get(i));
//            }
//            dataPostJson1.put("type", "6");
//        }
        } else {
            if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("1")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "1");
            } else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("2")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray2);
                for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
                    jsonArray2.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
                }
                dataPostJson1.put("type", "2");
            } else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("3")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "3");
            } else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("4")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", edt_questionType.getText().toString());
                dataPostJson1.put("type", "4");
            } else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("5")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("id", selectedAnsId);
                dataPostJson1.put("text", typeFiveReason);
                dataPostJson1.put("type", "5");
            }
//        else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("6")) {
//            dataPostJson1 = new JSONObject();
//            quesJsonGrid = new JSONObject();
//            quesJson.put(selectedQuesId, dataPostJson1);
//            dataPostJson1.put("options", quesJsonGrid);
//            for (int i = 0; i < savedQuesAndAnswers.getGridAnsIds().size(); i++) {
//                quesJsonGrid.put(savedQuesAndAnswers.getGridOptionIds().get(i), savedQuesAndAnswers.getGridAnsIds().get(i));
//            }
//            dataPostJson1.put("type", "6");
//        }
        }

        Log.d("json", "json " + quesJson);
    }

    @Override
    public void onBackPressed() {
        setPreviousQuestion();
    }

    private void clearValues() {
        dataPostJson1 = null;
        quesJson = null;
        jsonArray2 = new JSONArray();
        savedQuesAndAnswers.getCheckAnsId().clear();
        savedQuesAndAnswers.getCheckQuesId().clear();
        savedQuesAndAnswers.getGridAnsIds().clear();
        savedQuesAndAnswers.getGridOptionIds().clear();
        savedQuesAndAnswers.getGridQuesIds().clear();
        savedQuesAndAnswers.getRadioAnsIds().clear();
        savedQuesAndAnswers.getRadioQuesIds().clear();
        questionNo = 0;
        questionSize = 0;

        if (quesType.equalsIgnoreCase("Pre")) {
            preQuestions.clear();
        } else {
            postQuestions.clear();
        }
    }
}
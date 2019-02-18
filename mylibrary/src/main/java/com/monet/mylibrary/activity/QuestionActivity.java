package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.CheckBoxTypeAdapter;
import com.monet.mylibrary.adapter.RadioTypeAdapter;
import com.monet.mylibrary.adapter.RateAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.listner.CheckBoxClickListner;
import com.monet.mylibrary.listner.IOnItemClickListener;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkPostQuestion;
import com.monet.mylibrary.model.question.SdkPreQuestion;
import com.monet.mylibrary.model.survay.SurvayPojo;
import com.monet.mylibrary.model.survay.SurvayPost;
import com.monet.mylibrary.utils.AnswerSavedClass;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_quesQCard;
    private LinearLayout ll_quesQCardBtn, ll_quesNextBtn, ll_question, grid_linearLayout;
    private Button btn_quesqa_exit, btn_quesqa_proceed;
    public static Button btn_quesNext;
    private RecyclerView rv_question;
    private TextView tv_questionNo, tv_question, tv_questionSelect;
    private EditText edt_questionType;
    private int questionNo = 0, header = 0, child = 0;
    public static int questionSize;
    private String typeFiveReason = "";
    public static String quesType = "";
    public static ArrayList<SdkPreQuestion> preQuestions = new ArrayList<>();
    public static ArrayList<SdkPostQuestion> postQuestions = new ArrayList<>();
    private RadioTypeAdapter radioTypeAdapter;
    private CheckBoxTypeAdapter checkBoxTypeAdapter;
    private RateAdapter rateAdapter;
    private LinearLayoutManager radioLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private JSONObject dataPostJson1 = new JSONObject();
    private JSONObject quesJson = new JSONObject();
    private JSONObject quesJsonGrid = new JSONObject();
    private JSONArray jsonArray2 = new JSONArray();
    private String selectedQuesId, selectedAnsId, questionType;
    private RelativeLayout rate_layout;
    private LinearLayout grid_Linear_Layout;
    private int radioType;
    private Dialog dialog;
    public static AnswerSavedClass savedQuesAndAnswers = new AnswerSavedClass();
    private ArrayList<String> selectedGridOptions = new ArrayList<>();
    private boolean flagAdapter = true;
    private ProgressBar quesProgress;

    RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String quesId, String ansId) {
            selectedQuesId = quesId;
            selectedAnsId = ansId;

            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_quesNext.setEnabled(true);

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
                        showDialog();
                    } else {
                        typeFiveReason = "";
                    }
                } else {
                    String yes = postQuestions.get(questionNo).getOptions().get(position).getOption_value();
                    if (yes.equalsIgnoreCase("Yes") || yes.equalsIgnoreCase("Yes ")) {
                        showDialog();
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

            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_quesNext.setEnabled(true);
        }
    };

    private IOnItemClickListener rateItemClick = new IOnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_quesNext.setEnabled(true);

            selectedAnsId = String.valueOf(position + 1);

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getRateQuesId().size() == 0) {
                savedQuesAndAnswers.setRateQuesId(selectedQuesId);
                savedQuesAndAnswers.setRateAnsId(selectedAnsId);
            } else {
                if (savedQuesAndAnswers.getRateQuesId().contains(selectedQuesId)) {
                    int pos = savedQuesAndAnswers.getRateQuesId().indexOf(selectedQuesId);
                    savedQuesAndAnswers.getRateAnsId().set(pos, selectedAnsId);
                } else {
                    savedQuesAndAnswers.setRateQuesId(selectedQuesId);
                    savedQuesAndAnswers.setRateAnsId(selectedAnsId);
                }
            }
        }
    };

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

        rate_layout = findViewById(R.id.rate_layout);
        grid_Linear_Layout = findViewById(R.id.grid_linearLayout);
        quesProgress = findViewById(R.id.quesProgress);

        btn_quesqa_proceed.setOnClickListener(this);
        btn_quesqa_exit.setOnClickListener(this);
        btn_quesNext.setOnClickListener(this);

        dialog = new Dialog(QuestionActivity.this, R.style.Theme_Dialog);
        gridLayoutManager = new GridLayoutManager(this, 5);
        radioLayoutManager = new LinearLayoutManager(this);
        rv_question.setLayoutManager(radioLayoutManager);

        quesType = SdkPreferences.getQuestionType(this);

        if (quesType.equalsIgnoreCase("Pre")) {
            questionSize = preQuestions.size();
        } else {
            questionSize = postQuestions.size();
        }
        setQuestions();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_quesqa_exit) {
            finish();
        } else if (i == R.id.btn_quesqa_proceed) {
            ll_quesNextBtn.setVisibility(View.VISIBLE);
            ll_quesQCardBtn.setVisibility(View.GONE);
            rl_quesQCard.setVisibility(View.GONE);
            ll_question.setVisibility(View.VISIBLE);
        }
        if (i == R.id.btn_quesNext) {
            nextQuestion();
        }
    }

    private void setQuestions() {

        if (quesType.equalsIgnoreCase("Pre")) {
            if (questionNo == (questionSize - 1)) {
                btn_quesNext.setText("Finish");
            } else {
                btn_quesNext.setText("Next");
            }

            if (questionNo == 0) {
                tv_questionNo.setText("Q1.");
            } else {
                tv_questionNo.setText("Q" + (questionNo + 1) + ".");
            }

            tv_question.setText(preQuestions.get(questionNo).getQuestion());

            if (preQuestions.get(questionNo).getQuestion_type().equals("1")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.GONE);
                questionType = "1";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                tv_questionSelect.setText("Please select 1 answer");
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("2")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                tv_questionSelect.setText("Please Select all that apply");
                questionType = "2";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(this, checkBoxClickListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);

            } else if (preQuestions.get(questionNo).getQuestion_type().equals("3")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.VISIBLE);
                edt_questionType.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                questionType = "3";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                radioType = Integer.parseInt(preQuestions.get(questionNo).getRadio_type());
                rv_question.setLayoutManager(gridLayoutManager);
                rateAdapter = new RateAdapter(this, radioType, rateItemClick, selectedQuesId);
                rv_question.setAdapter(rateAdapter);
                tv_questionSelect.setText("Please rate on a scale of 1-" + radioType + " with " + radioType + " being the strongest likely");
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("4")) {
                tv_questionSelect.setText("Please type your answer");
                rv_question.setVisibility(View.GONE);
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rate_layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.VISIBLE);
                grid_Linear_Layout.setVisibility(View.GONE);
                questionType = "4";
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("5")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                tv_questionSelect.setText("Please select 1 answer");
                questionType = "5";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                radioTypeAdapter = new RadioTypeAdapter(this, preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("6")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.VISIBLE);
                edt_questionType.setVisibility(View.GONE);
                tv_questionSelect.setText("Grid Choice");
                questionType = "6";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                if (flagAdapter) {
                    createGridAdapter();
                    flagAdapter = false;
                }
            }
        } else {
            if (questionNo == (questionSize - 1)) {
                btn_quesNext.setText("Finish");
            } else {
                btn_quesNext.setText("Next");
            }

            if (questionNo == 0) {
                tv_questionNo.setText("Q1.");
            } else {
                tv_questionNo.setText("Q" + (questionNo + 1) + ".");
            }

            tv_question.setText(postQuestions.get(questionNo).getQuestion());

            if (postQuestions.get(questionNo).getQuestion_type().equals("1")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.GONE);
                questionType = "1";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                tv_questionSelect.setText("Please select 1 answer");
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, postQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("2")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                tv_questionSelect.setText("Please Select all that apply");
                questionType = "2";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(this, checkBoxClickListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);

            } else if (postQuestions.get(questionNo).getQuestion_type().equals("3")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.VISIBLE);
                edt_questionType.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                questionType = "3";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                radioType = Integer.parseInt(postQuestions.get(questionNo).getRadio_type());
                rv_question.setLayoutManager(gridLayoutManager);
                rateAdapter = new RateAdapter(this, radioType, rateItemClick, selectedQuesId);
                rv_question.setAdapter(rateAdapter);
                tv_questionSelect.setText("Please rate on a scale of 1-" + radioType + " with " + radioType + " being the strongest likely");
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("4")) {
                tv_questionSelect.setText("Please type your answer");
                rv_question.setVisibility(View.GONE);
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rate_layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.VISIBLE);
                grid_Linear_Layout.setVisibility(View.GONE);
                questionType = "4";
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("5")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                edt_questionType.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.GONE);
                tv_questionSelect.setText("Please select 1 answer");
                questionType = "5";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                radioTypeAdapter = new RadioTypeAdapter(this, postQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("6")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.GONE);
                grid_Linear_Layout.setVisibility(View.VISIBLE);
                edt_questionType.setVisibility(View.GONE);
                tv_questionSelect.setText("Grid Choice");
                questionType = "6";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                grid_Linear_Layout.removeAllViewsInLayout();
                selectedGridOptions.clear();
                createGridAdapter();
            }
        }
    }

    private void createGridAdapter() {
        if (quesType.equalsIgnoreCase("Pre")) {
            header = preQuestions.get(questionNo).getOptions().size();
            child = preQuestions.get(questionNo).getOptions().get(0).getGrid().size();
            selectedQuesId = preQuestions.get(questionNo).getQuestion_id();

            RadioButton radioButton = null;
            RadioGroup radioGroup = null;

            for (int i = 0; i < header; i++) {
                TextView option_Header = new TextView(getApplicationContext());
                radioGroup = new RadioGroup(getApplicationContext());
                radioGroup.setPadding(2, 5, 2, 20);
                option_Header = new TextView(getApplicationContext());
                radioGroup = new RadioGroup(getApplicationContext());
                radioGroup.setVisibility(View.VISIBLE);
                option_Header.setText(preQuestions.get(questionNo).getOptions().get(i).getOption_value());
                option_Header.setTextSize(16);
                option_Header.setTypeface(null, Typeface.BOLD);
                option_Header.setPadding(10, 20, 10, 20);
                option_Header.setId(Integer.parseInt(preQuestions.get(questionNo).getOptions().get(i).getOption_id()));
                option_Header.setTextColor(Color.BLACK);

                for (int j = 0; j < child; j++) {
                    radioButton = new RadioButton(getApplicationContext());
                    radioButton.setText(preQuestions.get(questionNo).getOptions().get(i).getGrid().get(j).getGrid_value());
                    radioButton.setId(Integer.parseInt(preQuestions.get(questionNo).getOptions().get(i).getGrid().get(j).getGrid_id()));
                    radioGroup.addView(radioButton);
                    radioButton.setId(Integer.parseInt(preQuestions.get(questionNo).getOptions().get(i).getGrid().get(j).getGrid_id()));
                }
                grid_Linear_Layout.addView(option_Header);
                grid_Linear_Layout.addView(radioGroup);
                final RadioGroup finalRadioGroup = radioGroup;
                final TextView finalOption_Header = option_Header;

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        String optionId = String.valueOf(finalOption_Header.getId());
                        String gridOptionId = String.valueOf(finalRadioGroup.getCheckedRadioButtonId());

                        try {
                            quesJson.put(selectedQuesId, dataPostJson1);
                            dataPostJson1.put("options", quesJsonGrid);
                            quesJsonGrid.put(optionId, gridOptionId);
                            dataPostJson1.put("type", "6");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.d("json", "json "+quesJson);

                        selectedGridOptions.add(String.valueOf(finalOption_Header.getId()));

                        if (selectedGridOptions.size() == header) {
                            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
                            btn_quesNext.setEnabled(true);
                        }
                    }
                });
            }
        } else {
            header = postQuestions.get(questionNo).getOptions().size();
            child = postQuestions.get(questionNo).getOptions().get(0).getGrid().size();
            selectedQuesId = postQuestions.get(questionNo).getQuestion_id();

            RadioButton radioButton = null;
            RadioGroup radioGroup = null;

            for (int i = 0; i < header; i++) {
                TextView option_Header = new TextView(getApplicationContext());
                radioGroup = new RadioGroup(getApplicationContext());
                radioGroup.setPadding(2, 5, 2, 20);
                option_Header = new TextView(getApplicationContext());
                radioGroup = new RadioGroup(getApplicationContext());
                radioGroup.setVisibility(View.VISIBLE);
                option_Header.setText(postQuestions.get(questionNo).getOptions().get(i).getOption_value());
                option_Header.setTextSize(16);
                option_Header.setTypeface(null, Typeface.BOLD);
                option_Header.setPadding(10, 20, 10, 20);
                option_Header.setId(Integer.parseInt(postQuestions.get(questionNo).getOptions().get(i).getOption_id()));
                option_Header.setTextColor(Color.BLACK);

                for (int j = 0; j < child; j++) {
                    radioButton = new RadioButton(getApplicationContext());
                    radioButton.setText(postQuestions.get(questionNo).getOptions().get(i).getGrid().get(j).getGrid_value());
                    radioButton.setId(Integer.parseInt(postQuestions.get(questionNo).getOptions().get(i).getGrid().get(j).getGrid_id()));
                    radioGroup.addView(radioButton);
                    radioButton.setId(Integer.parseInt(postQuestions.get(questionNo).getOptions().get(i).getGrid().get(j).getGrid_id()));
                }
                grid_Linear_Layout.addView(option_Header);
                grid_Linear_Layout.addView(radioGroup);
                final RadioGroup finalRadioGroup = radioGroup;
                final TextView finalOption_Header = option_Header;

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        String optionId = String.valueOf(finalOption_Header.getId());
                        String gridOptionId = String.valueOf(finalRadioGroup.getCheckedRadioButtonId());

                        if (savedQuesAndAnswers == null || savedQuesAndAnswers.getGridQuesIds().size() == 0 || savedQuesAndAnswers.getGridOptionIds().size() == 0
                                || savedQuesAndAnswers.getGridAnsIds().size() == 0) {
                            savedQuesAndAnswers.setGridQuesIds(selectedQuesId);
                            savedQuesAndAnswers.setGridOptionIds(String.valueOf(finalOption_Header.getId()));
                            savedQuesAndAnswers.setGridAnsIds(String.valueOf(finalRadioGroup.getCheckedRadioButtonId()));
                        } else {
                            if (savedQuesAndAnswers.getGridQuesIds().contains(selectedAnsId)) {
                                if (savedQuesAndAnswers.getGridOptionIds().contains(optionId)) {
                                    int optPos = savedQuesAndAnswers.getGridOptionIds().indexOf(optionId);
                                    savedQuesAndAnswers.getGridAnsIds().set(optPos, gridOptionId);
                                } else {
                                    savedQuesAndAnswers.setGridOptionIds(String.valueOf(finalOption_Header.getId()));
                                    savedQuesAndAnswers.setGridAnsIds(String.valueOf(finalRadioGroup.getCheckedRadioButtonId()));
                                }
                            } else {
                                savedQuesAndAnswers.setGridQuesIds(selectedQuesId);
                                savedQuesAndAnswers.setGridOptionIds(String.valueOf(finalOption_Header.getId()));
                                savedQuesAndAnswers.setGridAnsIds(String.valueOf(finalRadioGroup.getCheckedRadioButtonId()));
                            }
                        }

                        selectedGridOptions.add(String.valueOf(finalOption_Header.getId()));

                        if (selectedGridOptions.size() == header) {
                            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
                            btn_quesNext.setEnabled(true);
                        }
                    }
                });
            }
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
            setQuestions();
            jsonArray2 = new JSONArray();
            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_gray);
            btn_quesNext.setEnabled(false);
        } else {
            questionNo = (questionNo - 1);
            submitAnswer();
        }
    }

    private void submitAnswer() {
        quesProgress.setVisibility(View.VISIBLE);
        String cf_id = SdkPreferences.getCfId(this);
        String cmp_Id = SdkPreferences.getCmpId(this);
        String apiToken = SdkPreferences.getApiToken(this);
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        SurvayPost survayPost = new SurvayPost(quesJson.toString(), cf_id, cmp_Id, quesType);
        Call<SurvayPojo> pojoCall = apiInterface.submitSurvayAns(apiToken, survayPost);
        pojoCall.enqueue(new Callback<SurvayPojo>() {
            @Override
            public void onResponse(Call<SurvayPojo> call, Response<SurvayPojo> response) {
                quesProgress.setVisibility(View.GONE);
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    if (response.body().getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        clearValues();
                        setScreen();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SurvayPojo> call, Throwable t) {
                quesProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPreviousQuestion() {
        questionNo = (questionNo - 1);

        if (questionNo == -1) {
            clearValues();
            finish();
            clearValues();
        } else {
            setQuestions();
            btn_quesNext.setBackgroundResource(R.drawable.btn_pro_gray);
            btn_quesNext.setEnabled(false);

            if (selectedGridOptions.size() != 0) {
                if (selectedGridOptions.size() == header) {
                    btn_quesNext.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_quesNext.setEnabled(true);
                }
                selectedGridOptions.clear();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setPreviousQuestion();
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
        }

    }

    @SuppressLint("NewApi")
    public void showDialog() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i)) &&
                            !Character.toString(source.charAt(i)).equals("_") &&
                            !Character.toString(source.charAt(i)).equals("-") &&
                            !Character.toString(source.charAt(i)).equals(" ")) {
                        return "";
                    }
                }
                return null;
            }
        };

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_typefive);

        final EditText edt_dialogFive;
        Button btn_dialogFive;

        edt_dialogFive = dialog.findViewById(R.id.edt_dialogFive);
        btn_dialogFive = dialog.findViewById(R.id.btn_dialogFive);

        edt_dialogFive.setFilters(new InputFilter[]{filter});

        dialog.show();

        btn_dialogFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_dialogFive.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your review", Toast.LENGTH_SHORT).show();
                } else {
                    typeFiveReason = edt_dialogFive.getText().toString();
                    dialog.dismiss();
                }

            }
        });
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

    private void setScreen() {
        int count = Integer.valueOf(SdkPreferences.getCmpLength(this));
        int i = SdkPreferences.getCmpLengthCount(this);

        if (count == 1) {
            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    SdkPreferences.setQuestionType(this, "pre");
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    SdkPreferences.setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = SdkPreferences.getCmpLengthCountFlag(this);
                SdkPreferences.setCmpLengthCountFlag(this, flag + 1);
                try {
                    stagingJson.put("6", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        } else {
            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    SdkPreferences.setQuestionType(this, "pre");
                    try {
                        stagingJson.put("2", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    SdkPreferences.setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    SdkPreferences.setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = SdkPreferences.getCmpLengthCountFlag(this);
                SdkPreferences.setCmpLengthCountFlag(this, flag + 1);
                try {
                    stagingJson.put("6", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        }
    }
}
package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.CheckBoxTypeAdapter;
import com.monet.mylibrary.adapter.GridSliderAdapter;
import com.monet.mylibrary.adapter.MultipleImageSelectionAdapter;
import com.monet.mylibrary.adapter.RadioTypeAdapter;
import com.monet.mylibrary.adapter.SingleImageSelectionAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.listner.CheckBoxClickListner;
import com.monet.mylibrary.listner.ImageQClickListner;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.survay.SurvayPojo;
import com.monet.mylibrary.model.survay.SurvayPost;
import com.monet.mylibrary.utils.AnswerSavedClass;
import com.monet.mylibrary.utils.SdkPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.arrayList;
import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;
import static com.monet.mylibrary.activity.LandingPage.stagingJson;
import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLength;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpName;
import static com.monet.mylibrary.utils.SdkPreferences.getThumbUrl;
import static com.monet.mylibrary.utils.SdkPreferences.getVideoTime;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.setQuestionType;
import static com.monet.mylibrary.utils.SdkUtils.progressDialog;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout cl_quesQCard, cl_questionLayout, rate_layout;
    private ImageView back, img_rate;
    private SeekBar seekBar_rate;
    private ViewPager gridViewPager;
    private LinearLayout ll_edtLayout;
    private RelativeLayout rl_grid;
    public static Button btn_question;
    private RecyclerView rv_question;
    private TextView tv_questionNo, tv_question, tv_edtCount;
    public static TextView tv_nextGrid;
    private EditText edt_questionType;
    private boolean qCardVisible = true;
    public static int questionSize;
    public static String quesType = "";
    private int questionNo = 0;
    public static String token, type, cmp_id, cf_id, qusId, selectedAnsId, selectedQuesId, questionType, typeFiveReason = "";
    public static AnswerSavedClass savedQuesAndAnswers = new AnswerSavedClass();
    public static JSONObject dataPostJson1 = new JSONObject();
    public static JSONObject quesJson = new JSONObject();
    public static JSONObject quesJsonGrid = new JSONObject();
    public static JSONArray jsonArray = new JSONArray();
    private RadioTypeAdapter radioTypeAdapter;
    private CheckBoxTypeAdapter checkBoxTypeAdapter;
    private GridSliderAdapter gridSliderAdapter;
    private CircleIndicator indicatorGrid;
    private Dialog dialog;
    private SingleImageSelectionAdapter singleImageSelectionAdapter;
    private MultipleImageSelectionAdapter multipleImageSelectionAdapter;

    RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String quesId, String ansId) {
            selectedQuesId = quesId;
            selectedAnsId = ansId;

            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

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

            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

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

    private ImageQClickListner singleImageSelectionListner = new ImageQClickListner() {
        @Override
        public void onItemClick(String quesId, String optionId) {
            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

            selectedQuesId = quesId;
            selectedAnsId = optionId;

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getSingleImageQId().size() == 0 || savedQuesAndAnswers.getSingleImageOid().size() == 0) {
                savedQuesAndAnswers.setSingleImageQId(quesId);
                savedQuesAndAnswers.setSingleImageOid(optionId);
            } else {
                if (savedQuesAndAnswers.getSingleImageQId().contains(quesId)) {
                    int pos = savedQuesAndAnswers.getSingleImageQId().indexOf(quesId);
                    savedQuesAndAnswers.getSingleImageOid().set(pos, optionId);
                } else {
                    savedQuesAndAnswers.setSingleImageQId(quesId);
                    savedQuesAndAnswers.setSingleImageOid(optionId);
                }
            }
        }
    };

    private ImageQClickListner multiImageSelectionListner = new ImageQClickListner() {
        @Override
        public void onItemClick(String quesId, String optionId) {

            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

            selectedQuesId = quesId;

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getMultiImageQid().size() == 0 ||
                    savedQuesAndAnswers.getMultiImageOid().size() == 0) {
                savedQuesAndAnswers.setMultiImageQid(quesId);
                savedQuesAndAnswers.setMultiImageOid(optionId);
            } else {
                if (savedQuesAndAnswers.getMultiImageOid().contains(optionId)) {
                    int pos = savedQuesAndAnswers.getMultiImageOid().indexOf(optionId);
                    savedQuesAndAnswers.getMultiImageOid().remove(pos);
                } else {
                    savedQuesAndAnswers.setMultiImageOid(optionId);
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
        tv_questionNo = findViewById(R.id.tv_questionNo);
        tv_question = findViewById(R.id.tv_question);
        edt_questionType = findViewById(R.id.edt_questionType);
        back = findViewById(R.id.img_toolbarBack);
        rate_layout = findViewById(R.id.rate_layout);
        img_rate = findViewById(R.id.img_rate);
        seekBar_rate = findViewById(R.id.seekBar_rate);
        ll_edtLayout = findViewById(R.id.ll_edtLayout);
        tv_edtCount = findViewById(R.id.tv_edtCount);
        gridViewPager = findViewById(R.id.pager_grid);
        rl_grid = findViewById(R.id.rl_grid);
        indicatorGrid = findViewById(R.id.indicatorGrid);
        tv_nextGrid = findViewById(R.id.tv_nextGrid);
        edt_questionType.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_questionType.setRawInputType(InputType.TYPE_CLASS_TEXT);

        btn_question.setOnClickListener(this);
        tv_nextGrid.setOnClickListener(this);
        back.setOnClickListener(this);

        dataPostJson1 = new JSONObject();
        quesJson = new JSONObject();
        quesJsonGrid = new JSONObject();
        jsonArray = new JSONArray();

        dialog = new Dialog(QuestionActivity.this, R.style.Theme_Dialog);

        token = getApiToken(getApplicationContext());
        cmp_id = getCmpId(getApplicationContext());
        cf_id = getCfId(getApplicationContext());

        quesType = SdkPreferences.getQuestionType(getApplicationContext());

        if (quesType.equalsIgnoreCase("Pre")) {
            questionSize = preQuestions.size();
        } else {
            questionSize = postQuestions.size();
            Toast.makeText(getApplicationContext(), "Post", Toast.LENGTH_SHORT).show();
        }

        edt_questionType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tv_edtCount.setText(s.length() + "/500");

                if (s.length() > 0) {
                    btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_question.setEnabled(true);
                } else {
                    btn_question.setBackgroundResource(R.drawable.btn_disabled);
                    btn_question.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_questionType.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_questionType.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        seekBar_rate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {

                btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                btn_question.setEnabled(true);

                if (position <= 1) {
                    img_rate.setImageResource(R.drawable.ic_grayemotion_one);
                } else if (position == 2) {
                    img_rate.setImageResource(R.drawable.ic_grayemotion_two);
                } else if (position == 3) {
                    img_rate.setImageResource(R.drawable.ic_grayemotion_three);
                } else if (position == 4) {
                    img_rate.setImageResource(R.drawable.ic_grayemotion_four);
                } else if (position == 5) {
                    img_rate.setImageResource(R.drawable.ic_grayemotion_five);
                }

                selectedAnsId = String.valueOf(position);

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

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_toolbarBack) {
            setPreviousQuestion();
        } else if (i == R.id.btn_question) {

            if (btn_question.getText().toString().equalsIgnoreCase("Proceed")) {
                btn_question.setBackgroundResource(R.drawable.btn_disabled);
                btn_question.setEnabled(false);
                setQuestion();
            }

            if (qCardVisible) {
                cl_quesQCard.setVisibility(View.GONE);
                cl_questionLayout.setVisibility(View.VISIBLE);
                qCardVisible = false;
                btn_question.setText("Next");
                if (quesType.equalsIgnoreCase("Pre")) {
                    try {
                        stagingJson.put("2", "2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        stagingJson.put("3", "2");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (quesType.equalsIgnoreCase("Pre")) {
                    try {
                        stagingJson.put("2", "4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        stagingJson.put("3", "4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                nextQuestion();
            }
        }else if(i == R.id.tv_nextGrid){
            nextQuestion();
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
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "1";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("2")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "2";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(getApplicationContext(), checkBoxClickListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("3")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.VISIBLE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "3";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                if (seekBar_rate.getProgress() > 0) {
                    btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_question.setEnabled(true);
                }
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("4")) {
                rv_question.setVisibility(View.GONE);
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.VISIBLE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "4";
                if (edt_questionType.getText().length() >= 1) {
                    btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_question.setEnabled(true);
                }
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("5")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "5";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                radioTypeAdapter = new RadioTypeAdapter(getApplicationContext(), preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("6")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.VISIBLE);
                btn_question.setVisibility(View.GONE);
                questionType = "6";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                gridSliderAdapter = new GridSliderAdapter(getSupportFragmentManager(), preQuestions.get(questionNo).getOptions().size(), "pre", questionNo);
                gridViewPager.setAdapter(gridSliderAdapter);
                indicatorGrid.setViewPager(gridViewPager);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("7")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "7";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                singleImageSelectionAdapter = new SingleImageSelectionAdapter(getApplicationContext(), singleImageSelectionListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(singleImageSelectionAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("8")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "8";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                multipleImageSelectionAdapter = new MultipleImageSelectionAdapter(getApplicationContext(), multiImageSelectionListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(multipleImageSelectionAdapter);
            }
        } else {
            tv_question.setText(postQuestions.get(questionNo).getQuestion());
            qusId = postQuestions.get(questionNo).getQuestion_id();

            if (postQuestions.get(questionNo).getQuestion_type().equals("1")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "1";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, postQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("2")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "2";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(getApplicationContext(), checkBoxClickListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("3")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.VISIBLE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "3";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                if(seekBar_rate.getProgress() > 0){
                    btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_question.setEnabled(true);
                }
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("4")) {
                rv_question.setVisibility(View.GONE);
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.VISIBLE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "4";
                if (edt_questionType.getText().length() >= 1) {
                    btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
                    btn_question.setEnabled(true);
                }
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("5")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "5";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                radioTypeAdapter = new RadioTypeAdapter(getApplicationContext(), postQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("6")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.VISIBLE);
                btn_question.setVisibility(View.GONE);
                questionType = "6";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                gridSliderAdapter = new GridSliderAdapter(getSupportFragmentManager(), postQuestions.get(questionNo).getOptions().size(), "post", questionNo);
                gridViewPager.setAdapter(gridSliderAdapter);
                indicatorGrid.setViewPager(gridViewPager);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("7")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "7";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                singleImageSelectionAdapter = new SingleImageSelectionAdapter(getApplicationContext(), singleImageSelectionListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(singleImageSelectionAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("8")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "8";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                multipleImageSelectionAdapter = new MultipleImageSelectionAdapter(getApplicationContext(), multiImageSelectionListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(multipleImageSelectionAdapter);
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
            btn_question.setBackgroundResource(R.drawable.btn_disabled);
            btn_question.setEnabled(false);
            setQuestion();
        } else {
            questionNo = (questionNo - 1);
            if (quesType.equalsIgnoreCase("Pre")) {
                changeView();
            } else {
                submitAnswer();
            }
        }
    }

    private void changeView() {
        setContentView(R.layout.item_pre_complete);
        ImageView img_preComThumb = findViewById(R.id.img_preComThumb);
        TextView tv_time = findViewById(R.id.textView6);
        Button firstText = findViewById(R.id.button);
        TextView videoName = findViewById(R.id.textView5);
        Button proceed = findViewById(R.id.button2);
        ImageView backTollbar = findViewById(R.id.img_toolbarBack);
        backTollbar.setVisibility(View.GONE);

        if (getThumbUrl(QuestionActivity.this).equalsIgnoreCase("Nothing")) {
            img_preComThumb.setBackgroundResource(R.drawable.ic_imagenotavailable);
        } else {
            Picasso.get().load(getThumbUrl(this)).into(img_preComThumb);
        }

        tv_time.setText(getVideoTime(QuestionActivity.this) + " Mins");
        String test = getCmpName(QuestionActivity.this);
        test = String.valueOf(test.charAt(0));
        firstText.setText(test);
        videoName.setText(getCmpName(QuestionActivity.this));

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });

    }

    private void setPreviousQuestion() {
        questionNo = (questionNo - 1);

        if (questionNo == -1) {
            clearValues();
            sendStagingData(this);
            finish();
        } else {
            btn_question.setBackgroundResource(R.drawable.btn_disabled);
            btn_question.setEnabled(false);
            setQuestion();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        sendStagingData(this);
        return super.onSupportNavigateUp();
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
        final TextView tv_dialogLimit, tv_submitDialog, tv_cancelDialog;

        edt_dialogFive = dialog.findViewById(R.id.edt_dialogFive);
        tv_submitDialog = dialog.findViewById(R.id.tv_submitDialog);
        tv_dialogLimit = dialog.findViewById(R.id.tv_dialogLimit);
        tv_cancelDialog = dialog.findViewById(R.id.tv_cancelDialog);

        edt_dialogFive.setFilters(new InputFilter[]{filter});

        edt_dialogFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_dialogLimit.setText(s.length() + "/150");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();

        tv_submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_dialogFive.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please write your answer", Toast.LENGTH_SHORT).show();
                } else {
                    typeFiveReason = edt_dialogFive.getText().toString();
                    dialog.dismiss();
                }

            }
        });

        tv_cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setAnsJson() throws JSONException {

        if (quesType.equalsIgnoreCase("Pre")) {
            if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("1")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "1");
            } else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("2")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
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
            //6 no question json set on another screen
            else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("7")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "7");
            } else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("8")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getMultiImageOid().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getMultiImageOid().get(i)));
                }
                dataPostJson1.put("type", "8");
            }
        } else {
            if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("1")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "1");
            } else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("2")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
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
            }//6 no question json set on another screen
            else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("7")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "7");
            } else if (postQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("8")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getMultiImageOid().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getMultiImageOid().get(i)));
                }
                dataPostJson1.put("type", "8");
            }
        }

//        Log.d("json", "json " + quesJson);
    }

    private void submitAnswer() {
        progressDialog(QuestionActivity.this, "Please wait...", true);
        String cf_id = SdkPreferences.getCfId(getApplicationContext());
        String cmp_Id = SdkPreferences.getCmpId(getApplicationContext());
        String apiToken = SdkPreferences.getApiToken(getApplicationContext());
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        SurvayPost survayPost = new SurvayPost(quesJson.toString(), cf_id, cmp_Id, quesType);
        Call<SurvayPojo> pojoCall = apiInterface.submitSurvayAns(apiToken, survayPost);
        pojoCall.enqueue(new Callback<SurvayPojo>() {
            @Override
            public void onResponse(Call<SurvayPojo> call, Response<SurvayPojo> response) {
                progressDialog(QuestionActivity.this, "Please wait...", false);
                if (response.body() == null) {
                    Toast.makeText(getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    if (response.body().getCode().equals("200")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (quesType.equalsIgnoreCase("pre")) {
                            try {
                                stagingJson.put("2", "2");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                stagingJson.put("3", "2");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        clearValues();
                        setScreen();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SurvayPojo> call, Throwable t) {
                progressDialog(QuestionActivity.this, "Please wait...", false);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setPreviousQuestion();
    }

    private void clearValues() {
        dataPostJson1 = null;
        quesJson = null;
        quesJsonGrid = null;
        jsonArray = new JSONArray();
        savedQuesAndAnswers.getCheckAnsId().clear();
        savedQuesAndAnswers.getCheckQuesId().clear();
        savedQuesAndAnswers.getGridAnsIds().clear();
        savedQuesAndAnswers.getGridOptionIds().clear();
        savedQuesAndAnswers.getGridQuesIds().clear();
        savedQuesAndAnswers.getRadioAnsIds().clear();
        savedQuesAndAnswers.getRadioQuesIds().clear();
        savedQuesAndAnswers.getMultiImageOid().clear();
        savedQuesAndAnswers.getMultiImageQid().clear();
        savedQuesAndAnswers.getSingleImageOid().clear();
        savedQuesAndAnswers.getSingleImageQId().clear();
        questionNo = 0;
        questionSize = 0;

        if (quesType.equalsIgnoreCase("Pre")) {
            preQuestions.clear();
        } else {
            postQuestions.clear();
        }
    }

    private void setScreen() {
        int count = Integer.valueOf(getCmpLength(this));
        int i = getCmpLengthCount(this);

        if (count == 1) {
            if (arrayList.size() > i) {
                if (arrayList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    try {
                        stagingJson.put("2", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
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
                    setQuestionType(this, "pre");
                    try {
                        stagingJson.put("2", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    try {
                        stagingJson.put("3", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("4", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (arrayList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    try {
                        stagingJson.put("5", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
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
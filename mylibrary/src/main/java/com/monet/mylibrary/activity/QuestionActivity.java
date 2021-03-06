package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.monet.mylibrary.utils.AnswerSavedClass;
import com.monet.mylibrary.utils.SdkPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;
import static com.monet.mylibrary.activity.LandingPage.sequenceList;
import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLength;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpName;
import static com.monet.mylibrary.utils.SdkPreferences.getPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.getThumbUrl;
import static com.monet.mylibrary.utils.SdkPreferences.getVideoTime;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCount;
import static com.monet.mylibrary.utils.SdkPreferences.setCmpLengthCountFlag;
import static com.monet.mylibrary.utils.SdkPreferences.setPageStage;
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
    public static String token, type, cmp_id, qusId, selectedAnsId, selectedQuesId, questionType, typeFiveReason = "";
    public static AnswerSavedClass savedQuesAndAnswers;
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
    private ImageView forwardArrowImageViewQA, backArrowImageViewQA;

    RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String ansId) {
            selectedAnsId = ansId;

            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getRadioQuesIds().size() == 0 || savedQuesAndAnswers.getRadioAnsIds().size() == 0) {
                savedQuesAndAnswers.setRadioQuesIds(selectedQuesId);
                savedQuesAndAnswers.setRadioAnsIds(ansId);
            } else {
                if (savedQuesAndAnswers.getRadioQuesIds().contains(selectedQuesId)) {
                    int pos = savedQuesAndAnswers.getRadioQuesIds().indexOf(selectedQuesId);
                    savedQuesAndAnswers.getRadioAnsIds().set(pos, ansId);
                } else {
                    savedQuesAndAnswers.setRadioQuesIds(selectedQuesId);
                    savedQuesAndAnswers.setRadioAnsIds(ansId);
                }
            }
        }
    };

    private CheckBoxClickListner checkBoxClickListner = new CheckBoxClickListner() {
        @Override
        public void onItemCheckBoxClick(View view, int position, String ansId) {

            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getCheckQuesId().size() == 0 || savedQuesAndAnswers.getCheckAnsId().size() == 0) {
                savedQuesAndAnswers.setCheckQuesId(selectedQuesId);
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

    private ImageQClickListner imageQClickListner = new ImageQClickListner() {
        @Override
        public void onItemClick(String optionId) {
            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

            selectedAnsId = optionId;

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getSingleImageQId().size() == 0 || savedQuesAndAnswers.getSingleImageOid().size() == 0) {
                savedQuesAndAnswers.setSingleImageQId(selectedQuesId);
                savedQuesAndAnswers.setSingleImageOid(optionId);
            } else {
                if (savedQuesAndAnswers.getSingleImageQId().contains(selectedQuesId)) {
                    int pos = savedQuesAndAnswers.getSingleImageQId().indexOf(selectedQuesId);
                    savedQuesAndAnswers.getSingleImageOid().set(pos, optionId);
                } else {
                    savedQuesAndAnswers.setSingleImageQId(selectedQuesId);
                    savedQuesAndAnswers.setSingleImageOid(optionId);
                }
            }
        }
    };

    private ImageQClickListner multiImageSelectionListner = new ImageQClickListner() {
        @Override
        public void onItemClick(String optionId) {

            btn_question.setBackgroundResource(R.drawable.btn_pro_activate);
            btn_question.setEnabled(true);

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getMultiImageQid().size() == 0 ||
                    savedQuesAndAnswers.getMultiImageOid().size() == 0) {
                savedQuesAndAnswers.setMultiImageQid(selectedQuesId);
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
        backArrowImageViewQA = findViewById(R.id.backArrowImageViewQA);
        forwardArrowImageViewQA = findViewById(R.id.forwardArrowImageViewQA);

        btn_question.setOnClickListener(this);
        tv_nextGrid.setOnClickListener(this);
        back.setOnClickListener(this);

        dataPostJson1 = new JSONObject();
        quesJson = new JSONObject();
        quesJsonGrid = new JSONObject();
        jsonArray = new JSONArray();
        savedQuesAndAnswers = new AnswerSavedClass();

        dialog = new Dialog(QuestionActivity.this, R.style.Theme_Dialog);

        token = getApiToken(getApplicationContext());
        cmp_id = getCmpId(getApplicationContext());

        quesType = SdkPreferences.getQuestionType(getApplicationContext());

        if (quesType.equalsIgnoreCase("Pre")) {
            questionSize = preQuestions.size();
            setPageStage(QuestionActivity.this, getPageStage(this) + ",pre=question-start");
        } else {
            questionSize = postQuestions.size();
            setPageStage(QuestionActivity.this, getPageStage(this) + ",post=question-start");
        }
        sendStagingData(this, 0);

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
            }

            if (qCardVisible) {
                setQuestion();
                cl_quesQCard.setVisibility(View.GONE);
                cl_questionLayout.setVisibility(View.VISIBLE);
                qCardVisible = false;
                btn_question.setText("Next");
            } else {
                nextQuestion();
            }
        } else if (i == R.id.tv_nextGrid) {
            tv_nextGrid.setVisibility(View.GONE);
            btn_question.performClick();
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
            qusId = preQuestions.get(questionNo).getQs_id();

            if (preQuestions.get(questionNo).getQuestionType().equals("single")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "single";
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestionType().equals("multiple")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "multiple";
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(getApplicationContext(), checkBoxClickListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestionType().equals("numeric")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.VISIBLE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "numeric";
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
            } else if (preQuestions.get(questionNo).getQuestionType().equals("freetext")) {
                rv_question.setVisibility(View.GONE);
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.VISIBLE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "freetext";
            } else if (preQuestions.get(questionNo).getQuestionType().equals("grid")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.VISIBLE);
                btn_question.setVisibility(View.GONE);
                questionType = "grid";
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
                gridSliderAdapter = new GridSliderAdapter(getSupportFragmentManager(), preQuestions.get(questionNo).getOptions().size(), "pre", questionNo);
                gridViewPager.setAdapter(gridSliderAdapter);
                indicatorGrid.setViewPager(gridViewPager);
                forwardArrowImageViewQA.setVisibility(View.VISIBLE);

            } else if (preQuestions.get(questionNo).getQuestionType().equals("image")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "image";
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                singleImageSelectionAdapter = new SingleImageSelectionAdapter(getApplicationContext(), imageQClickListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(singleImageSelectionAdapter);
            } else if (preQuestions.get(questionNo).getQuestionType().equals("multiple_image")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "multiple_image";
                selectedQuesId = preQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                multipleImageSelectionAdapter = new MultipleImageSelectionAdapter(getApplicationContext(), multiImageSelectionListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(multipleImageSelectionAdapter);
            }
        } else {
            tv_question.setText(postQuestions.get(questionNo).getQuestion());
            qusId = postQuestions.get(questionNo).getQs_id();

            if (postQuestions.get(questionNo).getQuestionType().equals("single")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "single";
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                radioTypeAdapter = new RadioTypeAdapter(QuestionActivity.this, postQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestionType().equals("multiple")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "multiple";
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(getApplicationContext(), checkBoxClickListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestionType().equals("numeric")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.VISIBLE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "numeric";
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
            } else if (postQuestions.get(questionNo).getQuestionType().equals("freetext")) {
                rv_question.setVisibility(View.GONE);
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.VISIBLE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "freetext";
            } else if (postQuestions.get(questionNo).getQuestionType().equals("grid")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.VISIBLE);
                btn_question.setVisibility(View.GONE);
                questionType = "grid";
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
                gridSliderAdapter = new GridSliderAdapter(getSupportFragmentManager(), postQuestions.get(questionNo).getOptions().size(), "post", questionNo);
                gridViewPager.setAdapter(gridSliderAdapter);
                indicatorGrid.setViewPager(gridViewPager);
                forwardArrowImageViewQA.setVisibility(View.VISIBLE);

                gridViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                         Toast.makeText(QuestionActivity.this, "Page selected " + position, Toast.LENGTH_SHORT).show();
                        if (position == postQuestions.get(questionNo).getOptions().size()) {
                            backArrowImageViewQA.setVisibility(View.VISIBLE);
                            forwardArrowImageViewQA.setVisibility(View.GONE);
                        } else if (position == 0) {
                            backArrowImageViewQA.setVisibility(View.GONE);
                            forwardArrowImageViewQA.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


            } else if (postQuestions.get(questionNo).getQuestionType().equals("image")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "image";
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
                rv_question.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                singleImageSelectionAdapter = new SingleImageSelectionAdapter(getApplicationContext(), imageQClickListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(singleImageSelectionAdapter);
            } else if (postQuestions.get(questionNo).getQuestionType().equals("multiple_image")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "multiple_image";
                selectedQuesId = postQuestions.get(questionNo).getQs_id();
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
            Picasso.with(this).load(getThumbUrl(this)).into(img_preComThumb);
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
            if (quesType.equalsIgnoreCase("Pre")) {
                questionSize = preQuestions.size();
                String pageStage = getPageStage(QuestionActivity.this);
                pageStage = pageStage.replace("pre=question-start", "pre=question-exit");
                setPageStage(QuestionActivity.this, pageStage);
            } else {
                questionSize = postQuestions.size();
                String pageStage = getPageStage(QuestionActivity.this);
                pageStage = pageStage.replace("post=question-start", "post=question-exit");
                setPageStage(QuestionActivity.this, pageStage);
            }
            clearValues();
            finish();
        } else {
            btn_question.setBackgroundResource(R.drawable.btn_disabled);
            btn_question.setEnabled(false);
            setQuestion();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        sendStagingData(this, 0);
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
            if (preQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("single")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "single");
            } else if (preQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("multiple")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
                }
                dataPostJson1.put("type", "multiple");
            } else if (preQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("numeric")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "numeric");
            } else if (preQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("freetext")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", edt_questionType.getText().toString());
                dataPostJson1.put("type", "freetext");
            }
            //grid type question json set on another screen
            else if (preQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("image")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "image");
            } else if (preQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("multiple_image")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getMultiImageOid().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getMultiImageOid().get(i)));
                }
                dataPostJson1.put("type", "multiple_image");
            }
        } else {
            if (postQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("single")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "single");
            } else if (postQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("multiple")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getCheckAnsId().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getCheckAnsId().get(i)));
                }
                dataPostJson1.put("type", "multiple");
            } else if (postQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("numeric")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "numeric");
            } else if (postQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("freetext")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", edt_questionType.getText().toString());
                dataPostJson1.put("type", "freetext");
            } //6 no question json set on another screen
            else if (postQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("image")) {
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("options", Integer.valueOf(selectedAnsId));
                dataPostJson1.put("type", "image");
            } else if (postQuestions.get(questionNo).getQuestionType().equalsIgnoreCase("multiple_image")) {
                jsonArray = new JSONArray();
                dataPostJson1 = new JSONObject();
                quesJson.put(selectedQuesId, dataPostJson1);
                dataPostJson1.put("selectedOptions", jsonArray);
                for (int i = 0; i < savedQuesAndAnswers.getMultiImageOid().size(); i++) {
                    jsonArray.put(i, Integer.valueOf(savedQuesAndAnswers.getMultiImageOid().get(i)));
                }
                dataPostJson1.put("type", "multiple_image");
            }
        }

//        Log.d("json", "json " + quesJson);
    }

    private void submitAnswer() {
        progressDialog(QuestionActivity.this, "Please wait...", true);
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_response_id", getCfId(getApplicationContext()));
            jsonObject.put("survey", quesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<SurvayPojo> pojoCall = apiInterface.submitSurvayAns(token, jsonObject.toString());
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
                        if (quesType.equalsIgnoreCase("Pre")) {
                            questionSize = preQuestions.size();
                            String pageStage = getPageStage(QuestionActivity.this);
                            pageStage = pageStage.replace("pre=question-start", "pre=question-complete");
                            setPageStage(QuestionActivity.this, pageStage);
                        } else {
                            questionSize = postQuestions.size();
                            String pageStage = getPageStage(QuestionActivity.this);
                            pageStage = pageStage.replace("post=question-start", "post=question-complete");
                            setPageStage(QuestionActivity.this, pageStage);
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
        questionNo = 0;
        questionSize = 0;

        if (quesType.equalsIgnoreCase("Pre")) {
            preQuestions.clear();
        } else {
            postQuestions.clear();
        }
        sendStagingData(this, 0);
    }

    private void setScreen() {
        int count = getCmpLength(this);
        int i = getCmpLengthCount(this);

        if (count == 1) {
            if (sequenceList.size() > i) {
                if (sequenceList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        } else {
            if (sequenceList.size() > i) {
                if (sequenceList.get(i).equalsIgnoreCase("Pre")) {
                    setQuestionType(this, "pre");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Post")) {
                    setQuestionType(this, "post");
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, QuestionActivity.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Emotion")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, EmotionScreen.class));
                    finish();
                } else if (sequenceList.get(i).equalsIgnoreCase("Reaction")) {
                    setCmpLengthCount(this, i + 1);
                    startActivity(new Intent(this, ReactionScreen.class));
                    finish();
                }
            } else {
                int flag = getCmpLengthCountFlag(this);
                setCmpLengthCountFlag(this, flag + 1);
                startActivity(new Intent(this, ThankyouPage.class));
                finish();
            }
        }
    }
}
package com.monet.mylibrary.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.monet.mylibrary.adapter.RadioTypeAdapter;
import com.monet.mylibrary.listner.CheckBoxClickListner;
import com.monet.mylibrary.listner.IOnItemClickListener;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.utils.AnswerSavedClass;
import com.monet.mylibrary.utils.SdkPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;
import static com.monet.mylibrary.utils.SdkPreferences.getApiToken;
import static com.monet.mylibrary.utils.SdkPreferences.getCfId;
import static com.monet.mylibrary.utils.SdkPreferences.getCmpId;

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
    private EditText edt_questionType;
    private boolean qCardVisible = true;
    public static int questionSize;
    public static String quesType = "";
    private int questionNo = 0, radioType;
    public static String token, type, cmp_id, cf_id, qusId, selectedAnsId, selectedQuesId, questionType, typeFiveReason = "";
    public static AnswerSavedClass savedQuesAndAnswers = new AnswerSavedClass();
    public static JSONObject dataPostJson1 = new JSONObject();
    public static JSONObject quesJson = new JSONObject();
    public static JSONObject quesJsonGrid = new JSONObject();
    public static JSONArray jsonArray2 = new JSONArray();
    private RadioTypeAdapter radioTypeAdapter;
    private CheckBoxTypeAdapter checkBoxTypeAdapter;
    private GridSliderAdapter gridSliderAdapter;
    private CircleIndicator indicatorGrid;
    private Dialog dialog;

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

    private IOnItemClickListener rateItemClick = new IOnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

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
        rate_layout = findViewById(R.id.rate_layout);
        img_rate = findViewById(R.id.img_rate);
        seekBar_rate = findViewById(R.id.seekBar_rate);
        ll_edtLayout = findViewById(R.id.ll_edtLayout);
        tv_edtCount = findViewById(R.id.tv_edtCount);
        gridViewPager = findViewById(R.id.pager_grid);
        rl_grid = findViewById(R.id.rl_grid);
        indicatorGrid = findViewById(R.id.indicatorGrid);

        btn_question.setOnClickListener(this);
        back.setOnClickListener(this);

        dialog = new Dialog(QuestionActivity.this, R.style.Theme_Dialog);

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

        edt_questionType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tv_edtCount.setText(s.length()+"/500");

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
                btn_question.setText("Next");
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
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "1";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
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
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(this, checkBoxClickListner, preQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("3")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.VISIBLE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "3";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                radioType = Integer.parseInt(preQuestions.get(questionNo).getRadio_type());
//                seekBar_rate.setMax(radioType);
            } else if (preQuestions.get(questionNo).getQuestion_type().equals("4")) {
                rv_question.setVisibility(View.GONE);
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.VISIBLE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "4";
            }else if (preQuestions.get(questionNo).getQuestion_type().equals("5")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "5";
                selectedQuesId = preQuestions.get(questionNo).getQuestion_id();
                radioTypeAdapter = new RadioTypeAdapter(this, preQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            }else if(preQuestions.get(questionNo).getQuestion_type().equals("6")){
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
                checkBoxTypeAdapter = new CheckBoxTypeAdapter(this, checkBoxClickListner, postQuestions.get(questionNo).getOptions());
                rv_question.setAdapter(checkBoxTypeAdapter);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("3")) {
                rv_question.setVisibility(View.GONE);
                rate_layout.setVisibility(View.VISIBLE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "3";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                radioType = Integer.parseInt(postQuestions.get(questionNo).getRadio_type());
//                seekBar_rate.setMax(radioType);
            } else if (postQuestions.get(questionNo).getQuestion_type().equals("4")) {
                rv_question.setVisibility(View.GONE);
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.VISIBLE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "4";
            }else if (postQuestions.get(questionNo).getQuestion_type().equals("5")) {
                rv_question.setVisibility(View.VISIBLE);
                rate_layout.setVisibility(View.GONE);
                ll_edtLayout.setVisibility(View.GONE);
                rl_grid.setVisibility(View.GONE);
                btn_question.setVisibility(View.VISIBLE);
                questionType = "5";
                selectedQuesId = postQuestions.get(questionNo).getQuestion_id();
                radioTypeAdapter = new RadioTypeAdapter(this, postQuestions.get(questionNo).getOptions(), radioClickListner);
                rv_question.setAdapter(radioTypeAdapter);
            }else if(postQuestions.get(questionNo).getQuestion_type().equals("6")){
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
        }
    }

    private void setPreviousQuestion() {
        questionNo = (questionNo - 1);

        if (questionNo == -1) {
            clearValues();
            finish();
        } else {
            btn_question.setBackgroundResource(R.drawable.btn_disabled);
            btn_question.setEnabled(false);
            setQuestion();
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
                tv_dialogLimit.setText(s.length()+"/150");
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
//            else if (preQuestions.get(questionNo).getQuestion_type().equalsIgnoreCase("6")) {
//                dataPostJson1 = new JSONObject();
//                quesJsonGrid = new JSONObject();
//                quesJson.put(selectedQuesId, dataPostJson1);
//                dataPostJson1.put("options", quesJsonGrid);
//                for (int i = 0; i < savedQuesAndAnswers.getGridAnsIds().size(); i++) {
//                    quesJsonGrid.put(savedQuesAndAnswers.getGridOptionIds().get(i), savedQuesAndAnswers.getGridAnsIds().get(i));
//                }
//                dataPostJson1.put("type", "6");
//            }
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
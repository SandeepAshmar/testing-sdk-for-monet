package com.monet.mylibrary.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.GridOptionAdapter;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.sdk.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;
import static com.monet.mylibrary.activity.QuestionActivity.btn_question;
import static com.monet.mylibrary.activity.QuestionActivity.dataPostJson1;
import static com.monet.mylibrary.activity.QuestionActivity.quesJson;
import static com.monet.mylibrary.activity.QuestionActivity.quesJsonGrid;
import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;
import static com.monet.mylibrary.activity.QuestionActivity.tv_nextGrid;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    private int optionPosition = 0, questionNo = 0;
    private String survayType = "", questionId = "", optionId = "";
    private TextView tv_gridOption, tv_gridCount;
    private RecyclerView rv_grid;
    private GridOptionAdapter gridOptionAdapter;
    private View view;

    private RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String ansId) {

            if (survayType.equalsIgnoreCase("pre")) {
                if (savedQuesAndAnswers.getGridQuesIds().contains(preQuestions.get(questionNo).getQs_id())) {
                    if (savedQuesAndAnswers.getGridOptionIds().contains(preQuestions.get(questionNo)
                            .getOptions().get(optionPosition).getOpt_id())) {
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                preQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    } else {
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                preQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else if ((preQuestions.get(questionNo).getOptions().size()
                                - savedQuesAndAnswers.getSelectedIntSize() == 1)) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    }
                } else {
                    savedQuesAndAnswers.setSelectedIntSize(0);
                    tv_nextGrid.setVisibility(View.GONE);
                }
            } else {
                if (savedQuesAndAnswers.getGridQuesIds().contains(postQuestions.get(questionNo).getQs_id())) {
                    if (savedQuesAndAnswers.getGridOptionIds().contains(postQuestions.get(questionNo)
                            .getOptions().get(optionPosition).getOpt_id())) {
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                postQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    } else {
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                postQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else if ((postQuestions.get(questionNo).getOptions().size()
                                - savedQuesAndAnswers.getSelectedIntSize() == 1)) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    }
                } else {
                    savedQuesAndAnswers.setSelectedIntSize(0);
                    tv_nextGrid.setVisibility(View.GONE);
                }
            }

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getGridQuesIds().size() == 0 || savedQuesAndAnswers.getGridOptionIds().size() == 0
                    || savedQuesAndAnswers.getGridAnsIds().size() == 0) {
                savedQuesAndAnswers.setGridQuesIds(questionId);
                savedQuesAndAnswers.setGridOptionIds(optionId);
                savedQuesAndAnswers.setGridAnsIds(ansId);
                savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
            } else {
                if (savedQuesAndAnswers.getGridQuesIds().contains(questionId)) {
                    if (savedQuesAndAnswers.getGridOptionIds().contains(optionId)) {
                        int optPos = savedQuesAndAnswers.getGridOptionIds().indexOf(optionId);
                        savedQuesAndAnswers.getGridAnsIds().set(optPos, ansId);
                    } else {
                        savedQuesAndAnswers.setGridOptionIds(String.valueOf(optionId));
                        savedQuesAndAnswers.setGridAnsIds(String.valueOf(ansId));
                        savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                    }
                } else {
                    savedQuesAndAnswers.setGridQuesIds(questionId);
                    savedQuesAndAnswers.setGridOptionIds(String.valueOf(optionId));
                    savedQuesAndAnswers.setGridAnsIds(String.valueOf(ansId));
                    savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                }
            }

            try {
                dataPostJson1 = new JSONObject();
                quesJson.put(questionId, dataPostJson1);
                dataPostJson1.put("options", quesJsonGrid);
                quesJsonGrid.put(optionId, ansId);
                dataPostJson1.put("type", "grid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grid, container, false);
        optionPosition = getArguments().getInt("position");
        survayType = getArguments().getString("survayType");
        questionNo = getArguments().getInt("questionNo");
        initView();
        return view;
    }

    private View initView() {
        tv_gridCount = view.findViewById(R.id.tv_gridCount);
        tv_gridOption = view.findViewById(R.id.tv_gridOption);
        rv_grid = view.findViewById(R.id.rv_grid);
        rv_grid.setLayoutManager(new LinearLayoutManager(getActivity()));

        tv_gridCount.setText("Question " + (optionPosition + 1));

        if (survayType.equalsIgnoreCase("pre")) {
            questionId = preQuestions.get(questionNo).getQs_id();
            optionId = preQuestions.get(questionNo).getOptions().get(optionPosition).getOpt_id();
            tv_gridOption.setText(preQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            setAdapter(preQuestions.get(questionNo).getOptions().get(optionPosition).getValues());
        } else {
            questionId = postQuestions.get(questionNo).getQs_id();
            optionId = postQuestions.get(questionNo).getOptions().get(optionPosition).getOpt_id();
            tv_gridOption.setText(postQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            setAdapter(postQuestions.get(questionNo).getOptions().get(optionPosition).getValues());
        }
        return view;
    }

    private void setAdapter(ArrayList<Values> sdkGrid) {
        gridOptionAdapter = new GridOptionAdapter(radioClickListner, sdkGrid, optionId);
        rv_grid.setAdapter(gridOptionAdapter);
        gridOptionAdapter.notifyDataSetChanged();
    }

}

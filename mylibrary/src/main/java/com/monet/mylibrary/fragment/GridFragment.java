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
    private ArrayList<String> optionValue;

    private RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String ansId) {

            if (survayType.equalsIgnoreCase("pre")) {
                    if(savedQuesAndAnswers.getGridOptionIds().contains(preQuestions.get(questionNo)
                            .getOptions().get(optionPosition).getOpt_id())) {
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                preQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    }else{
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                preQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        }else if ((preQuestions.get(questionNo).getOptions().size()
                                - savedQuesAndAnswers.getSelectedIntSize() == 1)){
                            savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    }

            } else {
                    if(savedQuesAndAnswers.getGridOptionIds().contains(postQuestions.get(questionNo)
                            .getOptions().get(optionPosition).getOpt_id())) {
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                postQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    }else{
                        if (savedQuesAndAnswers.getSelectedIntSize() ==
                                postQuestions.get(questionNo).getOptions().size()) {
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        }else if ((postQuestions.get(questionNo).getOptions().size()
                                - savedQuesAndAnswers.getSelectedIntSize() == 1)){
                            savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                            tv_nextGrid.setVisibility(View.VISIBLE);
                            btn_question.setEnabled(true);
                        } else {
                            savedQuesAndAnswers.setSelectedIntSize((savedQuesAndAnswers.getSelectedIntSize() + 1));
                            tv_nextGrid.setVisibility(View.GONE);
                            btn_question.setEnabled(false);
                        }
                    }
            }

            if (savedQuesAndAnswers == null || savedQuesAndAnswers.getGridQuesIds().size() == 0 || savedQuesAndAnswers.getGridOptionIds().size() == 0
                    || savedQuesAndAnswers.getGridAnsIds().size() == 0) {
                savedQuesAndAnswers.setGridQuesIds(questionId);
                savedQuesAndAnswers.setGridOptionIds(optionId);
                savedQuesAndAnswers.setGridAnsIds(ansId);
            } else {
                if (savedQuesAndAnswers.getGridQuesIds().contains(questionId)) {
                    if (savedQuesAndAnswers.getGridOptionIds().contains(optionId)) {
                        int optPos = savedQuesAndAnswers.getGridOptionIds().indexOf(optionId);
                        savedQuesAndAnswers.getGridAnsIds().set(optPos, ansId);
                    } else {
                        savedQuesAndAnswers.setGridOptionIds(String.valueOf(optionId));
                        savedQuesAndAnswers.setGridAnsIds(String.valueOf(ansId));
                    }
                } else {
                    savedQuesAndAnswers.setGridQuesIds(questionId);
                    savedQuesAndAnswers.setGridOptionIds(String.valueOf(optionId));
                    savedQuesAndAnswers.setGridAnsIds(String.valueOf(ansId));
                }
            }

            try {
                dataPostJson1 = new JSONObject();
                quesJson.put(questionId, dataPostJson1);
                dataPostJson1.put("options", quesJsonGrid);
                quesJsonGrid.put(optionId, ansId);
                dataPostJson1.put("type", "6");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        optionPosition = getArguments().getInt("position");
        survayType = getArguments().getString("survayType");
        questionNo = getArguments().getInt("questionNo");
        return initView(view);
    }

    private View initView(View view) {
        tv_gridCount = view.findViewById(R.id.tv_gridCount);
        tv_gridOption = view.findViewById(R.id.tv_gridOption);
        rv_grid = view.findViewById(R.id.rv_grid);
        rv_grid.setLayoutManager(new LinearLayoutManager(getActivity()));

        tv_gridCount.setText("Question " + (optionPosition + 1));
        optionValue = new ArrayList<>();

        if (survayType.equalsIgnoreCase("pre")) {
            questionId = preQuestions.get(questionNo).getQs_id();
            optionId = preQuestions.get(questionNo).getOptions().get(optionPosition).getOpt_id();
            tv_gridOption.setText(preQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            for (int i = 0; i < preQuestions.get(questionNo).getValues().size(); i++) {
                preQuestions.get(questionNo).getValues().get(i).setOptionId(preQuestions.get(questionNo).getOptions().
                        get(optionPosition).getOpt_id());
            }
            setAdapter(preQuestions.get(questionNo).getValues());
        } else {
            questionId = postQuestions.get(questionNo).getQs_id();
            optionId = postQuestions.get(questionNo).getOptions().get(optionPosition).getOpt_id();
            tv_gridOption.setText(postQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            for (int i = 0; i < postQuestions.get(questionNo).getValues().size(); i++) {
                postQuestions.get(questionNo).getValues().get(i).setOptionId(postQuestions.get(questionNo).getOptions().
                        get(optionPosition).getOpt_id());
            }
            setAdapter(postQuestions.get(questionNo).getValues());
        }


        return view;
    }

    private void setAdapter(ArrayList<Values> sdkGrid) {
        gridOptionAdapter = new GridOptionAdapter(getActivity(), radioClickListner, sdkGrid, optionId);
        rv_grid.setAdapter(gridOptionAdapter);
        gridOptionAdapter.notifyDataSetChanged();
    }

}

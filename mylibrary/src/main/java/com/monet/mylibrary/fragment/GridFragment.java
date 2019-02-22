package com.monet.mylibrary.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.GridOptionAdapter;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkGrid;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;
import static com.monet.mylibrary.activity.QuestionActivity.savedQuesAndAnswers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    private int optionPosition = 0, questionNo = 0;
    private String survayType = "", questionId = "", optionId = "";
    private TextView tv_gridOption, tv_gridCount;
    private RecyclerView rv_grid;
    private GridOptionAdapter gridOptionAdapter;

    private RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String quesId, String ansId) {
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

    private View initView(View view){
        tv_gridCount = view.findViewById(R.id.tv_gridCount);
        tv_gridOption = view.findViewById(R.id.tv_gridOption);
        rv_grid = view.findViewById(R.id.rv_grid);
        rv_grid.setLayoutManager(new LinearLayoutManager(getActivity()));

        tv_gridCount.setText("Question "+(optionPosition+1));

        if(survayType.equalsIgnoreCase("pre")){
            questionId = preQuestions.get(questionNo).getQuestion_id();
            optionId = preQuestions.get(questionNo).getOptions().get(optionPosition).getOption_id();
            tv_gridOption.setText(preQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            setAdapter(preQuestions.get(questionNo).getOptions().get(optionPosition).getGrid());
        }else{
            questionId = postQuestions.get(questionNo).getQuestion_id();
            optionId = postQuestions.get(questionNo).getOptions().get(optionPosition).getOption_id();
            tv_gridOption.setText(postQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            setAdapter(postQuestions.get(questionNo).getOptions().get(optionPosition).getGrid());
        }

        return view;
    }

    private void setAdapter(ArrayList<SdkGrid> sdkGrid) {
        gridOptionAdapter = new GridOptionAdapter(getActivity(), radioClickListner, sdkGrid);
        rv_grid.setAdapter(gridOptionAdapter);
        gridOptionAdapter.notifyDataSetChanged();
    }

}

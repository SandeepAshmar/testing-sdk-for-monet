package com.monet.mylibrary.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.GridOptionAdapter;
import com.monet.mylibrary.listner.RadioClickListner;
import com.monet.mylibrary.model.question.SdkGrid;

import org.json.JSONException;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;
import static com.monet.mylibrary.activity.QuestionActivity.dataPostJson1;
import static com.monet.mylibrary.activity.QuestionActivity.quesJson;
import static com.monet.mylibrary.activity.QuestionActivity.quesJsonGrid;
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
    private View view;

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

            try {
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
        view = inflater.inflate(R.layout.fragment_grid, container, false);
        return view;
    }

    private void initView(){
        tv_gridCount = view.findViewById(R.id.tv_gridCount);
        tv_gridOption = view.findViewById(R.id.tv_gridOption);
        rv_grid = view.findViewById(R.id.rv_grid);
        rv_grid.setLayoutManager(new LinearLayoutManager(getActivity()));

        optionPosition = getArguments().getInt("position");
        survayType = getArguments().getString("survayType");
        questionNo = getArguments().getInt("questionNo");

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

    }

    private void setAdapter(ArrayList<SdkGrid> sdkGrid) {
        gridOptionAdapter = new GridOptionAdapter(getActivity(), radioClickListner, sdkGrid, optionId);
        rv_grid.setAdapter(gridOptionAdapter);
        gridOptionAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            }, 100);
        }
    }

}

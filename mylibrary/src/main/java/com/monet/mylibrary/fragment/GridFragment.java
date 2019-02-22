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

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    private int optionPosition = 0, questionNo = 0;
    private String survayType = "";
    private TextView tv_gridOption, tv_gridCount;
    private RecyclerView rv_grid;
    private GridOptionAdapter gridOptionAdapter;

    private RadioClickListner radioClickListner = new RadioClickListner() {
        @Override
        public void onItemClick(View view, int position, String quesId, String ansId) {

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
            tv_gridOption.setText(preQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            setAdapter(preQuestions.get(questionNo).getOptions().get(optionPosition).getGrid(), preQuestions.get(questionNo).getQuestion_id());
        }else{
            tv_gridOption.setText(postQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
            setAdapter(postQuestions.get(questionNo).getOptions().get(optionPosition).getGrid(), postQuestions.get(questionNo).getQuestion_id());
        }

        return view;
    }

    private void setAdapter(ArrayList<SdkGrid> sdkGrid, String question_id) {
        gridOptionAdapter = new GridOptionAdapter(getActivity(), radioClickListner, sdkGrid, question_id);
        rv_grid.setAdapter(gridOptionAdapter);
        gridOptionAdapter.notifyDataSetChanged();
    }

}

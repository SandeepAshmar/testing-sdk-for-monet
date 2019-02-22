package com.monet.mylibrary.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monet.mylibrary.R;

import androidx.fragment.app.Fragment;

import static com.monet.mylibrary.activity.LandingPage.postQuestions;
import static com.monet.mylibrary.activity.LandingPage.preQuestions;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    private int optionPosition = 0, questionNo = 0;
    private String survayType = "";
    private TextView tv_gridOption, tv_gridCount;

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

        tv_gridCount.setText("Question"+(optionPosition+1));

        if(survayType.equalsIgnoreCase("pre")){
            tv_gridOption.setText(preQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
        }else{
            tv_gridOption.setText(postQuestions.get(questionNo).getOptions().get(optionPosition).getOption_value());
        }

        return view;
    }

}

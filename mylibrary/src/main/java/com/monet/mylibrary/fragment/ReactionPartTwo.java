package com.monet.mylibrary.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monet.mylibrary.R;
import com.monet.mylibrary.utils.SdkPreferences;

import static com.monet.mylibrary.utils.SdkConstant.REACTION_A;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_B;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_C;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_D;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_E;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_F;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_G;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_H;
import static com.monet.mylibrary.utils.SdkConstant.REACTION_I;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReactionPartTwo extends Fragment {

    private View view;
    private String camEval;
    private ImageView img_reactionIconA, img_reactionIconB, img_reactionIconC, img_reactionIconD, img_reactionIconE, img_reactionIconF, img_reactionIconG, img_reactionIconH, img_reactionIconI;
    private TextView tv_reactionNameA, tv_reactionNameB, tv_reactionNameC, tv_reactionNameD, tv_reactionNameE, tv_reactionNameF, tv_reactionNameG, tv_reactionNameH, tv_reactionNameI;
    private LinearLayout ll_reactionA, ll_reactionB, ll_reactionC, ll_reactionD, ll_reactionE, ll_reactionF, ll_reactionG, ll_reactionH, ll_reactionI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reaction_part_two, container, false);

        img_reactionIconA = view.findViewById(R.id.img_reactionIconA);
        img_reactionIconB = view.findViewById(R.id.img_reactionIconB);
        img_reactionIconC = view.findViewById(R.id.img_reactionIconC);
        img_reactionIconD = view.findViewById(R.id.img_reactionIconD);
        img_reactionIconE = view.findViewById(R.id.img_reactionIconE);
        img_reactionIconF = view.findViewById(R.id.img_reactionIconF);
        img_reactionIconG = view.findViewById(R.id.img_reactionIconG);
        img_reactionIconH = view.findViewById(R.id.img_reactionIconH);
        img_reactionIconI = view.findViewById(R.id.img_reactionIconI);
        tv_reactionNameA = view.findViewById(R.id.tv_reactionNameA);
        tv_reactionNameB = view.findViewById(R.id.tv_reactionNameB);
        tv_reactionNameC = view.findViewById(R.id.tv_reactionNameC);
        tv_reactionNameD = view.findViewById(R.id.tv_reactionNameD);
        tv_reactionNameE = view.findViewById(R.id.tv_reactionNameE);
        tv_reactionNameF = view.findViewById(R.id.tv_reactionNameF);
        tv_reactionNameG = view.findViewById(R.id.tv_reactionNameG);
        tv_reactionNameH = view.findViewById(R.id.tv_reactionNameH);
        tv_reactionNameI = view.findViewById(R.id.tv_reactionNameI);
        ll_reactionA = view.findViewById(R.id.ll_reactionA);
        ll_reactionB = view.findViewById(R.id.ll_reactionB);
        ll_reactionC = view.findViewById(R.id.ll_reactionC);
        ll_reactionD = view.findViewById(R.id.ll_reactionD);
        ll_reactionE = view.findViewById(R.id.ll_reactionE);
        ll_reactionF = view.findViewById(R.id.ll_reactionF);
        ll_reactionG = view.findViewById(R.id.ll_reactionG);
        ll_reactionH = view.findViewById(R.id.ll_reactionH);
        ll_reactionI = view.findViewById(R.id.ll_reactionI);

        camEval = SdkPreferences.getCamEval(getActivity());
        camEval = camEval.replaceAll("[0-9()]", "");
        SdkPreferences.setCamEval(getContext(),camEval);

        setReactionIcons();

        return view;
    }

    public void setReactionIcons(){

        if(camEval.contains("a")){
            ll_reactionA.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_A).into(img_reactionIconA);
            tv_reactionNameA.setText(R.string.reaction_A);
        }
        if(camEval.contains("b")){
            ll_reactionB.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_B).into(img_reactionIconB);
            tv_reactionNameB.setText(R.string.reaction_B);
        }
        if(camEval.contains("c")){
            ll_reactionC.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_C).into(img_reactionIconC);
            tv_reactionNameC.setText(R.string.reaction_C);
        }
        if(camEval.contains("d")){
            ll_reactionD.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_D).into(img_reactionIconD);
            tv_reactionNameD.setText(R.string.reaction_D);
        }
        if(camEval.contains("e")){
            ll_reactionE.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_E).into(img_reactionIconE);
            tv_reactionNameE.setText(R.string.reaction_E);
        }
        if(camEval.contains("f")){
            ll_reactionF.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_F).into(img_reactionIconF);
            tv_reactionNameF.setText(R.string.reaction_F);
        }
        if(camEval.contains("g")){
            ll_reactionG.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_G).into(img_reactionIconG);
            tv_reactionNameG.setText(R.string.reaction_G);
        }
        if(camEval.contains("h")){
            ll_reactionH.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_H).into(img_reactionIconH);
            tv_reactionNameH.setText(R.string.reaction_H);
        }
        if(camEval.contains("i")){
            ll_reactionI.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(REACTION_I).into(img_reactionIconI);
            tv_reactionNameI.setText(R.string.reaction_I);
        }
    }
}

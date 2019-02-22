package com.monet.mylibrary.adapter;

import android.os.Bundle;

import com.monet.mylibrary.fragment.GridFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class GridSliderAdapter extends FragmentStatePagerAdapter {

    private int size = 0;
    private String survayType = "";
    private int questionNo = 0;

    public GridSliderAdapter(@NonNull FragmentManager fm, int size, String survayType, int questionNo) {
        super(fm);
        this.size = size;
        this.survayType = survayType;
        this.questionNo = questionNo;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        GridFragment gridFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("survayType", survayType);
        bundle.putInt("questionNo", questionNo);
        gridFragment.setArguments(bundle);
        return gridFragment;
    }

    @Override
    public int getCount() {
        return size;
    }
}

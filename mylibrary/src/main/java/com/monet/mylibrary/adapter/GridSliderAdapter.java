package com.monet.mylibrary.adapter;

import android.os.Bundle;

import com.monet.mylibrary.fragment.GridFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class GridSliderAdapter extends FragmentStatePagerAdapter {

    private int size = 0;

    public GridSliderAdapter(@NonNull FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        GridFragment gridFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        gridFragment.setArguments(bundle);
        return gridFragment;
    }

    @Override
    public int getCount() {
        return size;
    }
}

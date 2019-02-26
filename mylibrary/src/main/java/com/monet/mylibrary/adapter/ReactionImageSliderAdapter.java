package com.monet.mylibrary.adapter;

import android.os.Bundle;

import com.monet.mylibrary.fragment.ReactionPartOneFragment;
import com.monet.mylibrary.fragment.ReactionPartThreeFragment;
import com.monet.mylibrary.fragment.ReactionPartTwoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ReactionImageSliderAdapter extends FragmentStatePagerAdapter {

    private ReactionPartOneFragment reactionPartOneFragment;
    private ReactionPartTwoFragment reactionPartTwoFragment;
    private ReactionPartThreeFragment reactionPartThreeFragment;

    public ReactionImageSliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        if (position == 0) {
            reactionPartOneFragment = new ReactionPartOneFragment();
            reactionPartOneFragment.setArguments(bundle);
            return reactionPartOneFragment;
        } else if (position == 1) {
            reactionPartTwoFragment = new ReactionPartTwoFragment();
            reactionPartTwoFragment.setArguments(bundle);
            return reactionPartTwoFragment;
        } else {
            reactionPartThreeFragment = new ReactionPartThreeFragment();
            reactionPartThreeFragment.setArguments(bundle);
            return reactionPartThreeFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

package com.monet.mylibrary.adapter;

import android.os.Bundle;

import com.monet.mylibrary.fragment.EmotionImageSlider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ImageSliderAdapter extends FragmentStatePagerAdapter {

    public ImageSliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        EmotionImageSlider emotionImageSlider = new EmotionImageSlider();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        emotionImageSlider.setArguments(bundle);
        return emotionImageSlider;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

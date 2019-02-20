package com.monet.mylibrary.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.fragment.EmotionImageSlider;

public class ImageSliderAdapter extends FragmentStatePagerAdapter {

    public ImageSliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        EmotionImageSlider emotionImageSlider =new EmotionImageSlider();
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

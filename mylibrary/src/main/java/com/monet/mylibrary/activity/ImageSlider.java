package com.monet.mylibrary.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.ImageSliderAdapter;

public class ImageSlider extends AppCompatActivity {

    private ViewPager imageSliderViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        imageSliderViewPager = findViewById(R.id.imageSliderViewPager);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this);
    }


}

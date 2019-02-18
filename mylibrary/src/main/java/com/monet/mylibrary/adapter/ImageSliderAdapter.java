package com.monet.mylibrary.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.widget.ImageView;

public class ImageSliderAdapter  extends PagerAdapter {
    private Context context;

    public ImageSliderAdapter(Context context)
    {
        this.context = context;
    }


    @Override
    public int getCount() {
        return 0;
    }

    private  int[] sliderImageId = new int[]{
        
    };

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }
}

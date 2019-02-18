package com.monet.mylibrary.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.monet.mylibrary.R;

public class ImageSliderAdapter  extends PagerAdapter {
    private Context context;
    private  int[] sliderImageId = new int[]{
            R.drawable.ic_view_pager_allow_camera, R.drawable.ic_view_pager_face_camera,R.drawable.ic_view_pager_be_in_good_light
    };

    public ImageSliderAdapter(Context context)
    {
        this.context = context;
    }




    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(sliderImageId[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public int getCount() {
        return sliderImageId.length;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}

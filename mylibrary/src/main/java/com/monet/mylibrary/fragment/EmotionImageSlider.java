package com.monet.mylibrary.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monet.mylibrary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmotionImageSlider extends Fragment {

    private TextView tv_emotionSliderText;
    private ImageView img_emotionSliderImage;
    public EmotionImageSlider() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emotion_image_slider, container, false);
        tv_emotionSliderText = view.findViewById(R.id.tv_emotionSliderText);
        img_emotionSliderImage = view.findViewById(R.id.img_emotionSliderImage);
        int position = getArguments().getInt("position");

        switch (position)
        {
            case 0:
                tv_emotionSliderText.setText("Allow your Camera");
                img_emotionSliderImage.setImageResource(R.drawable.ic_allow_camera);
                break;
            case 1:
                tv_emotionSliderText.setText("Face camera without covering your face");
                img_emotionSliderImage.setImageResource(R.drawable.ic_face_camera);
                break;
            case 2:
                tv_emotionSliderText.setText("Be in Good lighting");
                img_emotionSliderImage.setImageResource(R.drawable.ic_be_in_good_light);
                break;
        }
        return view;
    }

}

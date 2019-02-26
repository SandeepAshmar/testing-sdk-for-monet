package com.monet.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.ReactionImageSliderAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class ReactionScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView tv_next;
    private ReactionImageSliderAdapter reactionImageSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_screen);

        viewPager = findViewById(R.id.viewPagerReaction);
        tv_next = findViewById(R.id.textView8);
        circleIndicator = findViewById(R.id.circleIndicatorReaction);

        reactionImageSliderAdapter = new ReactionImageSliderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(reactionImageSliderAdapter);
        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tv_next.setVisibility(View.VISIBLE);
                } else {
                    tv_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReactionScreen.this, ReactionWatchVideo.class));
                finish();
            }
        });

    }

}

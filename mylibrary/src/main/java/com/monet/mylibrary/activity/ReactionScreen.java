package com.monet.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.ReactionImageSliderAdapter;

import me.relex.circleindicator.CircleIndicator;

import static com.monet.mylibrary.utils.SdkPreferences.getPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.setPageStage;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

public class ReactionScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView tv_next;
    private ReactionImageSliderAdapter reactionImageSliderAdapter;
    private ImageView forwardArrowImageViewRS, backArrowImageViewRS, img_toolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_screen);

        viewPager = findViewById(R.id.viewPagerReaction);
        tv_next = findViewById(R.id.textView8);
        circleIndicator = findViewById(R.id.circleIndicatorReaction);
        forwardArrowImageViewRS = findViewById(R.id.forwardArrowImageViewRS);
        backArrowImageViewRS = findViewById(R.id.backArrowImageViewRS);
        forwardArrowImageViewRS.setVisibility(View.VISIBLE);
        img_toolbarBack = findViewById(R.id.img_toolbarBack);
        img_toolbarBack.setVisibility(View.GONE);

        reactionImageSliderAdapter = new ReactionImageSliderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(reactionImageSliderAdapter);
        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        tv_next.setVisibility(View.GONE);
                        forwardArrowImageViewRS.setVisibility(View.VISIBLE);
                        backArrowImageViewRS.setVisibility(View.GONE);
                        break;
                    case 1:
                        forwardArrowImageViewRS.setVisibility(View.VISIBLE);
                        backArrowImageViewRS.setVisibility(View.VISIBLE);
                        tv_next.setVisibility(View.GONE);
                        break;
                    case 2:
                        forwardArrowImageViewRS.setVisibility(View.GONE);
                        backArrowImageViewRS.setVisibility(View.VISIBLE);
                        tv_next.setVisibility(View.VISIBLE);
                        break;
                }
//                if (position == 2) {
//                    tv_next.setVisibility(View.VISIBLE);
//                } else {
//                    tv_next.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        forwardArrowImageViewRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                switch (currentPosition) {
                    case 0:
                        viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        viewPager.setCurrentItem(2);
                }
            }
        });

        backArrowImageViewRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                switch (currentPosition) {
                    case 1:
                        viewPager.setCurrentItem(0);
                        break;
                    case 2:
                        viewPager.setCurrentItem(1);
                        break;
                    default:
                        viewPager.setCurrentItem(0);
                }
            }
        });


        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageStage = getPageStage(ReactionScreen.this);
                pageStage = pageStage.replace("reaction=q-card-start", "reaction=video-start");
                setPageStage(ReactionScreen.this, pageStage);
                sendStagingData(ReactionScreen.this, 0);
                startActivity(new Intent(ReactionScreen.this, ReactionWatchVideo.class));
                finish();
            }
        });

        setPageStage(this, getPageStage(this) + ",reaction=q-card-start");
        sendStagingData(this, 0);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        String pageStage = getPageStage(ReactionScreen.this);
        pageStage = pageStage.replace("reaction=q-card-start", "reaction=q-card-exit");
        setPageStage(ReactionScreen.this, pageStage);
        sendStagingData(this, 0);
        finish();
        super.onBackPressed();
    }

}

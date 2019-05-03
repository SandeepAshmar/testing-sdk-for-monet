package com.monet.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.monet.mylibrary.R;
import com.monet.mylibrary.adapter.ReactionImageSliderAdapter;

import org.json.JSONException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import static com.monet.mylibrary.utils.SdkPreferences.getPageStage;
import static com.monet.mylibrary.utils.SdkPreferences.setPageStage;
import static com.monet.mylibrary.utils.SdkUtils.sendStagingData;

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

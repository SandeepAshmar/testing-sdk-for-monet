package com.monet.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.monet.mylibrary.R;
import com.monet.mylibrary.fragment.ReactionPartOne;
import com.monet.mylibrary.fragment.ReactionPartThree;
import com.monet.mylibrary.fragment.ReactionPartTwo;
import com.monet.mylibrary.utils.SdkUtils;

public class ReactionScreen extends AppCompatActivity {

    private String layoutShow = "first_layout";
    private Button btn_reactionProceed, btn_reactionExit, btn_reactionNext;
    private RelativeLayout reaction_container;
    private LinearLayout btn_exit_layout, btn_nextLayout;
    private FrameLayout reaction_frame;
    private ReactionPartOne reactionPartOne;
    private ReactionPartTwo reactionPartTwo;
    private ReactionPartThree reactionPartThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_screen);

        btn_reactionExit = findViewById(R.id.btn_reactionExit);
        btn_reactionProceed = findViewById(R.id.btn_reactionProceed);
        btn_nextLayout = findViewById(R.id.btn_nextLayout);
        btn_exit_layout = findViewById(R.id.btn_exit_layout);
        reaction_frame = findViewById(R.id.reaction_frame);
        btn_reactionNext = findViewById(R.id.btn_reactionNext);

        reactionPartOne = new ReactionPartOne();
        reactionPartTwo = new ReactionPartTwo();
        reactionPartThree = new ReactionPartThree();

        setFragment(reactionPartOne);
        setFragmentButton();

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.reaction_frame, fragment);
        fragmentTransaction.commit();
    }

    private void setFragmentButton() {
        btn_reactionProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(reactionPartTwo);
                layoutShow = "second_layout";
                btn_nextLayout.setVisibility(View.VISIBLE);
                btn_exit_layout.setVisibility(View.GONE);
            }
        });

        btn_reactionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutShow.equalsIgnoreCase("second_layout")) {
                    setFragment(reactionPartThree);
                    btn_reactionNext.setText("watch");
                    layoutShow = "watch";
                } else if (layoutShow.equalsIgnoreCase("watch")) {
                    if (SdkUtils.isConnectionAvailable(ReactionScreen.this)) {
//                        try {
////                            stagingJson.put("5", "3");
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
                        startActivity(new Intent(ReactionScreen.this, ReactionWatchVideo.class));
                        finish();
                    } else {
                        SdkUtils.noInternetDialog(ReactionScreen.this);
                    }

                }
            }
        });
    }
}

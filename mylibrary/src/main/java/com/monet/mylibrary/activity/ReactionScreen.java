package com.monet.mylibrary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.monet.mylibrary.R;

public class ReactionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_screen);
        startActivity(new Intent(this, PlayVideoAndRecordScreen.class));
        finish();
    }
}

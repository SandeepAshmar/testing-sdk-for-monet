package com.monet.mylibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SUPER_AWESOME_APP";

    private Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        btn1 = findViewById(R.id.btn_one);
        btn2 = findViewById(R.id.btn_two);
        btn3 = findViewById(R.id.btn_three);
        btn4 = findViewById(R.id.btn_four);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void Toast(Context context, String message){
        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void openActivity(Activity activity){
        activity.setContentView(R.layout.activity_debug);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_one) {
            Toast.makeText(this, "Button first", Toast.LENGTH_SHORT).show();
        }else if(i == R.id.btn_two){
            Toast.makeText(this, "Button secound", Toast.LENGTH_SHORT).show();
        }else if(i == R.id.btn_three){
            Toast.makeText(this, "Button third", Toast.LENGTH_SHORT).show();
        }else if(i == R.id.btn_four){
            Toast.makeText(this, "Button forth", Toast.LENGTH_SHORT).show();
        }
    }
}

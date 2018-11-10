package com.monet.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DebugActivity extends AppCompatActivity {

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

    public void btnFirstClick(View view){
        Toast.makeText(this, "btn 1", Toast.LENGTH_SHORT).show();
    }

    public void btnSecoundClick(View view){
        Toast.makeText(this, "btn 2", Toast.LENGTH_SHORT).show();
    }

    public void btnThirdClick(View view){
        Toast.makeText(this, "btn 3", Toast.LENGTH_SHORT).show();
    }

    public void btnForthClick(View view){
        Toast.makeText(this, "btn 4", Toast.LENGTH_SHORT).show();
    }
}

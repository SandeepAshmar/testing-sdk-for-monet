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
    private static Context mContext;

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

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void openDialog(View view) {
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("btn clicked");
        progressDialog.show();
    }

    public static void openActivity(Activity activity, Context context){
        activity.setContentView(R.layout.activity_debug);
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_one) {
            Toast(mContext, "btn 1");
        } else if (i == R.id.btn_two) {
            Toast(mContext, "btn 2");
        } else if (i == R.id.btn_three) {
            Toast(mContext, "btn 3");
        } else if (i == R.id.btn_four) {
            Toast(mContext, "btn 3");
        }
    }
}

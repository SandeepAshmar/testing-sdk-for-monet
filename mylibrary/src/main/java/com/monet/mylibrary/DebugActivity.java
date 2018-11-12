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

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "SUPER_AWESOME_APP";
    private static Button btn1, btn2, btn3, btn4;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        btn1 = findViewById(R.id.btn_one);
        btn2 = findViewById(R.id.btn_two);
        btn3 = findViewById(R.id.btn_three);
        btn4 = findViewById(R.id.btn_four);

      btn1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(DebugActivity.this, "btn 1 clicked", Toast.LENGTH_SHORT).show();
          }
      });
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void openDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("btn clicked");
        progressDialog.show();
    }

    public static void openActivity(Activity activity) {
        activity.setContentView(R.layout.activity_debug);
        mContext = activity.getApplicationContext();
    }
}

package com.monet.mylibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "SUPER_AWESOME_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void openActivity(Activity activity) {
        activity.setContentView(R.layout.activity_debug);
    }

    public static void btnFirstClick(View view) {
        openDialog(view);
    }

    public static void btnSecoundClick(View view) {
        openDialog(view);
    }

    public static void btnThirdClick(View view) {
        openDialog(view);
    }

    public static void btnForthClick(View view) {
        openDialog(view);
    }

    public static void openDialog(View view) {
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("btn clicked");
        progressDialog.show();
    }
}

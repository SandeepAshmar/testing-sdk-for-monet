package com.monet.mylibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    public static void Toast(Context context, String message){
        Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    public static void dialog(Activity activity, Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("This is loader");
        progressDialog.show();
        activity.setContentView(R.layout.activity_debug);
    }
}

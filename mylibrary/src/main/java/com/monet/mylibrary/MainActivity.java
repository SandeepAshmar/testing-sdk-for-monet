package com.monet.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SUPER_AWESOME_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void Toast(Context context, String message){
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}

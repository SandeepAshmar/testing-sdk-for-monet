package com.monet.mylibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

abstract public class LogDebog {

    private static final String TAG = "SUPER_AWESOME_APP";

    static Context context;

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void Toast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

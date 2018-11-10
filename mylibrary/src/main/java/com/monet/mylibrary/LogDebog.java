package com.monet.mylibrary;

import android.util.Log;

abstract public class LogDebog {

    private static final String TAG = "SUPER_AWESOME_APP";

    public static void d(String message){
        Log.d(TAG, message);
    }
    
}

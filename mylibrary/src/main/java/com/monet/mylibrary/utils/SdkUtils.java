package com.monet.mylibrary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.monet.mylibrary.R;


public class SdkUtils {

    public static AlertDialog d;

    public static boolean isConnectionAvailable(Context ctx) {
        ConnectivityManager mManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mManager.getActiveNetworkInfo();
        return (mNetworkInfo != null) && (mNetworkInfo.isConnected());
    }

    public static void noInternetDialog(Context ctx) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ctx, R.style.Theme_AppCompat_Light_Dialog);
        dialog.setMessage("Please check your internet connection and try again later.");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        d = dialog.create();
        d.show();
    }
}

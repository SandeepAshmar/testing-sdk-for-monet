package com.monet.mylibrary.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.monet.mylibrary.R;


public class SdkUtils {

    public static AlertDialog d;
    public static ProgressDialog pd ;

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

    public static String convertVideoTime(long millis) {
        String videoTime, hourString, minutesString, secoundsString;
        long secoundsInMilli = 1000;
        long minutesInMilli = secoundsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long hours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long minutes = millis / minutesInMilli;
        millis = millis % minutesInMilli;

        long secounds = millis / secoundsInMilli;

        if (hours == 0) {
            if (minutes >= 0 && minutes <= 9) {
                minutesString = "0" + minutes;
            } else {
                minutesString = String.valueOf(minutes);
            }

            if (secounds >= 0 && secounds <= 9) {
                secoundsString = "0" + secounds;
            } else {
                secoundsString = String.valueOf(secounds);
            }

            videoTime = minutesString + ":" + secoundsString;
        } else {

            if (hours >= 0 && hours <= 9) {
                hourString = "0" + hours;
            } else {
                hourString = String.valueOf(hours);
            }

            if (minutes >= 0 && minutes <= 9) {
                minutesString = "0" + minutes;
            } else {
                minutesString = String.valueOf(minutes);
            }

            if (secounds >= 0 && secounds <= 9) {
                secoundsString = "0" + secounds;
            } else {
                secoundsString = String.valueOf(secounds);
            }

            videoTime = hourString + ":" + minutesString + ":" + secoundsString;
        }

        return videoTime;
    }

    public static void progressDialog(Context context, String message, boolean show){
        if(show){
            pd = new ProgressDialog(context);
            pd.setMessage(message);
            pd.setCancelable(false);
            pd.show();
        }else{
            pd.dismiss();
        }
    }
}

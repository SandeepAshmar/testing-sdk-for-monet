package com.monet.mylibrary.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.monet.mylibrary.R;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.StagingPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.stagingJson;


public class SdkUtils {

    public static AlertDialog d;
    public static ProgressDialog pd;

    public static boolean isConnectionAvailable(Context ctx) {
        ConnectivityManager mManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mManager.getActiveNetworkInfo();
        return (mNetworkInfo != null) && (mNetworkInfo.isConnected());
    }

//    public static void noInternetDialog(Context ctx) {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(ctx, R.style.Theme_AppCompat_Light_Dialog);
//        dialog.setMessage("Please check your internet connection and try again later.");
//        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        d = dialog.create();
//        d.show();
//    }

    public static String convertVideoTime(long millis) {
        String videoTime, hourString, minutesString, secoundsString;
//        long secoundsInMilli = 1000;
//        long minutesInMilli = secoundsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//
//        long hours = millis / hoursInMilli;
//        millis = millis % hoursInMilli;
//
//        long minutes = millis / minutesInMilli;
//        millis = millis % minutesInMilli;
//
//        long secounds = Math.round(millis / secoundsInMilli);

        long secounds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = millis / (1000 * 60 * 60);

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

    public static void progressDialog(Context context, String message, boolean show) {
        if (show) {
            pd = new ProgressDialog(context);
            pd.setMessage(message);
            pd.setCancelable(false);
            pd.show();
        } else {
            pd.dismiss();
        }
    }

    public static void sendStagingData(final Context context) {
        String cmp_id = SdkPreferences.getCmpId(context);
        String token = SdkPreferences.getApiToken(context);

        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<StagingPojo> pojoCall = apiInterface.sendStagingData(token, cmp_id, stagingJson);

        pojoCall.enqueue(new Callback<StagingPojo>() {
            @Override
            public void onResponse(Call<StagingPojo> call, Response<StagingPojo> response) {
                if (response.body() == null) {
                    Log.d("TAG", "staging error: "+response.raw().message());
                } else {
                    if (response.body().getCode().equalsIgnoreCase("200")) {
                        Log.d("TAG", "staging success: "+response.body().getMessage());
                    } else {
                        Log.d("TAG", "staging caode no match: "+response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<StagingPojo> call, Throwable t) {
                Log.d("TAG", "staging through: "+ t.getMessage());
            }
        });
    }
}

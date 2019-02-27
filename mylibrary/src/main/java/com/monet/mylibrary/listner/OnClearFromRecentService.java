package com.monet.mylibrary.listner;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.model.StagingPojo;
import com.monet.mylibrary.utils.SdkPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.monet.mylibrary.activity.LandingPage.stagingJson;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        callApi();
        stopSelf();
    }

    private void callApi() {
        String cmp_id = SdkPreferences.getCmpId(this);
        String token = "Bearer " + SdkPreferences.getApiToken(this);

        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Log.d("TAG", "callApi: staging data "+stagingJson);
        Call<StagingPojo> pojoCall = apiInterface.sendStagingData(token, cmp_id, stagingJson);

        pojoCall.enqueue(new Callback<StagingPojo>() {
            @Override
            public void onResponse(Call<StagingPojo> call, Response<StagingPojo> response) {
                if (response.body() == null) {
                    Toast.makeText(OnClearFromRecentService.this, "Api not hit", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equalsIgnoreCase("200")) {
                        Toast.makeText(OnClearFromRecentService.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OnClearFromRecentService.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StagingPojo> call, Throwable t) {
                Toast.makeText(OnClearFromRecentService.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
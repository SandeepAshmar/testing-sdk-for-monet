package com.monet.mylibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.monet.mylibrary.adapter.LandAdapter;
import com.monet.mylibrary.connection.ApiInterface;
import com.monet.mylibrary.connection.BaseUrl;
import com.monet.mylibrary.modle.getCampDetails.GetCampDetails_Pojo;
import com.monet.mylibrary.modle.getCampDetails.GetCampDetails_Response;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPage extends AppCompatActivity {

    private static final String TAG = "SUPER_AWESOME_APP";
    private static TextView tv_land_watch;
    private static RecyclerView mRecycler;
    private static LandAdapter mAdapter;
    private static ArrayList<GetCampDetails_Response> detailsResponses = new ArrayList<>();
    private static ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mRecycler = findViewById(R.id.recyler_land);
        tv_land_watch = findViewById(R.id.tv_land_watch);

        mAdapter = new LandAdapter(this, detailsResponses);
        mRecycler.setAdapter(mAdapter);
    }

    private static void getCampDetails(final Activity activity, String token, String cmpId) {
        pd.show();
        ApiInterface apiInterface = BaseUrl.getClient().create(ApiInterface.class);
        Call<GetCampDetails_Pojo> pojoCall = apiInterface.getCampDetails(token, cmpId);
        pojoCall.enqueue(new Callback<GetCampDetails_Pojo>() {
            @Override
            public void onResponse(Call<GetCampDetails_Pojo> call, Response<GetCampDetails_Pojo> response) {
                pd.dismiss();
                if (response.body() == null) {
                    Toast.makeText(activity.getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                } else {
                    if (response.body().getCode().equals("200")) {

                        if (response.body().getResponse().size() <= 1) {
                            mRecycler.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 1));
                            tv_land_watch.setText("watch " + response.body().getResponse().size() + " short clip");
                        } else {
                            mRecycler.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), 2));
                            tv_land_watch.setText("watch " + response.body().getResponse().size() + " short clips");
                        }
                        detailsResponses.addAll(response.body().getResponse());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCampDetails_Pojo> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startCampaign(Activity activity, String token, String cmpId) {
        detailsResponses.clear();
        activity.startActivity(new Intent(activity, LandingPage.class));
        pd = new ProgressDialog(activity);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading...");
        getCampDetails(activity, token, cmpId);
    }
}

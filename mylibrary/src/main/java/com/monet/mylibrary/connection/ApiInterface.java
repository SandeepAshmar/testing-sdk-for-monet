package com.monet.mylibrary.connection;

import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Pojo;
import com.monet.mylibrary.model.question.QuestionModelPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("campaignDetails/{cam_id}")
    Call<GetCampDetails_Pojo> getCampDetails(@Header("Authorization") String token, @Path("cam_id") String cam_id);

    @Headers("Content-Type: application/json")
    @GET("sdk/{cam_id}/{user_id}")
    Call<QuestionModelPojo> getSdk(@Path("cam_id") String cam_id, @Path("user_id") String userId);
}
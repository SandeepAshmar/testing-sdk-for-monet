package com.monet.mylibrary.connection;

import com.monet.mylibrary.model.StagingPojo;
import com.monet.mylibrary.model.cmpDetails.GetCampDetails_Pojo;
import com.monet.mylibrary.model.getCampignFlow.GetCampFlowPojo;
import com.monet.mylibrary.model.reaction.ReactionPost;
import com.monet.mylibrary.model.reaction.ReactionResponse;
import com.monet.mylibrary.model.sdk.SdkPojo;
import com.monet.mylibrary.model.survay.SurvayPojo;
import com.monet.mylibrary.model.survay.SurvayPost;
import com.monet.mylibrary.model.video.VideoPojo;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("campaignDetails")
    Call<GetCampDetails_Pojo> getCampDetails(@Header("Authorization") String token, @Query("cmp_id") String cmp_id);

    @Headers("Content-Type: application/json")
    @POST("sdk")
    Call<SdkPojo> getSdk(@Header("licence-key") String value , @Query("cmp_id") String cam_id, @Query("user_id") String userId);

    /*save survay answer*/
    @Headers("Content-Type: application/json")
    @POST("savesurveyanswers")
    Call<SurvayPojo> submitSurvayAns(@Header("Authorization") String token, @Body SurvayPost survayPost);

    /*get camp flow*/
    @Headers("Content-Type: application/json")
    @POST("feedbackStage")
    Call<StagingPojo> sendStagingData(@Header("Authorization") String token, @Query("cmp_id") String cmp_id, @Query("jsonData") JSONObject jsonData);

    @Headers("Content-Type: application/json")
    @POST("getCampaign")
    Call<GetCampFlowPojo> getCampFlow(@Header("Authorization") String token, @Query("cmp_id") String cmp_id);

    @Headers("Content-Type: application/json")
    @POST("savereaction")
    Call<ReactionResponse> submitReactionPart(@Header("Authorization") String token, @Body ReactionPost reactionPost);

    @Headers("Content-Type: application/json")
    @GET("videoUrlToMp4")
    Call<VideoPojo> getMp4VideoUrl(@Query("video_url") String video_url);
}

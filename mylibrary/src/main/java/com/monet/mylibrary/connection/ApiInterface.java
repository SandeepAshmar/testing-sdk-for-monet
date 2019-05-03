package com.monet.mylibrary.connection;

import com.google.gson.JsonObject;
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
    @POST("sdk")
    Call<SdkPojo> getSdk( @Header("licence-key") String licence,@Query("cmp_id") String cam_id, @Query("user_id") String userId);

    /*save survay answer*/
    @Headers({"Content-Type: application/json",
            "licence-key: fsdjhkdjsfhsjkdfhjkdsahfjkdshgjkhd"})
    @POST("storeFeedback")
    Call<SurvayPojo> submitSurvayAns(@Header("Authorization") String token,@Body String survayPost);

    @Headers("Content-Type: application/json")
    @POST("getCampaign")
    Call<GetCampFlowPojo> getCampFlow(@Header("Authorization") String token, @Query("cmp_id") String cmp_id);

    @Headers("Content-Type: application/json")
    @POST("updatePageStage")
    Call<SurvayPojo> updatePageStage(@Header("Authorization") String token, @Header("licence-key") String licence,
                                           @Query("page_stage") String page_stage, @Query("success_flag") int success_flag);

    @Headers({"Content-Type: application/json",
            "licence-key: fsdjhkdjsfhsjkdfhjkdsahfjkdshgjkhd"})
    @POST("reactionCommentData")
    Call<ReactionResponse> submitReactionPart(@Header("Authorization") String token, @Body String jsonObject);
}

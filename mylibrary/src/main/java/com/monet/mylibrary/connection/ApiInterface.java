package com.monet.mylibrary.connection;

import com.monet.mylibrary.model.reaction.ReactionResponse;
import com.monet.mylibrary.model.sdk.SdkPojo;
import com.monet.mylibrary.model.survay.SurvayPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers({"Content-Type: application/json",
            "licence-key: fsdjhkdjsfhsjkdfhjkdsahfjkdshgjkhd"})
    @POST("sdk")
    Call<SdkPojo> getSdk(@Query("cmp_id") String cam_id, @Query("user_id") String userId);

    /*save survay answer*/
    @Headers({"Content-Type: application/json",
            "licence-key: fsdjhkdjsfhsjkdfhjkdsahfjkdshgjkhd"})
    @POST("storeFeedback")
    Call<SurvayPojo> submitSurvayAns(@Header("Authorization") String token,@Body String survayPost);

    /*send app staging*/
    @Headers({"Content-Type: application/json",
            "licence-key: fsdjhkdjsfhsjkdfhjkdsahfjkdshgjkhd"})
    @POST("updatePageStage")
    Call<SurvayPojo> updatePageStage(@Header("Authorization") String token, @Query("page_stage") String page_stage, @Query("success_flag") int success_flag);

    /*save reaction part*/
    @Headers({"Content-Type: application/json",
            "licence-key: fsdjhkdjsfhsjkdfhjkdsahfjkdshgjkhd"})
    @POST("reactionCommentData")
    Call<ReactionResponse> submitReactionPart(@Header("Authorization") String token, @Body String jsonObject);
}

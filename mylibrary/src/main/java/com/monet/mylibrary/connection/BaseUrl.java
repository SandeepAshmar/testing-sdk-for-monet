package com.monet.mylibrary.connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseUrl {

    public static final String BASE_URL = "https://dev.monetrewards.com/appSandbox/api/";
    public static final String VIMEO_BASE_URL ="http://player.vimeo.com/";
    public static final String YOUTUBE_BASE_URL ="https://you-link.herokuapp.com/";

    private static Retrofit retrofit = null;
    private static Retrofit vimeoRetrofit = null;
    private static Retrofit youtubeRetrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getVimeoClient() {
        if (vimeoRetrofit == null) {
            vimeoRetrofit = new Retrofit.Builder()
                    .baseUrl(VIMEO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return vimeoRetrofit;
    }

    public static Retrofit getYoutubeRetrofit(){
        if(youtubeRetrofit == null){
            youtubeRetrofit = new Retrofit.Builder()
                    .baseUrl(YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return youtubeRetrofit;
    }
}

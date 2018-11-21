package com.monet.mylibrary.connection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseUrl {

    public static final String BASE_URL = "http://ci.monetrewards.com/monet-user/api/";
    public static final String VIMEO_BASE_URL ="http://player.vimeo.com/";

    private static Retrofit retrofit = null;
    private static Retrofit vimeoRetrofit = null;

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
}

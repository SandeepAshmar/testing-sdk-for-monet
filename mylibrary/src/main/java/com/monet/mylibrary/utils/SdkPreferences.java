package com.monet.mylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SdkPreferences {
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mPreferencesEditor;

    public static int getCmpLengthCount(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getInt("sdk_cmp_lengthCount", 0);
    }

    public static void setCmpLengthCount(Context ctx, Integer value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putInt("sdk_cmp_lengthCount", value);
        mPreferencesEditor.commit();
    }

    public static String getApiToken(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_api_token", "");
    }

    public static void setApiToken(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_api_token", value);
        mPreferencesEditor.commit();
    }

    public static String getPageStage(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("PageStage", "");
    }

    public static void setPageStage(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("PageStage", value);
        mPreferencesEditor.commit();
    }

    public static String getUserId(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_user_id", "");
    }

    public static void setUserId(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_user_id", value);
        mPreferencesEditor.commit();
    }

    public static String getlicence_key(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("licence-key", "");
    }

    public static void setlicence_key(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("licence-key", value);
        mPreferencesEditor.commit();
    }

    public static Integer getCfId(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getInt("sdk_cfid", 0);
    }

    public static void setCfId(Context ctx, int value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putInt("sdk_cfid", value);
        mPreferencesEditor.commit();
    }

    public static int getCmpLength(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getInt("sdk_cmp_length", 0);
    }

    public static void setCmpLength(Context ctx, Integer value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putInt("sdk_cmp_length", value);
        mPreferencesEditor.commit();
    }

    public static Set<String> getCamEval(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getStringSet("sdk_camEval", null);
    }

    public static void setCamEval(Context ctx, ArrayList<String> value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        Set<String> set = new HashSet<>();
        set.addAll(value);
        mPreferencesEditor.putStringSet("sdk_camEval", set);
        mPreferencesEditor.commit();
    }

    public static String getCmpId(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_cmp_id", "");
    }

    public static void setCmpId(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_cmp_id", value);
        mPreferencesEditor.commit();
    }

    public static int getCmpLengthCountFlag(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getInt("sdk_cmp_lengthCountFlag", 0);
    }

    public static void setCmpLengthCountFlag(Context ctx, Integer value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putInt("sdk_cmp_lengthCountFlag", value);
        mPreferencesEditor.commit();
    }

    public static String getQuestionType(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_quesType", "");
    }

    public static void setQuestionType(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_quesType", value);
        mPreferencesEditor.commit();
    }

    public static String getVideoUrl(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_videoUrl", "");
    }

    public static void setVideoUrl(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_videoUrl", value);
        mPreferencesEditor.commit();
    }

    public static String getThumbUrl(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_thumbUrl", "Nothing");
    }

    public static void setThumbUrl(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_thumbUrl", value);
        mPreferencesEditor.commit();
    }

    public static String getCmpName(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_CmpName", "");
    }

    public static void setCmpName(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_CmpName", value);
        mPreferencesEditor.commit();
    }

    public static String getVideoTime(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPreferences.getString("sdk_VideoTime", "");
    }

    public static void setVideoTime(Context ctx, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.putString("sdk_VideoTime", value);
        mPreferencesEditor.commit();
    }

    public static void clearAllPreferences(Context ctx) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPreferencesEditor = mPreferences.edit();
        mPreferencesEditor.clear();
        mPreferencesEditor.commit();
    }

}

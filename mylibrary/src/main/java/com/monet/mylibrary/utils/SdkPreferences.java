package com.monet.mylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SdkPreferences {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static int getCmpLengthCount(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getInt("sdk_cmp_lengthCount", 0);
    }

    public static void setCmpLengthCount(Context ctx, Integer value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putInt("sdk_cmp_lengthCount", value);
        mPrefsEditor.commit();
    }

    public static String getApiToken(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_api_token", "");
    }

    public static void setApiToken(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_api_token", value);
        mPrefsEditor.commit();
    }

    public static String getUserId(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_user_id", "");
    }

    public static void setUserId(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_user_id", value);
        mPrefsEditor.commit();
    }

    public static Integer getCfId(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getInt("sdk_cfid", 0);
    }

    public static void setCfId(Context ctx, int value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putInt("sdk_cfid", value);
        mPrefsEditor.commit();
    }

    public static int getCmpLength(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getInt("sdk_cmp_length", 0);
    }

    public static void setCmpLength(Context ctx, Integer value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putInt("sdk_cmp_length", value);
        mPrefsEditor.commit();
    }

    public static Set<String> getCamEval(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getStringSet("sdk_camEval", null);
    }

    public static void setCamEval(Context ctx, ArrayList<String> value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        Set<String> set = new HashSet<>();
        set.addAll(value);
        mPrefsEditor.putStringSet("sdk_camEval", set);
        mPrefsEditor.commit();
    }

    public static String getCmpId(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_cmp_id", "");
    }

    public static void setCmpId(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_cmp_id", value);
        mPrefsEditor.commit();
    }

    public static int getCmpLengthCountFlag(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getInt("sdk_cmp_lengthCountFlag", 0);
    }

    public static void setCmpLengthCountFlag(Context ctx, Integer value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putInt("sdk_cmp_lengthCountFlag", value);
        mPrefsEditor.commit();
    }

    public static String getQuestionType(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_quesType", "");
    }

    public static void setQuestionType(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_quesType", value);
        mPrefsEditor.commit();
    }

    public static String getVideoUrl(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_videoUrl", "");
    }

    public static void setVideoUrl(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_videoUrl", value);
        mPrefsEditor.commit();
    }

    public static String getThumbUrl(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_thumbUrl", "Nothing");
    }

    public static void setThumbUrl(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_thumbUrl", value);
        mPrefsEditor.commit();
    }

    public static String getCmpName(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_CmpName", "");
    }

    public static void setCmpName(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_CmpName", value);
        mPrefsEditor.commit();
    }

    public static String getVideoTime(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getString("sdk_VideoTime", "");
    }

    public static void setVideoTime(Context ctx, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("sdk_VideoTime", value);
        mPrefsEditor.commit();
    }

}

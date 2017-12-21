package com.yinhao.chatapp.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp on 2017/12/20.
 */

public class Prefs {

    private static final String PREFS_NAME = "config";

    public static final String PREF_KEY_TOKEN = "token";
    public static final String PREF_KEY_LOGIN_ID = "user_id";
    public static final String PREF_KEY_HEAD_IMAGE_URL = "head_image";
    public static final String PREF_KEY_NIKE_NAME = "nike_name";
    public static final String PREF_KEY_ACCOUNT = "account";
    public static final String PREF_FILE_PATH = "head_file_path";

    public static void putString(Context context, String key, String value) {
        SharedPreferences mPres = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mPres.edit().putString(key, value).apply();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences mPres = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mPres.edit().putInt(key, value).apply();
    }

    public static void putBoolean(Context context, String key, int value) {
        SharedPreferences mPres = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mPres.edit().putInt(key, value).apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences mPres = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return mPres.getString(key, "");
    }

    public static int getInt(Context context, String key) {
        SharedPreferences mPres = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return mPres.getInt(key, -1);
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences mPres = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return mPres.getBoolean(key, false);
    }
}

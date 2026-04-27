package com.app.penjahit.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    static final String KEY_LOGIN =
            "login",
            KEY_ID = "id",
            KEY_NAME = "name",
            KEY_TYPE_USER = "type_user",
            KEY_JURUSAN = "jurusan",
            KEY_INSTITUTE = "institute",
            KEY_EMAIL = "email",
            KEY_HP = "nomor_hp",
            KEY_NUMBER = "number",
            KEY_AVATAR = "avatar",
            KEY_USERNAME = "username",
            KEY_NIK = "nik",
            KEY_ADMIN = "admin",
            KEY_PIN = "pin",
            KEY_SEC_QUESTION = "sec_question",
            KEY_SEC_ANSWER = "sec_answer",
            KEY_SKILL = "skill";

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(
                "zona", Context.MODE_PRIVATE);
    }

    public static void setCustData(Context context, String name, String custId, String email, String phone) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN, true);
        editor.putString(KEY_ID, custId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_HP, phone);
        editor.apply();
    }
    public static void setAdminData(Context context, String name, String custId, String email, String pin, String secQuestion, String secAnswer) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN, false);
        editor.putString(KEY_ID, custId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_SEC_ANSWER, secAnswer);
        editor.putString(KEY_SEC_QUESTION, secQuestion);
        editor.putString(KEY_PIN, pin);
        editor.putBoolean(KEY_ADMIN, true);
        editor.apply();
    }
    public static void setAvatar(Context context, String avatar) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_AVATAR, avatar);
        editor.apply();
    }
    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN, isLogin);

        editor.apply();
    }


    public static String getKeySecQuestion(Context context) {
        return getSharedPreference(context).getString(KEY_SEC_QUESTION, "");
    }
    public static String getKeySecAnswer(Context context) {
        return getSharedPreference(context).getString(KEY_SEC_ANSWER, "");
    }
    public static String getId(Context context) {
        return getSharedPreference(context).getString(KEY_ID, "");
    }
    public static String getPIN(Context context) {
        return getSharedPreference(context).getString(KEY_PIN, "");
    }

    public static String getTypeUser(Context context) {
        return getSharedPreference(context).getString(KEY_TYPE_USER, "");
    }
    public static Boolean getIsAdmin(Context context) {
        return getSharedPreference(context).getBoolean(KEY_ADMIN, false);
    }


    public static String getName(Context context) {
        return getSharedPreference(context).getString(KEY_NAME, "");
    }
    public static String getEmail(Context context) {
        return getSharedPreference(context).getString(KEY_EMAIL, "");
    }

    public static String getAvatar(Context context) {
        return getSharedPreference(context).getString(KEY_AVATAR, "");
    }
    public static String getNoHp(Context context) {
        return getSharedPreference(context).getString(KEY_HP, "");
    }
    public static String getUsername(Context context) {
        return getSharedPreference(context).getString(KEY_USERNAME, "");
    }

    public static String getNik(Context context) {
        return getSharedPreference(context).getString(KEY_NIK, "");
    }

    public static Boolean getIsLogin(Context context) {
        return getSharedPreference(context).getBoolean(KEY_LOGIN, false);
    }

    public static void clearData(Context context) {
        getSharedPreference(context).edit().clear().apply();
    }

}

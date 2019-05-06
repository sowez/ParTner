package com.example.partner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceData {
    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    // 계정 정보 저장
    public static void saveToken(Context context, String token, String type, boolean autologinCheck) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("token", token);
        editor.putString("type", type);
        editor.putBoolean("auto",autologinCheck);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getToken(Context context) {
        return getSharedPreferences(context).getString("token", "");
    }

    public static String getType(Context context) {
        return getSharedPreferences(context).getString("type", "");
    }

    public static boolean getAutologinChecked(Context context) {
        return getSharedPreferences(context).getBoolean("auto", false);
    }

    // 로그아웃
    public static void clearUserData(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}

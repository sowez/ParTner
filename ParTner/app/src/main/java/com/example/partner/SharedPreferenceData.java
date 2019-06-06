package com.example.partner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SharedPreferenceData {

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // 계정 정보 저장
    public static void saveToken(Context context, String id, String token, String username, String type, boolean autologinCheck) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("id", id);
        editor.putString("token", token);
        editor.putString("username", username);
        editor.putString("type", type);
        editor.putBoolean("auto", autologinCheck);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getId(Context context) {
        return getSharedPreferences(context).getString("id", "");
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString("token", "");
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString("username", "");
    }

    public static String getType(Context context) {
        return getSharedPreferences(context).getString("type", "");
    }

    public static boolean getAutologinChecked(Context context) {
        return getSharedPreferences(context).getBoolean("auto", false);
    }

    // 로그아웃
    public static void clearUserData(Context context) {
        String userid = getId(context);
        ServerComm serverComm = new ServerComm();
        serverComm.init();
        serverComm.userLogout(userid);
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}

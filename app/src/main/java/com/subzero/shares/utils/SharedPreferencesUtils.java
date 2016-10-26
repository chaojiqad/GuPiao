package com.subzero.shares.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 记录登录信息   清除登录信息  是否第一次进入APP……
 */
public class SharedPreferencesUtils {

    /**
     * 设置第一次进入 APP
     *
     * @param context
     */
    public static void setOnceIntoAPP(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("app", Activity.MODE_PRIVATE).edit();
        editor.putBoolean("is_once_into", true);
        editor.commit();
    }


    /**
     * 获取是否是第一次进入APP  默认是false
     *
     * @param context
     * @return
     */
    public static boolean getOnceIntoAPP(Context context) {
        SharedPreferences sp = context.getSharedPreferences("app", Activity.MODE_PRIVATE);
        return sp.getBoolean("is_once_into", false);
    }


    /**
     * 存入用户登陆的userID
     *
     * @param context
     * @param user_id
     */
    public static void setUserId(Context context, String user_id) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit();
        editor.putString("user_id", user_id);
        editor.commit();
    }

    /**
     * 获取用户登陆的UserId
     *
     * @param context
     */
    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE);
        return sp.getString("user_id", "");
    }

    /**
     * 存入用户登陆的token_id
     *
     * @param context
     * @param token_id
     */
    public static void setTokenId(Context context, String token_id) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit();
        editor.putString("token_id", token_id);
        editor.commit();
    }


    /**
     * 获取用户登陆的tokenID
     *
     * @param context
     * @return
     */
    public static String getTokenId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE);
        return sp.getString("token_id", "");
    }

    /**
     * 获取类型
     *
     * @param context
     * @return
     */
    public static String getUserType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE);
        return sp.getString("user_type", "");
    }


    /**
     * 设置用户的类型
     *
     * @param context
     */
    public static void setUserType(Context context, String user_type) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit();
        editor.putString("user_type", user_type);
        editor.commit();
    }


    /**
     * 获取用户级别
     *
     * @param context
     * @return
     */
    public static String getUserLevel(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE);
        return sp.getString("user_level", "");
    }


    /**
     * 设置 用户的级别
     *
     * @param context
     */
    public static void setUserLevel(Context context, String user_level) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit();
        editor.putString("user_level", user_level);
        editor.commit();
    }


    /**
     * 设置融云的token
     *
     * @param context
     */
    public static void setUserRcToken(Context context, String rc) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit();
        editor.putString("rc", rc);
        editor.commit();
    }


    /**
     * 获取融云的token
     *
     * @param context
     */
    public static String getUserRcToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE);
        return sp.getString("rc", "");
    }


    /**
     * 设置注册的用户名
     *
     * @param context
     */
    public static void setUserName(Context context, String username) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.commit();
    }

    /**
     * 获取注册的用户名
     *
     * @param context
     */
    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE);
        return sp.getString("username", "");
    }





    /**
     * 清除用户的登录信息
     *
     * @param context
     */
    public static void clearUserId_tokenId(Context context) {
        context.getSharedPreferences("user_id_token_id_user_type_level_rc_name", Activity.MODE_PRIVATE).edit().clear().commit();
    }



}

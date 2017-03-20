package com.hbung.utils.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 作者　　: 李坤
 * 创建时间: 13:53 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：SharedPreferences 操作类
 */

public class SharedPreferencesUtils {

    public final static String USER_DATA = "user";

	/*
     * ����SharedPreferences�ַ�����ֵ��
	 */

    public static void setSharedPreferencesKeyAndValue(Context context,
                                                       String name, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        // ��ȡEditor����
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /*
     * ��ȡSharedPreferences�ַ�����ֵ��
     */
    public static String getSharedPreferencesKeyAndValue(Context context,
                                                         String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        try {
            return sp.getString(key, "");
        } catch (Exception e) {
            return "";
        }

    }

    /*
     * ����SharedPreferences������ֵ��
     */
    public static void setSharedPreferencesKeyAndValue(Context context,
                                                       String name, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        // ��ȡEditor����
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /*
     * ��ȡSharedPreferences������ֵ��
     */
    public static Boolean getSharedPreferencesKeyAndValue(Context context,
                                                          String name, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        return sp.getBoolean(key, value);
    }

	/*
     * ����SharedPreferences���ͼ�ֵ��
	 */

    public static void setSharedPreferencesKeyAndValue(Context context,
                                                       String name, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        // ��ȡEditor����
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /*
     * ��ȡSharedPreferences���ͼ�ֵ��
     */
    public static int getSharedPreferencesKeyAndValue(Context context,
                                                      String name, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        return sp.getInt(key, value);
    }

    public static void setSharedPreferencesKeyAndValue(Context context,
                                                       String name, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        // ��ȡEditor����
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /*
     * ��ȡSharedPreferences���ͼ�ֵ��
     */
    public static long getSharedPreferencesKeyAndValue(Context context,
                                                       String name, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        return sp.getLong(key, value);
    }
}

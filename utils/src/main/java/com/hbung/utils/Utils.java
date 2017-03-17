package com.hbung.utils;

import android.app.Application;

/**
 * 作者　　: 李坤
 * 创建时间:2016/12/30　9:59
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class Utils {
    public static boolean isDebug;
    public static String customTagPrefix;
    public static String BASE_URL;
    public static Application myApp;

    public static void init(Application context) {
        myApp = context;
    }

    public static void setIsDebug(boolean isDebug) {
        Utils.isDebug = isDebug;
    }

    public static void setCustomTagPrefix(String customTagPrefix) {
        Utils.customTagPrefix = customTagPrefix;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }
}

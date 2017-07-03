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
    public static Application myApp;

    //尽量每个地方
    public static void init(Application context, boolean isDebug) {
        myApp = context;
        Utils.isDebug = isDebug;
    }
}

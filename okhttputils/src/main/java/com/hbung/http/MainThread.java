package com.hbung.http;

import android.os.Handler;
import android.os.Looper;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/23　14:08
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class MainThread {
    private final Handler handler = new Handler(Looper.getMainLooper());

    public void execute(Runnable r) {
        handler.post(r);
    }
}
